package com.kotlin.mvvm_room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.mvvm_room.databinding.UserListRowBinding
import com.kotlin.mvvm_room.model.User
import java.util.ArrayList


class UserRecyclerViewAdapter(private val clickListener: (User) -> Unit) :
    RecyclerView.Adapter<MyViewHolder>() {
    private val usersList = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : UserListRowBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.user_list_row, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(usersList[position], clickListener)
    }

    fun setList(users: List<User>) {
        usersList.clear()
        usersList.addAll(users)
    }
}

class MyViewHolder(private val binding: UserListRowBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User, clickListener: (User) -> Unit) {
        binding.nameTextView.text = user.name
        binding.emailTextView.text = user.email
        binding.usernameTextView.text = user.username
        binding.phoneTextView.text = user.phone
        binding.listItemLayout.setOnClickListener {
            clickListener(user)
        }
    }
}
