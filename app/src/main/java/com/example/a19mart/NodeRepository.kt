package com.example.a19mart

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.a19mart.db.NodeDao
import com.example.a19mart.db.NodeDatabase
import com.example.a19mart.db.Node
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NodeRepository (application: Application) {

    private val nodeDao: NodeDao

    init {
        val database = NodeDatabase.getInstance(application)
        nodeDao = database.getNodeDao()
    }

    fun loadNodes(parentId: Int): List<Node>{
        return nodeDao.getAll()
    }

    suspend fun getNodeById(id: Int): Node {
        return withContext(Dispatchers.IO) {
            nodeDao.getNodeById(id)
        }
    }

    suspend fun deleteNode(node: Node) {
      //  nodeDao.deleteNode(node)
    }

    suspend fun getChildNodes(parentId: Int): List<Node>{
        return withContext(Dispatchers.IO) {
            nodeDao.getChildNodes(parentId)
        }
    }

    suspend fun insertNode(node: Node) {
       // nodeDao.addNode(node)
    }
}