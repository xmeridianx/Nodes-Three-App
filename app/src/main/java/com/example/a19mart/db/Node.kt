package com.example.a19mart.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "nodes"
)
data class Node(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val address: String,
    val children: MutableList<Node> = mutableListOf(),
    @ColumnInfo(name = "parent")
    var parentId: Int? = null
)
