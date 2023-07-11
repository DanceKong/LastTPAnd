package com.example.endp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(private val orderList: List<MenuOrder>) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderIdTextView: TextView = itemView.findViewById(R.id.orderIdTextView)
        val orderDateTextView: TextView = itemView.findViewById(R.id.orderDateTextView)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        val menuNameTextView: TextView = itemView.findViewById(R.id.menuNameTextView)
        val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        val orderNumberTextView: TextView = itemView.findViewById(R.id.orderNumberTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orderList[position]

        holder.orderIdTextView.text = order.id.toString()
        holder.orderDateTextView.text = order.orderDate
        holder.quantityTextView.text = order.quantity.toString()
        holder.menuNameTextView.text = order.menuId.toString()
        holder.usernameTextView.text = order.username
        holder.orderNumberTextView.text = order.orderNumber
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}