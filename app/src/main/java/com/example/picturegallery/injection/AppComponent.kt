package com.example.picturegallery.injection

import com.example.picturegallery.application.MyApp
import com.example.picturegallery.injection.module.ApplicationModule
import com.example.picturegallery.injection.module.LoginModule
import com.example.picturegallery.injection.module.ManagerModule
import com.example.picturegallery.injection.module.NetworkModule
import com.example.picturegallery.injection.module.RepositoryModule
import com.example.picturegallery.injection.module.UseCaseModule
import com.example.picturegallery.injection.module.ViewModelModule
import com.example.picturegallery.ui.fragment.base.BaseFlowFragment
import com.example.picturegallery.ui.fragment.base.BaseViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ NetworkModule::class, ViewModelModule::class, ApplicationModule::class, LoginModule::class, RepositoryModule::class, ManagerModule::class, UseCaseModule::class])
interface AppComponent {

    fun inject(application: MyApp)
    fun inject(baseViewModel: BaseViewModel)
    fun inject(baseFlowFragment: BaseFlowFragment)
}