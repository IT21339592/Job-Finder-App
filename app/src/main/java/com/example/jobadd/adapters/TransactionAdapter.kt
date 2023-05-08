package com.example.jobadd.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobadd.R
import com.example.jobadd.models.Transactions

class TransactionAdapter (private val transactionList: ArrayList<Transactions>) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentJob = transactionList[position]
        holder.tvTransactionName.text = currentJob.transactionName
        holder.tvAccHolderName.text = currentJob.accHolderName
        holder.tvAccNumber.text = currentJob.accNumber
        holder.tvBranch.text = currentJob.branch
        holder.tvAmount.text = currentJob.amount.toString()
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    class ViewHolder(itemView: View, clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val tvTransactionName: TextView = itemView.findViewById(R.id.tvSetTransName)
        val tvAccHolderName: TextView = itemView.findViewById(R.id.tvSetAccName)
        val tvAccNumber: TextView = itemView.findViewById(R.id.tvSetAccNumber)
        val tvBranch: TextView = itemView.findViewById(R.id.tvSetBranch)
        val tvAmount: TextView = itemView.findViewById(R.id.tvSetAmount)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}