package com.example.jobadd.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobadd.R
import com.example.jobadd.models.Jobs

class JobAdapter (private val jobList: ArrayList<Jobs>) : RecyclerView.Adapter<JobAdapter.ViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.job_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentJob = jobList[position]
        holder.tvJobName.text = currentJob.jobName
        holder.tvCompany.text = currentJob.company
        holder.tvBudget.text = currentJob.budget.toString()
        holder.tvStartDate.text = currentJob.startDate
    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    class ViewHolder(itemView: View, clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val tvJobName: TextView = itemView.findViewById(R.id.tvSetJobName)
        val tvCompany: TextView = itemView.findViewById(R.id.tvSetCompany)
        val tvBudget: TextView = itemView.findViewById(R.id.tvSetBudget)
        val tvStartDate: TextView = itemView.findViewById(R.id.tvSetStartDate)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}