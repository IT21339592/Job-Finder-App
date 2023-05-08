package com.example.jobadd.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobadd.R
import com.example.jobadd.models.Profile

class ProfileAdapter (private val profileList: ArrayList<Profile>) : RecyclerView.Adapter<ProfileAdapter.ViewHolder>(){
    private lateinit var mListener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int) : ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.profile_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder : ViewHolder, position: Int) {
        val currentProfile = profileList[position]
        holder.tvFullName.text = currentProfile.fullName
        holder.tvUserEmail.text = currentProfile.useremail
        holder.tvUserName.text = currentProfile.username
        holder.tvPhoneNumber.text = currentProfile.phoneNumber
        holder.tvAddress.text = currentProfile.address
    }

    override fun getItemCount() : Int{
        return profileList.size
    }

    class ViewHolder(itemView: View, clickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView){
        val tvFullName : TextView = itemView.findViewById(R.id.tvSetFullName)
        val tvUserEmail : TextView = itemView.findViewById(R.id.tvSetUserEmail)
        val tvUserName : TextView = itemView.findViewById(R.id.tvSetUserName)
        val tvPhoneNumber : TextView = itemView.findViewById(R.id.tvSetPhoneNumber)
        val tvAddress : TextView = itemView.findViewById(R.id.tvSetAddress)

        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}