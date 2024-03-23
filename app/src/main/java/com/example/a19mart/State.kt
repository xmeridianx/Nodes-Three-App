package com.example.a19mart

import com.example.a19mart.db.Node
import kotlinx.coroutines.flow.Flow

data class State(
    var parentNode: Node,
    var childNodeList: List<Node>
)
