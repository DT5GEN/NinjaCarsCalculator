package com.example.ninjacarscalculator.ui.main

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ninjacarscalculator.database.App
import com.example.ninjacarscalculator.repository.Repository


class ViewModelFactoryM(context: Activity): ViewModelProvider.Factory {

    private val repository by lazy(LazyThreadSafetyMode.NONE){
        Repository()
    }
    private val teamDao by lazy(LazyThreadSafetyMode.NONE){
        (context.application as App).db.myteamDao()
    }




    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            repository, teamDao,
        ) as T
    }
}