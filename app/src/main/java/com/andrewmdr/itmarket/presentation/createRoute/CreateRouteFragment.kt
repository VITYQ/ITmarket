package com.andrewmdr.itmarket.presentation.createRoute

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.andrewmdr.itmarket.R
import com.andrewmdr.itmarket.data.model.Route
import com.andrewmdr.itmarket.databinding.FragmentCreateOrderBinding
import com.andrewmdr.itmarket.databinding.FragmentCreateRouteBinding
import com.andrewmdr.itmarket.presentation.createOrder.CreateOrderViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class CreateRouteFragment : Fragment() {

    private lateinit var binding: FragmentCreateRouteBinding
    lateinit var viewModel: CreateRouteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(requireActivity()).get(CreateRouteViewModel::class.java)
        binding = FragmentCreateRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.date.observe(viewLifecycleOwner) {
            Log.d("checktime", it.toString())
            val dateFormatterDate = DateTimeFormatter.ofPattern("dd MMMM yyyy")
            val dateFormatterTime = DateTimeFormatter.ofPattern("HH:mm")
            val textDate = it.format(dateFormatterDate)
            val textTime = it.format(dateFormatterTime)
            binding.tilDate.editText?.setText(textDate)
            binding.tilTime.editText?.setText(textTime)
        }

        viewModel.successHandler.observe(viewLifecycleOwner){
            if (it){
                val bundle = Bundle()
                bundle.putLong("routeId", binding.tilRouteId.editText?.text.toString().toLong())
                bundle.putBoolean("isAdmin", true)
                findNavController().navigate(R.id.action_createRouteFragment_to_ordersListFragment, bundle)
                viewModel.successHandler.postValue(false)
            }
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


        binding.mbCreateRoute.setOnClickListener {
            with(binding){
                val routeId = tilRouteId.editText?.text.toString()
                val date = tilDate.editText?.text
                val time = tilTime.editText?.text
                val address = tilAddress.editText?.text.toString()
                if(routeId.isNullOrEmpty() || date.isNullOrEmpty() || time.isNullOrEmpty()
                    || address.isNullOrEmpty()){
                    Toast.makeText(requireContext(), "Нельзя оставлять поля пустыми",
                        Toast.LENGTH_SHORT).show()
                }
                else{
                    MaterialAlertDialogBuilder(requireContext())
                        .setMessage("Вы хотите создать маршрут?")
                        .setPositiveButton("Да") {dialog, which ->
                            viewModel.createRoute(routeId, address)
                        }
                        .setNegativeButton("Нет") {dialog, which ->}
                        .show()

                }

            }
        }
    }

}