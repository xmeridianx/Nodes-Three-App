package com.example.a19mart.data.repository

import com.example.a19mart.data.dao.NodeDao
import com.example.a19mart.data.model.Node
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NodeRepository(private val nodeDao: NodeDao) {

    suspend fun getNodeById(id: Int): Node? {
        return withContext(Dispatchers.IO) {
            nodeDao.getNodeById(id)
        }
    }

    suspend fun deleteNode(node: Node) {
        return withContext(Dispatchers.IO) {
            nodeDao.deleteNode(node)
        }
    }

    suspend fun getChildNodes(parentId: Int): List<Node> {
        return withContext(Dispatchers.IO) {
            nodeDao.getChildNodes(parentId)
        }
    }

    suspend fun insertNode(parentId: Int?): Node {
        return withContext(Dispatchers.IO) {
            val node = Node(address = "address: ", children = mutableListOf(), parentId = parentId)
            val newId = nodeDao.addNode(node)
            return@withContext node.copy(id = newId)
        }
    }
}