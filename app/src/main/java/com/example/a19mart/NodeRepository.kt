package com.example.a19mart

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.a19mart.db.NodeDao
import com.example.a19mart.db.NodeDatabase
import com.example.a19mart.db.Node
import kotlinx.coroutines.flow.Flow

class NodeRepository (application: Application) {

    private val nodeDao: NodeDao

    val allNodes: LiveData<List<Node>>

    init {
        val database = NodeDatabase.getInstance(application)
        nodeDao = database.getNodeDao()
        allNodes = nodeDao.getAll()
    }

    suspend fun getNodeById(id: Int): Flow<Node?> {
        return nodeDao.getNodeById(id)
    }

    suspend fun deleteNode(node: Node) {
        nodeDao.deleteNode(node)
    }

    suspend fun getChildNodes(parentId: Int): Flow<List<Node>>{
        return nodeDao.getChildNodes(parentId)
    }

    suspend fun insertNode(node: Node) {
        nodeDao.addNode(node)
    }
}