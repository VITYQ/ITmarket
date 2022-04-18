package com.andrewmdr.itmarket.presentation.orderslist

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.andrewmdr.itmarket.MainActivity
import com.andrewmdr.itmarket.R
import com.andrewmdr.itmarket.databinding.FragmentOrdersListBinding
import com.andrewmdr.itmarket.data.model.Order
import com.andrewmdr.itmarket.data.retrofit.Status
import com.andrewmdr.itmarket.presentation.order.OrderViewModel
import com.andrewmdr.itmarket.presentation.orderslist.adapters.OrdersRecyclerAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class OrdersListFragment : Fragment() {

    private lateinit var binding : FragmentOrdersListBinding
    lateinit var viewModel: OrdersListViewModel
    var isAdmin = false
    var routeId : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(requireActivity()).get(OrdersListViewModel::class.java)
        viewModel.ordersLiveData.value = null
        binding = FragmentOrdersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_appbar, menu)
        if(isAdmin){
            menu.findItem(R.id.menu_delete).setVisible(true)
        }
        else{
            menu.findItem(R.id.menu_delete).setVisible(false)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
                R.id.menu_delete -> {Log.d("checkdelete", "deleted")
                    MaterialAlertDialogBuilder(requireContext())
                        .setMessage("Вы хотите удалить маршрут?")
                        .setPositiveButton("Да") {dialog, which ->
                            viewModel.closeRoute(routeId)
                            (activity as MainActivity).onBackPressed()
                        }
                        .setNegativeButton("Нет") {dialog, which ->}
                        .show()


            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = mutableListOf<Order>()
        routeId = arguments!!.getLong("routeId")
        isAdmin = arguments!!.getBoolean("isAdmin")
        Log.d(
            "checkargs", "id: $routeId"
        )
        if (routeId == 0L){
            viewModel.fetchCurrentRoute()
        }
        else{
            viewModel.fetchOrdersList(routeId, false)
        }

        setUpView()

        viewModel.routeId.observe(viewLifecycleOwner){
            routeId = it
        }

        viewModel.ordersLiveData.observe(viewLifecycleOwner){
            if(it != null)
            when (it.status){
                Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()){
                        Log.d("checkadmin", "in orders list: $isAdmin")
                        val recyclerAdapter = OrdersRecyclerAdapter(requireContext(), it.data, isAdmin)
                        binding.rvOrders.adapter = recyclerAdapter
                    }

                }
                Status.ERROR -> {Toast.makeText(requireContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show()}
            }
        }

        viewModel.routeStatus.observe(viewLifecycleOwner){
            when(it){
                RouteStatus.CREATED -> {
                    with(activity as MainActivity){
                        binding.lpiMain.visibility = View.GONE
                        supportActionBar?.subtitle = ""
                    }

                    with(binding.mbStartRoute){
                        if (isAdmin){
                            text = "Создать заказ"
                            setOnClickListener {
                                val bundle = Bundle()
                                bundle.putLong("routeId", routeId)
                                findNavController().navigate(R.id.action_ordersListFragment_to_createOrderFragment, bundle)
                            }
                        }
                        else{
                            text = "Начать маршрут"
                            setOnClickListener {
                                MaterialAlertDialogBuilder(context)
                                    .setMessage("Вы хотите начать маршрут?")
                                    .setPositiveButton("Да") {dialog, which ->
                                        viewModel.takeRoute(routeId)
                                        viewModel.routeStatus.postValue(RouteStatus.IS_TAKEN)
                                    }
                                    .setNegativeButton("Нет") {dialog, which ->}
                                    .show()

                            }
                        }

                    }

                }
                RouteStatus.IS_TAKEN -> {
                    with(activity as MainActivity){
                        supportActionBar?.subtitle = "От: ${viewModel.startPoint.value}"
                        binding.lpiMain.visibility = View.GONE
                    }

                    with(binding.mbStartRoute){
                        text = "Завершить маршрут"
                        setOnClickListener {
                            MaterialAlertDialogBuilder(context)
                                .setMessage("Вы хотите завершить маршрут?")
                                .setPositiveButton("Да") {dialog, which ->
                                    viewModel.closeRoute(routeId)
                                }
                                .setNegativeButton("Нет") {dialog, which ->}
                                .show()
                        }
                    }
                }
                RouteStatus.LOADING -> {
                    (activity as MainActivity).binding.lpiMain.visibility = View.VISIBLE
                    (activity as MainActivity).supportActionBar?.subtitle = ""
                }
                RouteStatus.EMPTY_SCREEN -> {
                    viewModel.ordersLiveData.value = null
                    with(binding.mbStartRoute){
                        if (isAdmin){
                            text = "Создать заказ"
                            setOnClickListener {
                                val bundle = Bundle()
                                bundle.putLong("routeId", routeId)
                                findNavController().navigate(R.id.action_ordersListFragment_to_createOrderFragment, bundle)
                            }
                        }
                        else{
                            text = "Вернуться"
                            setOnClickListener {
                                (activity as MainActivity).onBackPressed()
                                (activity as MainActivity).supportActionBar?.subtitle = "Нет маршрута"
                            }
                        }


                    }
                    (activity as MainActivity).binding.lpiMain.visibility = View.GONE
                }
            }
        }




    }

    private fun setUpView() {
        if (isAdmin){
            binding.mbStartRoute.text = "Создать заказ"
        }
        else{
            binding.mbStartRoute.text = "Взять маршрут"
        }
    }


}