package com.example.a19mart.model

import com.example.a19mart.data.model.Node

data class State(
    var currentNode: Node,
    var childNodeList: List<Node> = mutableListOf()
)
