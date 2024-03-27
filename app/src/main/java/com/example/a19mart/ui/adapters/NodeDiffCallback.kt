package com.example.a19mart.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.a19mart.data.model.Node

class NodeDiffCallback : DiffUtil.ItemCallback<Node>() {
    override fun areItemsTheSame(oldItem: Node, newItem: Node): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Node, newItem: Node): Boolean {
        return oldItem == newItem
    }
}