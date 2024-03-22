package com.example.a19mart.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NodeDao {
    @Query("SELECT * FROM nodes")
    fun getAll(): LiveData<List<Node>>

    @Query("SELECT * FROM nodes WHERE id = :id")
    suspend fun getNodeById(id: Int): Flow<Node>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNode(node: Node): Long

    @Delete
    suspend fun deleteNode(node: Node)

    @Query("SELECT * FROM nodes WHERE parent = :parentId")
    suspend fun getNodeByParentId(parentId: Int): Flow<List<Node>>

    @Query("SELECT * FROM nodes WHERE parent = :parentId AND id != :parentId")
    suspend fun getChildNodes(parentId: Int): Flow<List<Node>>
}