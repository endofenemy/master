package com.app.barclaysdemo.ui.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.barclaysdemo.data.repository.StockRepository

class StockModelFactory(
    val repository: StockRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StockViewModel(repository) as T
    }
}