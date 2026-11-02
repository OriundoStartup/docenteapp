package com.example.docenteapp

import android.app.Application
import com.example.docenteapp.data.local.database.initDatabaseBuilder
import com.example.docenteapp.di.initKoin
import org.koin.android.ext.koin.androidContext

class DocenteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Inicializar el contexto para el Database Builder de Room antes de iniciar Koin
        initDatabaseBuilder(this)
        
        initKoin {
            androidContext(this@DocenteApplication)
        }
    }
}
