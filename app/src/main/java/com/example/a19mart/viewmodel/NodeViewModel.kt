package com.example.a19mart.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a19mart.NodeRepository
import com.example.a19mart.db.Node
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NodeViewModel(private val nodeRepository: NodeRepository, private val sharedPreferences: SharedPreferences) : ViewModel() {


    private var _state = MutableLiveData<State?>()
    val state: LiveData<State?>
        get() = _state

    fun loadData(parentId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val currentNodeId = sharedPreferences.getLong(CURRENT_NODE_ID, 1L)
            var rootNode = nodeRepository.getNodeById(parentId)
            var nodeList = nodeRepository.getChildNodes(parentId)
            _state.postValue(State(currentNodeId, rootNode, nodeList))
        }
    }

    fun createNode(parentId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val node = nodeRepository.insertNode(parentId)
            val currentNodeId = nodeRepository.getCurrentState()
            val newList = state.value!!.childNodeList + node
            val newState = State(currentNodeId, state.value?.parentNode, newList)
            _state.postValue(newState)
        }
    }

    fun deleteNode(node: Node) {
        CoroutineScope(Dispatchers.IO).launch {
            nodeRepository.deleteNode(node)
            val currentNodeId = nodeRepository.getCurrentState()
            val newList = state.value!!.childNodeList.toMutableList()
            newList.remove(node)
            val newState = State(currentNodeId,state.value?.parentNode, newList)
            _state.postValue(newState)
        }
    }

    companion object {
        private const val CURRENT_NODE_ID = "CURRENT_NODE_ID"
    }
}