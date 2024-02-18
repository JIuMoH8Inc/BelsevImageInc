package com.example.picturegallery.injection.module

import com.example.picturegallery.data.data_source.UserRemoteDataSource
import com.example.picturegallery.data.interceptor.LoginInterceptor
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
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
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException

@Module
class LoginModule {

    private val BASE_URL = "https://photo.belsev.su/api/"

    @Provides
    @Singleton
    @Named("LoginOkHttpClient")
    fun provideNewOkHttpClient(): OkHttpClient {
        val clientBuilder = getUnsafeOkHttpClient()
            .connectTimeout(
                20,
                TimeUnit.SECONDS
            )
            .readTimeout(
                120,
                TimeUnit.SECONDS
            )
            .protocols(listOf(Protocol.HTTP_1_1))
            .connectionPool(ConnectionPool(0, 1, TimeUnit.MINUTES))
            .retryOnConnectionFailure(false)
            .addInterceptor(LoginInterceptor())
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
            val sslSocketFactory: SSLSocketFactory = sslContext.getSocketFactory()
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { hostname, session -> true }
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    @Provides
    @Singleton
    @Named("LoginRetrofit")
    fun provideLoginRetrofit(
        @Named("LoginOkHttpClient") okHttpClient: OkHttpClient?,
        gson: Gson?
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
    fun provideUserRemoteDataSource(@Named("LoginRetrofit")retrofit: Retrofit): UserRemoteDataSource =
        UserRemoteDataSource.create(retrofit)

}