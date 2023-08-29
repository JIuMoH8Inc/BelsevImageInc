package com.example.picturegallery.application

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.example.picturegallery.injection.AppComponent
import com.example.picturegallery.injection.DaggerAppComponent
import com.example.picturegallery.injection.module.ApplicationModule
import com.example.picturegallery.injection.module.NetworkModule
import javax.inject.Inject

class MyApp: Application() {

    lateinit var appComponent: AppComponent

    init {
        setInstanceTest(this@MyApp)
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate() {
        super.onCreate()
        initInjection()
        appComponent.inject(this)
    }

    private fun initInjection() {
        appComponent = DaggerAppComponent
            .builder()
            .applicationModule(ApplicationModule((this)))
            .networkModule(NetworkModule())
            .build()
    }

    fun getViewModelFactory(): ViewModelProvider.Factory {
        return factory
    }

    companion object {
        lateinit var instance: MyApp

        fun setInstanceTest(test: MyApp) {
            this.instance = test
        }
    }
}