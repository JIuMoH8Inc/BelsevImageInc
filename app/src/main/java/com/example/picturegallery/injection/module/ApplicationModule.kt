package com.example.picturegallery.injection.module

import android.app.Application
import android.content.Context
import com.example.picturegallery.injection.ForApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule (private val application: Application) {

    @Provides
    @Singleton
    @ForApplication
    fun providesApplicationContext(): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return application
    }

}