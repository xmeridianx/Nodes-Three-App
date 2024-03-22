package com.example.a19mart.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a19mart.Node
import com.example.a19mart.R
import com.example.a19mart.databinding.FragmentChildNodeBinding
import com.example.a19mart.db.NodeDao
import com.example.a19mart.db.NodeDatabase
import com.example.a19mart.ui.adapters.ItemClickListener
import com.example.a19mart.ui.adapters.NodeListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChildNodeFragment : Fragment(), ItemClickListener {

    private lateinit var database: NodeDatabase
    private lateinit var nodeDao: NodeDao
    private val rootNode = Node(1, "root_address")
    private var adapter: NodeListAdapter? = null
    private val binding: FragmentChildNodeBinding
        get() = requireNotNull(_binding)
    private var _binding: FragmentChildNodeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChildNodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt("id", 1) ?: 1
        adapter = NodeListAdapter( rootNode, this)
        database = NodeDatabase.getInstance(requireActivity().application)!!
        nodeDao = database.getNodeDao()
        CoroutineScope(Dispatchers.IO).launch {
            val node = nodeDao.getNodeById(id)
            withContext(Dispatchers.Main) {
                updateUI(node)
            }
        }
        if (isAdded) {
            binding.recyclerViewChildrens.adapter = adapter
            binding.buttonAddNode.setOnClickListener {
                val newNode = Node( address = "address: ${11111}", children = mutableListOf(), parentId = id)
                insertNode(newNode)
                adapter!!.nodeList.add(newNode)
                adapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun updateUI(node: Node?) {
        if (node != null) {
            binding.textViewNodeId.text = "Id: ${node.id}"
            binding.textViewNodeAddress.text = "Address: ${node.address}"
            binding.textViewParent.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val parent = nodeDao.getNodeById(node.parentId ?: 0)
                    withContext(Dispatchers.Main) {
                        val transaction =
                            requireActivity().supportFragmentManager.beginTransaction()
                        val newNodeInfoFragment = newInstance(parent?.id ?: 1)
                        transaction.replace(R.id.container, newNodeInfoFragment)
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }
                }
            }
        }
    }

    private fun insertNode(node: Node) {
        CoroutineScope(Dispatchers.IO).launch {
            nodeDao.addNode(node)
        }
    }

    companion object {
        fun newInstance(id: Int): ChildNodeFragment {
            val fragment = ChildNodeFragment()
            val args = Bundle()
            args.putInt("id", id)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemClick(node: Node) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, newInstance(node.id))
            .addToBackStack(null)
            .commit()
    }
}