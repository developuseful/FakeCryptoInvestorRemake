package com.example.fakecryptoinvestorremake.di

import com.example.fakecryptoinvestorremake.domain.repository.CoinRepository
import com.example.fakecryptoinvestorremake.domain.repository.InvestmentRepository
import com.example.fakecryptoinvestorremake.domain.use_case.ProfitUpdateUseCase
import com.example.fakecryptoinvestorremake.domain.use_case.investment_use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    @Singleton
    fun provideInvestmentUseCases(
        investmentRepository: InvestmentRepository
    ): InvestmentUseCases {
        return InvestmentUseCases(
            getInvestments = GetInvestments(investmentRepository),
            getInvestment = GetInvestment(investmentRepository),
            addInvestment = AddInvestment(investmentRepository),
            deleteInvestment = DeleteInvestment(investmentRepository)
        )
    }

    @Provides
    @Singleton
    fun provideProfitUpdateUseCases(
        investmentRepository: InvestmentRepository,
        coinRepository: CoinRepository
    ): ProfitUpdateUseCase {
        return ProfitUpdateUseCase(investmentRepository, coinRepository)
    }
}