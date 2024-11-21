package com.example.sqlite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlite.R
import com.example.sqlite.model.User

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var userList: List<User> = listOf()
    private var selectedUserId: Int? = null

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val ageTextView: TextView = itemView.findViewById(R.id.ageTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.nameTextView.text = user.name
        holder.ageTextView.text = "Edad: ${user.age}"

        // Manejo de selección del usuario
        holder.itemView.setOnClickListener {
            selectedUserId = user.id
        }
    }

    override fun getItemCount(): Int = userList.size

    fun submitList(users: List<User>) {
        userList = users
        notifyDataSetChanged()
    }

    fun getSelectedUserId(): Int? = selectedUserId
}
