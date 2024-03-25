package com.example.a19mart.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.a19mart.NodeRepository
import com.example.a19mart.viewmodel.NodeViewModel
import com.example.a19mart.viewmodel.NodeViewModelFactory
import com.example.a19mart.db.Node
import com.example.a19mart.R
import com.example.a19mart.databinding.FragmentNodeBinding
import com.example.a19mart.db.NodeDao
import com.example.a19mart.db.NodeDatabase
import com.example.a19mart.ui.adapters.ItemClickListener
import com.example.a19mart.ui.adapters.NodeListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest

class NodeFragment : Fragment(), ItemClickListener {

    private lateinit var nodeViewModel: NodeViewModel
    private lateinit var nodeViewModelFactory: NodeViewModelFactory
    private lateinit var nodeDao: NodeDao
    private lateinit var database: NodeDatabase
    private var adapter: NodeListAdapter? = null
    private var parentId: Int? = null
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

        database = NodeDatabase.getInstance(requireActivity().application)
        nodeDao = database.getNodeDao()
        val nodeRepository = NodeRepository(nodeDao)
        nodeViewModelFactory = NodeViewModelFactory(nodeRepository)

        nodeViewModel = ViewModelProvider(this, nodeViewModelFactory).get(NodeViewModel::class.java)
        val id = arguments?.getInt("id", 1)

        adapter = NodeListAdapter(nodeViewModel, this@NodeFragment)
        binding.recyclerViewChildrens.adapter = adapter

        binding.buttonBack.setOnClickListener {
            navigateBack()
        }

        nodeViewModel.loadData(id!!)
        nodeViewModel.state.observe(viewLifecycleOwner) { state ->
            if (state != null) {
                parentId = state?.parentNode?.parentId
                binding.textViewNodeId.text = "Id: $id"
                val parentAddress = generateRootAddress(id)
                binding.textViewNodeAddress.text = "Address: $parentAddress"
                val childNodes = state.childNodeList
                if (childNodes != null) {
                    adapter?.submitList(childNodes)
                }
                if (id == 1) {
                    binding.buttonDelete.isVisible = false
                }
            }

            binding.buttonAddNode.setOnClickListener {
                nodeViewModel.createNode(id)
            }

            binding.buttonDelete.setOnClickListener {
                val parentNode = state?.parentNode
                if (parentNode != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        nodeRepository.deleteNode(parentNode)
                        navigateBack()
                    }
                }
            }
        }
    }

    private fun navigateBack() {
        if (parentId != null) {
            val newFragment = newInstance(parentId!!)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, newFragment)
                .addToBackStack(null)
                .commit()
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
            .replace(R.id.container, newInstance(node.id.toInt()))
            .addToBackStack(null)
            .commit()
    }

    private fun generateRootAddress(id: Int): String {
        val idBytes = id.toString().toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val hashBytes = md.digest(idBytes)
        val last20Bytes = hashBytes.takeLast(20).toByteArray()
        return last20Bytes.joinToString("") { "%02x".format(it) }
    }
}