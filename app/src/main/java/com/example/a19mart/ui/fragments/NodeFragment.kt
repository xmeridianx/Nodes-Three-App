package com.example.a19mart.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.a19mart.NodeViewModel
import com.example.a19mart.db.Node
import com.example.a19mart.R
import com.example.a19mart.databinding.FragmentNodeBinding
import com.example.a19mart.db.NodeDatabase
import com.example.a19mart.ui.adapters.ItemClickListener
import com.example.a19mart.ui.adapters.NodeListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class NodeFragment : Fragment(), ItemClickListener {

    private lateinit var nodeViewModel: NodeViewModel
    //private lateinit var database: NodeDatabase
    //private lateinit var nodeDao: NodeDao
    //private val rootNode = Node(1, "root_address")
    private var adapter: NodeListAdapter? = null
    private val binding: FragmentNodeBinding
        get() = requireNotNull(_binding)
    private var _binding: FragmentNodeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNodeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nodeViewModel = ViewModelProvider(this).get(NodeViewModel::class.java)
        val id = arguments?.getInt("id", 1) ?: 1
        val node = nodeViewModel.getNodeById(id)

        nodeViewModel.allNodes.observe(viewLifecycleOwner){

            adapter = NodeListAdapter(nodeViewModel, this@NodeFragment)
            binding.recyclerViewChildrens.adapter = adapter
            loadChildNodes(id)


            binding.buttonAddNode.setOnClickListener {
                val newNode =
                    Node(address = "address: ${11111}", children = mutableListOf(), parentId = id)
                insertNode(newNode)
                val updatedList = adapter?.currentList?.toMutableList() ?: mutableListOf()
                updatedList.add(newNode)
                adapter?.submitList(updatedList)
            }
        }
    }

/*
    private fun updateUIRootNode(node: Node?) {
        if (node != null) {
            if (node.id == 1){
                binding.buttonDelete.isVisible = false
                binding.textViewParent.isVisible = false
            }
            binding.textViewNodeId.text = "Id: ${node.id}"
            binding.textViewNodeAddress.text = "Address: ${generateAddress(node)}"
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

 */

    private fun loadChildNodes(parentId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val childNodes = nodeViewModel.getChildNodes(parentId)
            withContext(Dispatchers.Main) {
                adapter?.submitList(childNodes)
            }
        }
    }


    private fun insertNode(node: Node) {
        CoroutineScope(Dispatchers.IO).launch {
            val nodeId = nodeViewModel.insertNode(node)
            node.id = nodeId
        }
    }

    companion object {
        fun newInstance(id: Int): NodeFragment {
            val fragment = NodeFragment()
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

    private fun generateAddress(node: Node): String {
        val idBytes = node.id.toString().toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val hashBytes = md.digest(idBytes)
        val last20Bytes = hashBytes.takeLast(20).toByteArray()
        return last20Bytes.joinToString("") { "%02x".format(it) }
    }
}