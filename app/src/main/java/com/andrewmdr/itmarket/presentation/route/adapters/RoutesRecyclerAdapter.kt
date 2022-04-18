package com.andrewmdr.itmarket.presentation.route.adapters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.andrewmdr.itmarket.R
import com.andrewmdr.itmarket.databinding.ItemRouteBinding
import com.andrewmdr.itmarket.data.model.Route
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RoutesRecyclerAdapter (val context: Context, val listRoutes: List<Route>?, val isAdmin: Boolean): RecyclerView.Adapter<RoutesRecyclerAdapter.RouteItemViewHolder>() {

    val list = listRoutes



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteItemViewHolder {
        val binding = ItemRouteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RouteItemViewHolder(binding)
    }

    override fun getItemCount() : Int {
        if(list.isNullOrEmpty()) return 0
        else return list.size
    }

    override fun onBindViewHolder(holder: RouteItemViewHolder, position: Int) {
        list?.get(position)?.let { holder.bind(it, isAdmin) }
    }




    class RouteItemViewHolder(val binding: ItemRouteBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Route, isAdmin: Boolean) = with(itemView) {
            binding.tvRoute.text = item.id.toString()
            binding.tvAddress.text = item.address

            //            val formattedDate = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm")
//            val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//            val date = LocalDateTime.parse(item.date, dateTimeFormatter)
            val date = LocalDateTime.parse(item.date.dropLast(6))
            val formattedDate = date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm"))
            binding.tvDate.text = formattedDate


            binding.tvDate.text = formattedDate

            binding.cvRoute.setOnClickListener {
                val bundle = Bundle()
                bundle.putLong("routeId", item.id)
                bundle.putBoolean("isAdmin", isAdmin)

                Log.d("checkcheck", "in rv id: ${item.id}")
                findNavController().navigate(R.id.action_ordersFragment_to_ordersListFragment, bundle)
            }
        }

    }


}