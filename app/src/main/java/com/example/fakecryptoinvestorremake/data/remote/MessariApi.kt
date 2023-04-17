package com.example.fakecryptoinvestorremake.data.remote

import com.example.fakecryptoinvestorremake.data.remote.dto.Result
import retrofit2.http.GET
import retrofit2.http.Query

interface MessariApi {


    @GET("/api/v1/assets")
    suspend fun getCoins(
        @Query("fields") fields : String
    ) : Result
}