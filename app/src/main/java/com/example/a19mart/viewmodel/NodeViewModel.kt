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

    fun createNode(parentId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val node = nodeRepository.insertNode(parentId)
            val newList = state.value!!.childNodeList + node
            val newState = State(state.value?.parentNode, newList)
            _state.postValue(newState)
        }
    }

    fun deleteNode(node: Node) {
        CoroutineScope(Dispatchers.IO).launch {
            nodeRepository.deleteNode(node)
            val newList = state.value!!.childNodeList.toMutableList()
            newList.remove(node)
            val newState = State(state.value?.parentNode, newList)
            _state.postValue(newState)
        }
    }
}