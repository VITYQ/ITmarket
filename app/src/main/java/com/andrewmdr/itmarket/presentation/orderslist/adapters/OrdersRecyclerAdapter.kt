package com.andrewmdr.itmarket.presentation.orderslist.adapters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.andrewmdr.itmarket.R
import com.andrewmdr.itmarket.databinding.ItemOrderBinding
import com.andrewmdr.itmarket.data.model.Order

class OrdersRecyclerAdapter (val context: Context, val list: List<Order>, val isAdmin: Boolean): RecyclerView.Adapter<OrdersRecyclerAdapter.OrderItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderItemViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        holder.bind(list[position], isAdmin)
    }




    class OrderItemViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Order, isAdmin: Boolean) = with(itemView) {
            with(binding){

                val fullAddress = "${item.address}, д. ${item.house} к. ${item.building}, этаж ${item.floor}"

                tvAddress.text = fullAddress
                tvName.text = item.name

                when(item.orderStatus){
                    "CREATED" -> {
                        cvLight.setCardBackgroundColor(resources.getColor(R.color.light_gray))
                        tvStatus.text = "В ожидании"
                    }
                    "IN_COMPLETE" -> {
                        cvLight.setCardBackgroundColor(resources.getColor(R.color.light_yellow))
                        tvStatus.text = "В пути"
                    }
                    "COMPLETED" -> {
                        cvLight.setCardBackgroundColor(resources.getColor(R.color.light_green))
                        tvStatus.text = "Доставлен"
                    }
                    "CANCELED" -> {
                        cvLight.setCardBackgroundColor(resources.getColor(R.color.light_red))
                        tvStatus.text = "Отменен"
                    }
                }

                cvOrder.setOnClickListener {
//                    Log.d("checkbundle", "item: $item")
//                    Log.d("checkadmin", "in rv $isAdmin")
//                    val bundle = Bundle()
//                    bundle.putBoolean("isAdmin", isAdmin)
                    findNavController().currentBackStackEntry?.arguments?.putBoolean("isAdmin", isAdmin)
                    findNavController().currentBackStackEntry?.arguments?.putParcelable("order", item)
                    findNavController().navigate(R.id.action_ordersListFragment_to_orderFragment)
                }
            }
        }

    }


}