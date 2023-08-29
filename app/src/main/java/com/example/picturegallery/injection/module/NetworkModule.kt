package com.example.picturegallery.injection.module

import android.content.Context
import com.example.picturegallery.data.data_source.AlbumRemoteDataSource
import com.example.picturegallery.data.data_source.PhotosRemoteDataSource
import com.example.picturegallery.data.data_source.TokenDao
import com.example.picturegallery.data.interceptor.NetworkInterceptor
import com.example.picturegallery.domain.model.dispatchers.AppDispatchers
import com.example.picturegallery.injection.ForApplication
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.*
import javax.security.cert.CertificateException


@Module
class NetworkModule {

    private val BASE_URL = "https://photo.belsev.su/api/"

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    @Named("newOkHttpClient")
    fun provideNewOkHttpClient(@Named("token") token: String): OkHttpClient {
        val clientBuilder = getUnsafeOkHttpClient()
            .addInterceptor(NetworkInterceptor())
            .connectTimeout(
                20,
                TimeUnit.SECONDS
            )
            .readTimeout(
                120,
                TimeUnit.SECONDS
            )
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
                )
            }
            .protocols(listOf(Protocol.HTTP_1_1))
            .connectionPool(ConnectionPool(0, 1, TimeUnit.MINUTES))
            .retryOnConnectionFailure(false)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return clientBuilder.build()
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }

                }
            )

            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("newOkHttpClient") okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Named("token")
   fun provideToken(db: TokenDao): String =
        runBlocking(Dispatchers.IO) {
            db.getToken()?.token ?: ""
    }

    @Provides
    @Singleton
    fun provideLocalDbTest(@ForApplication context: Context): TokenDao =
        TokenDao.create(context)

    @Provides
    @Singleton
    fun provideAlbumsRemoteDataSource(retrofit: Retrofit): AlbumRemoteDataSource =
        AlbumRemoteDataSource.create(retrofit)

    @Provides
    @Singleton
    fun providePhotosRemoteDataSource(retrofit: Retrofit): PhotosRemoteDataSource =
        PhotosRemoteDataSource.create(retrofit)

    @Provides
    @Singleton
    fun providesAppDispatchers() = AppDispatchers()

}