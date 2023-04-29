package com.example.fakecryptoinvestorremake.di

import com.example.fakecryptoinvestorremake.data.internal.database.FCIRDatabase
import com.example.fakecryptoinvestorremake.data.internal.repository.InvestmentRepositoryImp
import com.example.fakecryptoinvestorremake.data.remote.MessariApi
import com.example.fakecryptoinvestorremake.data.remote.repository.CoinRepositoryImpl
import com.example.fakecryptoinvestorremake.domain.repository.CoinRepository
import com.example.fakecryptoinvestorremake.domain.repository.InvestmentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    //** Remote **//
    @Provides
    @Singleton
    fun provideCoinRepository(api: MessariApi) : CoinRepository {
        return CoinRepositoryImpl(api)
    }


    //** Internal **//
    @Provides
    @Singleton
    fun provideInvestmentRepository(db: FCIRDatabase): InvestmentRepository {
        return InvestmentRepositoryImp(db.investmentDao)
    }
}