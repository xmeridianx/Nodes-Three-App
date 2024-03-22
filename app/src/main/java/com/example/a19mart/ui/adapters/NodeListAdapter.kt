package com.example.a19mart.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.a19mart.Node
import com.example.a19mart.R
import java.security.MessageDigest

class NodeListAdapter(
    private val node: Node,
    private val onItemClickListener: ItemClickListener
): RecyclerView.Adapter<NodeListAdapter.NodeViewHolder>() {

    val nodeList: MutableList<Node> = mutableListOf()

    class NodeViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textViewNodeId: TextView = view.findViewById(R.id.textViewNodeId)
        val textViewNodeAddress: TextView = view.findViewById(R.id.textViewNodeAddress)
        val textViewNodeParent: TextView = view.findViewById(R.id.textViewParent)
        val buttonDelete: ImageButton = view.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_node, parent, false)
        return NodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: NodeViewHolder, position: Int) {
        val node = nodeList[position]
        holder.textViewNodeId.text = "Id: ${node.id.toString()}"
        holder.textViewNodeAddress.text = "Address: ${generateAddress(node)}"
        holder.textViewNodeParent.isVisible = false
        //holder.textViewNodeParent.text = "Parent: ${node.parentId.toString()}"
        holder.buttonDelete.setOnClickListener {
            removeItem(position)
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

    override fun getItemCount(): Int {
        return nodeList.size
    }

    private fun removeItem(position: Int) {
        nodeList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }
}