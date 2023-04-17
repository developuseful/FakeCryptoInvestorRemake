package com.example.fakecryptoinvestorremake.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.fakecryptoinvestorremake.data.internal.database.FCIRDatabase
import com.example.fakecryptoinvestorremake.data.internal.database.dao.InvestmentDao
import com.example.fakecryptoinvestorremake.data.internal.repository.InvestmentRepositoryImp
import com.example.fakecryptoinvestorremake.domain.repository.InvestmentRepository
import com.example.fakecryptoinvestorremake.domain.use_case.investment_use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun providesFCIRDatabase(app: Application): FCIRDatabase {
        return Room.databaseBuilder(app, FCIRDatabase::class.java, FCIRDatabase.DATABASE_NAME)
            //.fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideInvestmentRepository(db: FCIRDatabase) : InvestmentRepository {
        return InvestmentRepositoryImp(db.investmentDao)
    }

    @Provides
    @Singleton
    fun provideInvestmentUseCases(repository: InvestmentRepository): InvestmentUseCases {
        return InvestmentUseCases(
            getInvestments = GetInvestments(repository),
            getInvestment = GetInvestment(repository),
            addInvestment = AddInvestment(repository),
            deleteInvestment = DeleteInvestment(repository)
        )
    }
}