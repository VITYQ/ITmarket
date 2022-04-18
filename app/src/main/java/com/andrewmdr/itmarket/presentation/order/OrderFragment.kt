package com.andrewmdr.itmarket.presentation.order

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.andrewmdr.itmarket.MainActivity
import com.andrewmdr.itmarket.R
import com.andrewmdr.itmarket.data.model.Order
import com.andrewmdr.itmarket.databinding.FragmentOrderBinding
import com.andrewmdr.itmarket.presentation.login.LoginViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class OrderFragment : Fragment() {

    lateinit var binding: FragmentOrderBinding
    lateinit var viewModel: OrderViewModel
    var isAdmin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(OrderViewModel::class.java)
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        val view = binding.root
        //admin = arguments!!.getBoolean("is_admin")
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_appbar, menu)
        if (isAdmin){

            menu.findItem(R.id.menu_delete).setVisible(true)
        }
        else {
            menu.findItem(R.id.menu_delete).setVisible(false)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menu_delete -> {
                Log.d("checkdelete", "deleted")
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage("Вы хотите удалить маршрут?")
                    .setPositiveButton("Да") {dialog, which ->
                        viewModel.deleteOrder()
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
//        viewModel.order = arguments?.getParcelable("order")!!
        viewModel.order = findNavController().previousBackStackEntry?.arguments?.getParcelable<Order>("order")!!
        isAdmin = findNavController().previousBackStackEntry?.arguments?.getBoolean("isAdmin")!!
//        isAdmin = arguments!!.getBoolean("isAdmin")
        Log.d("checkadmin", "isadmin $isAdmin")
        if (isAdmin){

        }
        setUpView()
        setupPhoneButton()
        setupMapButton()


    }

    private fun setUpView() {

        binding.tvStreet.text = viewModel.order.address
        binding.tvBuilding.text = viewModel.order.building.toString()
        binding.tvHouse.text = viewModel.order.house.toString()
        binding.tvFloor.text = viewModel.order.floor.toString()
        binding.tvOrder.text = viewModel.order.id.toString()
        binding.tvService.text = viewModel.order.service
        binding.tvName.text = viewModel.order.name
        val date = LocalDateTime.parse(viewModel.order.date.dropLast(6))
        val formattedDate = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        val formattedTime = date.format(DateTimeFormatter.ofPattern("HH:mm"))
        binding.tvDate.text = formattedDate
        binding.tvTime.text = formattedTime
        binding.tvPhone.text = viewModel.order.phone
        binding.tvComment.text = viewModel.order.comment





        binding.mbComplete.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage("Вы хотите завершить заказ?")
                .setPositiveButton("Да") {dialog, which ->
                    viewModel.completeOrder()
                    viewModel.order.orderStatus = "COMPLETED"
                    setUpForStatus()
                }
                .setNegativeButton("Нет") {dialog, which ->}
                .show()
        }

        binding.mbCancel.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage("Вы хотите отменить заказ?")
                .setPositiveButton("Да") {dialog, which ->
                    viewModel.closeOrder()
                    viewModel.order.orderStatus = "CANCELED"
                    setUpForStatus()
                }
                .setNegativeButton("Нет") {dialog, which ->}
                .show()
        }

        binding.mbAccept.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage("Вы хотите принять заказ?")
                .setPositiveButton("Да") {dialog, which ->
                    viewModel.takeOrder()
                    viewModel.order.orderStatus = "IN_COMPLETE"
                    setUpForStatus()
                }
                .setNegativeButton("Нет") {dialog, which ->}
                .show()

        }

        setUpForStatus()

    }

    fun setUpForStatus(){
        when(viewModel.order.orderStatus) {
            "CREATED" -> {
                if (isAdmin) hideAllButtons()
                else setupButtonsForCreated()

                (activity as MainActivity).supportActionBar?.subtitle = "В ожидании"
            }
            "IN_COMPLETE" -> {
                if (isAdmin) hideAllButtons()
                else setupButtonsForInComplete()
                (activity as MainActivity).supportActionBar?.subtitle = "В пути"
            }
            "COMPLETED" -> {
                if (isAdmin) hideAllButtons()
                else hideAllButtons()
                (activity as MainActivity).supportActionBar?.subtitle = "Доставлен"
            }
            "CANCELED" -> {
                (activity as MainActivity).supportActionBar?.subtitle = "Отменен"
                hideAllButtons()
            }
        }
    }


    fun setupPhoneButton() {
        binding.mbCallClient.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, String.format("tel:%s", Uri.encode(viewModel.order.phone)).toUri()))
        }
    }

    fun setupMapButton() {
        binding.mbFindOnMap.setOnClickListener {
            val fullAddress = "${viewModel.order.address}, д. ${viewModel.order.house} к. ${viewModel.order.building}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$fullAddress"))
            context?.startActivity(intent)
        }
    }

    fun hideAllButtons() {
        with(binding){
            val buttonsList = listOf(mbAccept, mbFindOnMap, mbCallClient, mbCancel, mbComplete)
            buttonsList.forEach { it.visibility = View.GONE }
        }
    }

    fun setupButtonsForCreated() {
        with(binding){
            val buttonsListToShow = listOf(mbAccept, mbFindOnMap, mbCallClient)
            val buttonsListToHide = listOf(mbCancel, mbComplete)
            buttonsListToHide.forEach { it.visibility = View.GONE }
            buttonsListToShow.forEach { it.visibility = View.VISIBLE }
        }
    }

    fun setupButtonsForInComplete(){
        with(binding){
            val buttonsListToShow = listOf(mbCancel, mbComplete, mbFindOnMap, mbCallClient)
            val buttonsListToHide = listOf(mbAccept)
            buttonsListToHide.forEach { it.visibility = View.GONE }
            buttonsListToShow.forEach { it.visibility = View.VISIBLE }
        }
    }


}