package com.example.a19mart.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a19mart.data.repository.NodeRepository
import com.example.a19mart.data.model.Node
import com.example.a19mart.model.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NodeViewModel(private val nodeRepository: NodeRepository) : ViewModel() {

    private var _state = MutableLiveData<State?>()
    val state: LiveData<State?>
        get() = _state

    fun loadData(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            var currentNode = nodeRepository.getNodeById(id)
            if (currentNode == null) {
                currentNode = nodeRepository.insertNode(null)
            }
            var nodeList = nodeRepository.getChildNodes(id)
            _state.postValue(State(currentNode, nodeList))
        }
    }

    fun createNode(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val node = nodeRepository.insertNode(id)
            val newList = state.value!!.childNodeList + node
            val newState = State(state?.value?.currentNode!!, newList)
            _state.postValue(newState)
        }
    }

    fun deleteNode(node: Node) {
        CoroutineScope(Dispatchers.IO).launch {
            nodeRepository.deleteNode(node)
            val newList = state.value!!.childNodeList.toMutableList()
            newList.remove(node)
            val newState = State(state?.value?.currentNode!!, newList)
            _state.postValue(newState)
        }
    }
}