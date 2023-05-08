package com.example.jobadd.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobadd.R
import com.example.jobadd.models.Feedbacks

class FeedbackAdapter (private val feedbackList: ArrayList<Feedbacks>) : RecyclerView.Adapter<FeedbackAdapter.ViewHolder>(){
    private lateinit var mListener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int) : ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feedback_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder : ViewHolder, position: Int) {
        val currentFeedback = feedbackList[position]
        holder.tvName.text = currentFeedback.name
        holder.tvEmail.text = currentFeedback.email
        holder.tvRate.text = currentFeedback.rate.toString()
        holder.tvFeedback.text = currentFeedback.feedback
    }

    override fun getItemCount() : Int{
        return feedbackList.size
    }

    class ViewHolder(itemView: View, clickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView){
        val tvName : TextView = itemView.findViewById(R.id.tvSetName)
        val tvEmail : TextView = itemView.findViewById(R.id.tvSetEmail)
        val tvFeedback : TextView = itemView.findViewById(R.id.tvSetFeedback)
        val tvRate : TextView = itemView.findViewById(R.id.tvSetRate)

        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}