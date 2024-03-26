package com.example.a19mart.viewmodel

import com.example.a19mart.db.Node
import kotlinx.coroutines.flow.Flow

data class State(
    val currentNodeId: Long? = null,
    var parentNode: Node? = null,
    var childNodeList: List<Node> = mutableListOf()
)
