package com.example.a19mart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a19mart.db.Node
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class NodeViewModel(application: Application) : ViewModel() {

    private val nodeRepository: NodeRepository = NodeRepository(application)
    private val _nodeLiveData = MutableLiveData<Node>()
    val nodeLiveData: LiveData<Node>
        get() = _nodeLiveData

    val allNodes: LiveData<List<Node>> = nodeRepository.allNodes

    fun deleteNode(node: Node) {
        CoroutineScope(Dispatchers.IO).launch {
            nodeRepository.deleteNode(node)
        }
    }

    fun getNodeById(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            nodeRepository.getNodeById(id)
        }
    }

    fun getChildNodes(parentId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            nodeRepository.getChildNodes(parentId)
        }
    }

    fun insertNode(node: Node) {
        CoroutineScope(Dispatchers.IO).launch {
            nodeRepository.insertNode(node)
        }
    }
}