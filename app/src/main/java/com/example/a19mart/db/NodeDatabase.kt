package com.example.a19mart.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Node::class], version = 6)
@TypeConverters(Converters::class)
abstract class NodeDatabase : RoomDatabase() {

    abstract fun getNodeDao(): NodeDao

    companion object {
        @Volatile
        private var instance: NodeDatabase? = null

        fun getInstance(
            application: Application
        ): NodeDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(application).also { instance = it }
            }
        }

        private fun buildDatabase(application: Application): NodeDatabase {
            return Room.databaseBuilder(
                application,
                NodeDatabase::class.java, "nodes_database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
