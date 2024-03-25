package com.example.a19mart.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a19mart.NodeRepository
import com.example.a19mart.db.Node
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NodeViewModel(private val nodeRepository: NodeRepository) : ViewModel() {


    private var _state = MutableLiveData<State?>()
    val state: LiveData<State?>
        get() = _state

    fun loadData(parentId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            var rootNode = nodeRepository.getNodeById(parentId)
            var nodeList = nodeRepository.getChildNodes(parentId)
            _state.postValue(State(rootNode, nodeList))
        }
    }

    fun getParentNode(): Node? {
        return _state.value?.parentNode
    }

    fun createNode(parentId: Int): LiveData<Node> {
        val newNodeLiveData = MutableLiveData<Node>()
        CoroutineScope(Dispatchers.IO).launch {
            val oldState = _state.value
            _state.postValue(State(oldState!!.parentNode, oldState.childNodeList))
            val node = nodeRepository.insertNode(parentId)
            newNodeLiveData.postValue(node)
        }
        return newNodeLiveData
    }

    fun deleteNode(node: Node) {
        CoroutineScope(Dispatchers.IO).launch {
            nodeRepository.deleteNode(node)
        }
    }
}