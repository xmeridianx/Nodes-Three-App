package com.example.a19mart.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.a19mart.viewmodel.NodeViewModel
import com.example.a19mart.data.model.Node
import com.example.a19mart.R
import java.security.MessageDigest

class NodeListAdapter(
    private val nodeViewModel: NodeViewModel,
    private val onItemClickListener: ItemClickListener
) : ListAdapter<Node, NodeListAdapter.NodeViewHolder>(NodeDiffCallback()) {

    class NodeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewNodeId: TextView = view.findViewById(R.id.textViewNodeId)
        val textViewNodeAddress: TextView = view.findViewById(R.id.textViewNodeAddress)
        val buttonDelete: ImageButton = view.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_node, parent, false)
        return NodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: NodeViewHolder, position: Int) {
        val node = getItem(position)
        holder.textViewNodeId.text = "Id: ${node.id.toString()}"
        holder.textViewNodeAddress.text = "Address: ${generateAddress(node)}"
        holder.buttonDelete.setOnClickListener {
            nodeViewModel.deleteNode(node)
        }
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(node)
        }
    }

    private fun generateAddress(node: Node): String {
        val idBytes = node.id.toString().toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val hashBytes = md.digest(idBytes)
        val last20Bytes = hashBytes.takeLast(20).toByteArray()
        return last20Bytes.joinToString("") { "%02x".format(it) }
    }
}