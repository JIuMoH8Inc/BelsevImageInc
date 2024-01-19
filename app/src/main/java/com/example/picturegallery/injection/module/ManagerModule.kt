package com.example.picturegallery.injection.module

import com.example.picturegallery.data.manager.FileManagerImpl
import com.example.picturegallery.data.manager.ResourceManagerImpl
import com.example.picturegallery.domain.manager.FileManager
import com.example.picturegallery.domain.manager.ResourceManager
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ManagerModule {
    @Binds
    @Singleton
    abstract fun bindResourceManager(resourceManager: ResourceManagerImpl): ResourceManager

    @Binds
    @Singleton
    abstract fun bindFileManager(fileManagerImpl: FileManagerImpl): FileManager
}