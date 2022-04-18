package com.andrewmdr.itmarket.presentation.route

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.andrewmdr.itmarket.MainActivity
import com.andrewmdr.itmarket.R
import com.andrewmdr.itmarket.databinding.FragmentOrdersBinding
import com.andrewmdr.itmarket.data.model.Route
import com.andrewmdr.itmarket.data.retrofit.Status
import com.andrewmdr.itmarket.presentation.order.OrderViewModel

import com.andrewmdr.itmarket.presentation.route.adapters.RoutesRecyclerAdapter
import org.w3c.dom.Text
import java.time.LocalDateTime


class OrdersFragmnt : Fragment() {

    private lateinit var binding: FragmentOrdersBinding
    lateinit var viewModel: RouteViewModel
    var isAdmin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(requireActivity()).get(RouteViewModel::class.java)
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchRoutes(viewModel.currentId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchUserInfo()

        binding.tilChooseCompany.editText?.addTextChangedListener { companyName ->
            (activity as MainActivity).supportActionBar?.subtitle = companyName
            val id = viewModel.companiesLiveData.value?.data?.find { it.name == companyName.toString() }?.id!!
            viewModel.fetchRoutes(id)
        }


        binding.mbCreateRoute.setOnClickListener {
            findNavController().navigate(R.id.action_routesFragment_to_createRouteFragment)
        }


        viewModel.isLoading.observe(viewLifecycleOwner){
            if(it) (activity as MainActivity).binding.lpiMain.visibility = View.VISIBLE
            else (activity as MainActivity).binding.lpiMain.visibility = View.GONE
        }

        viewModel.userLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS ->{
                    val navDrawer = (activity as MainActivity).binding.nvMain

                    val name = navDrawer.getHeaderView(0).findViewById<TextView>(R.id.tv_name)
                    val phone = navDrawer.getHeaderView(0).findViewById<TextView>(R.id.tv_phone)
                    val status = navDrawer.getHeaderView(0).findViewById<TextView>(R.id.tv_status)
                    name.text = it.data?.firstName + " " + it.data?.lastName
                    phone.text = it.data?.phone

                    Log.d("check user", it.data.toString())
                    if(it.data?.companyDTO == null){
                        status.text = "Курьер"
                        isAdmin = false
                        binding.tilChooseCompany.visibility = View.VISIBLE
                        binding.mbCreateRoute.visibility = View.GONE
                        navDrawer.menu.findItem(R.id.routesFragment).setTitle("Доступные маршруты")
                        navDrawer.menu.findItem(R.id.ordersListFragment).isVisible = true

                    }
                    else {
                        status.text = "Администратор"
                        isAdmin = true
                        binding.tilChooseCompany.visibility = View.GONE
                        binding.mbCreateRoute.visibility = View.VISIBLE
                        navDrawer.menu.findItem(R.id.routesFragment).setTitle("Маршруты компании")
                        navDrawer.menu.findItem(R.id.ordersListFragment).isVisible = false
                        viewModel.fetchRoutes(it.data.companyDTO.id)
                    }

                }
                Status.ERROR -> Toast.makeText(requireContext(), "Network problem", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.companiesLiveData.observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS -> {
                    val list = mutableListOf<String>()
                    it.data?.forEach { list.add(it.name) }

                    val adapter = ArrayAdapter(requireContext(), R.layout.item_companies_list, list)
                    (binding.tilChooseCompany.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                }
                Status.ERROR -> Toast.makeText(requireContext(), "Network problem", Toast.LENGTH_SHORT).show()
            }

        }



        viewModel.routesLiveData.observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS -> {
                    val list = viewModel.routesLiveData.value?.data
                    if(list.isNullOrEmpty()){
                        binding.tvNothing.visibility = View.VISIBLE
                        Log.d("checkvis", "list ${list.toString()}")
                    }
                    else{
                        binding.tvNothing.visibility = View.GONE
                        Log.d("checkvis", "list2 ${list.toString()}")
                    }
                    val recyclerAdapter = RoutesRecyclerAdapter(requireContext(), list, isAdmin)
                    binding.rvRoutes.adapter = recyclerAdapter
                }
                Status.ERROR -> Toast.makeText(requireContext(), "Network problem", Toast.LENGTH_SHORT).show()
                Status.LOADING -> Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
            }


        }




    }




}