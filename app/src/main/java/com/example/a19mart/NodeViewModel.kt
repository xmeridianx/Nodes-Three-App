package com.example.a19mart

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a19mart.db.Node
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NodeViewModel(application: Application) : ViewModel() {

    private val nodeRepository: NodeRepository = NodeRepository(application)

    private var _state = MutableStateFlow<State?>(null)
    val state: StateFlow<State?> = _state

    private val _stateLiveData = MutableLiveData<State?>()
    val stateLiveData: LiveData<State?>
        get() = _stateLiveData

    fun deleteNode(node: Node) {
        CoroutineScope(Dispatchers.IO).launch {
            nodeRepository.deleteNode(node)
        }
    }

   fun loadData(parentId: Int){
       CoroutineScope(Dispatchers.IO).launch {
           var rootNode = nodeRepository.getNodeById(parentId)
           var nodeList = nodeRepository.getChildNodes(parentId)
           _state.value = State(rootNode, nodeList)
       }
   }

    fun getParentNode(): Node? {
        return _state.value?.parentNode
    }

    /*
    fun createNode(node: Node) {
        CoroutineScope(Dispatchers.IO).launch {
            val newNode = nodeRepository.insertNode(node)
            val oldState = state.value
            state.value = State(oldState!!.parentNode, oldState!!.childNodeList)
        }
    }

     */
}