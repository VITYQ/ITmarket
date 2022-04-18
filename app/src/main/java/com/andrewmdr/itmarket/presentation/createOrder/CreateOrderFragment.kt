package com.andrewmdr.itmarket.presentation.createOrder

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.andrewmdr.itmarket.MainActivity
import com.andrewmdr.itmarket.R
import com.andrewmdr.itmarket.data.model.OrderBody
import com.andrewmdr.itmarket.databinding.FragmentCreateOrderBinding
import com.andrewmdr.itmarket.databinding.FragmentOrdersBinding
import com.andrewmdr.itmarket.presentation.route.RouteViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class CreateOrderFragment : Fragment() {

    private lateinit var binding: FragmentCreateOrderBinding
    lateinit var viewModel: CreateOrderViewModel
    var routeId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(requireActivity()).get(CreateOrderViewModel::class.java)
        binding = FragmentCreateOrderBinding.inflate(inflater, container, false)
        routeId = arguments!!.getLong("routeId")
        Log.d(
            "checkargs", "id: $routeId"
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPickers()

        setupButton()

        viewModel.successHandler.observe(viewLifecycleOwner){
            if(it){
                (activity as MainActivity).onBackPressed()
                viewModel.successHandler.postValue(false)
            }
        }


    }

    private fun setupButton() {
        binding.mbCreateRoute.setOnClickListener {
            with(binding){
                val id = tilOrderId.editText?.text.toString()
                val packageCount = tilCount.editText?.text.toString()
                val name = tilName.editText?.text.toString()
                val address = tilAddress.editText?.text.toString()
                val house = tilHouse.editText?.text.toString()
                val service = tilService.editText?.text.toString()
                val date = tilDate.editText?.text.toString()
                val time = tilTime.editText?.text.toString()
                val phone = tilPhone.editText?.text.toString()
                val building = tilBuilding.editText?.text.toString()
                val entrance = tilEntrance.editText?.text.toString()
                val floor = tilFloor.editText?.text.toString()
                val comment = tilComment.editText?.text.toString()
                val price = tilPrice.editText?.text.toString()
                if (id.isEmpty() || packageCount.isEmpty() || address.isEmpty() ||
                        house.isEmpty() || service.isEmpty() || date.isEmpty() ||
                        time.isEmpty() || phone.isEmpty() || building.isEmpty() ||
                        entrance.isEmpty() || floor.isEmpty() || price.isEmpty()){
                    Toast.makeText(requireContext(), "Нельзя оставлять поля путсыми",
                    Toast.LENGTH_SHORT).show()
                }
                else{
                    MaterialAlertDialogBuilder(requireContext())
                        .setMessage("Вы хотите создать заказ?")
                        .setPositiveButton("Да") {dialog, which ->
                            viewModel.createOrder(routeId!!, id, packageCount, address, house, service, phone, building,
                                entrance, floor, comment, price, name)
                        }
                        .setNegativeButton("Нет") {dialog, which ->}
                        .show()

                }
            }
        }
    }

    private fun setupPickers() {

        viewModel.date.observe(viewLifecycleOwner) {
            Log.d("checktime", it.toString())
            val dateFormatterDate = DateTimeFormatter.ofPattern("dd MMMM yyyy")
            val dateFormatterTime = DateTimeFormatter.ofPattern("HH:mm")
            val textDate = it.format(dateFormatterDate)
            val textTime = it.format(dateFormatterTime)
            binding.tilDate.editText?.setText(textDate)
            binding.tilTime.editText?.setText(textTime)
        }

        binding.tietTime.setOnClickListener {
            val date = LocalTime.now()
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(date.hour)
                .setMinute(date.minute)
                .setTitleText("Выберите время")
                .build()

            picker.show(childFragmentManager, "ff")

            picker.addOnPositiveButtonClickListener {
                viewModel.setTime(picker.hour, picker.minute)
            }
        }

        binding.tietDate.setOnClickListener{
            val date = LocalTime.now()
            val picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату")
                .build()

            picker.show(childFragmentManager, "ff")

            picker.addOnPositiveButtonClickListener {
                val timestamp = picker.selection
                viewModel.setDate(timestamp!!)
            }
        }
    }


}