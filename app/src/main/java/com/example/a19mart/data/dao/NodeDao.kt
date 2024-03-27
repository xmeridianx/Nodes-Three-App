package com.example.a19mart.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.a19mart.data.model.Node

@Dao
interface NodeDao {
    @Query("SELECT * FROM nodes")
    fun getAll(): List<Node>

    @Query("SELECT * FROM nodes WHERE id = :id")
    suspend fun getNodeById(id: Int): Node?

    @Query("SELECT * FROM nodes WHERE parent = :parentId")
    suspend fun getNodeByParentId(parentId: Int): List<Node>

    @Query("SELECT * FROM nodes WHERE parent = :parentId AND id != :parentId")
    suspend fun getChildNodes(parentId: Int): List<Node>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNode(node: Node): Long

    @Delete
    suspend fun deleteNode(node: Node)
}