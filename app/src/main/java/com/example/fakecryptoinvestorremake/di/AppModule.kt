package com.example.fakecryptoinvestorremake.di

import com.example.fakecryptoinvestorremake.common.Constants
import com.example.fakecryptoinvestorremake.data.remote.MessariApi
import com.example.fakecryptoinvestorremake.data.remote.repository.CoinRepositoryImpl
import com.example.fakecryptoinvestorremake.domain.repository.CoinRepository
import com.example.fakecryptoinvestorremake.domain.use_case.investment_use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideMessariApi(): MessariApi{

        val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MessariApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinRepository(api: MessariApi) : CoinRepository{
        return CoinRepositoryImpl(api)
    }

}