package com.example.a19mart

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.a19mart.db.NodeDao
import com.example.a19mart.db.NodeDatabase
import com.example.a19mart.db.Node
import com.example.a19mart.viewmodel.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NodeRepository(private val nodeDao: NodeDao, private val sharedPreferences: SharedPreferences) {


    suspend fun getNodeById(id: Long): Node {
        return withContext(Dispatchers.IO) {
            nodeDao.getNodeById(id)
        }
    }

    suspend fun deleteNode(node: Node) {
        return withContext(Dispatchers.IO) {
            nodeDao.deleteNode(node)
        }
    }

    suspend fun getChildNodes(parentId: Long): List<Node> {
        return withContext(Dispatchers.IO) {
            nodeDao.getChildNodes(parentId)
        }
    }

    suspend fun insertNode(parentId: Long): Node {
        return withContext(Dispatchers.IO) {
            val node = Node(address = "address: ", children = mutableListOf(), parentId = parentId)
            val newId = nodeDao.addNode(node)
            return@withContext node.copy(id = newId)
        }
    }

    fun getCurrentState(): Long? {
        return sharedPreferences.getLong("CURRENT_STATE", -1)
    }

    fun saveCurrentState(currentState: Long) {
        sharedPreferences.edit().putLong("CURRENT_STATE", currentState).apply()
    }
}