package com.kamikadze328.whoisthefirst.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kamikadze328.whoisthefirst.R
import com.kamikadze328.whoisthefirst.data.ServerDTO

class ServerAdapter : ListAdapter<ServerDTO, ServerViewHolder>(ServerCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerViewHolder {
        return ServerViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ServerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ServerCallback : DiffUtil.ItemCallback<ServerDTO>() {
    override fun areItemsTheSame(oldItem: ServerDTO, newItem: ServerDTO): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ServerDTO, newItem: ServerDTO): Boolean {
        return oldItem == newItem
    }
}

class ServerViewHolder(private val view: TextView) : RecyclerView.ViewHolder(view) {

    fun bind(serverDTO: ServerDTO) {
        view.text = serverDTO.name
        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, serverDTO.mode.imageId, 0)
    }

    companion object {
        fun from(parent: ViewGroup): ServerViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.online_row, parent, false) as TextView
            return ServerViewHolder(view)
        }
    }
}