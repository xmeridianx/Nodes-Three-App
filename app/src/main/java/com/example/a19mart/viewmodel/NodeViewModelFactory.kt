package com.example.a19mart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.a19mart.data.repository.NodeRepository

class NodeViewModelFactory(private val nodeRepository: NodeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NodeViewModel(nodeRepository) as T
    }
}