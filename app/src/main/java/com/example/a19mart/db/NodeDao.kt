package com.example.a19mart.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.a19mart.Node

@Dao
interface NodeDao {
    @Query("SELECT * FROM nodes")
    fun getAll(): LiveData<List<Node>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNode(node: Node): Long

    @Delete
    suspend fun deleteNode(node: Node)

    @Query("SELECT * FROM nodes WHERE parent = :parentId")
    suspend fun getNodeByParentId(parentId: Int): Node?

    @Query("SELECT * FROM nodes WHERE id = :id")
    suspend fun getNodeById(id: Int): Node?
}