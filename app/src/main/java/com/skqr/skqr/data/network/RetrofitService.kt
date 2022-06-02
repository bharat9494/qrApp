package com.skqr.skqr.data.network

import com.google.gson.JsonObject
import com.skqr.skqr.data.models.*
import com.skqr.skqr.misc.baseUrl
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {

    @POST("some/api/here")
    suspend fun validateUser(@Body request: LoginModel): Response<LoginResponse>

    @POST("some/api/here")
    suspend fun getTypeMaster(): Response<TypeMasterResponse>

    @POST("some/api/here")
    suspend fun createQr(@Body request: CreateQrModel): Response<CreateQrResponse>

    @POST("some/api/here")
    suspend fun savePhysicalTest(@Body request: PhysicalTestModel): Response<PhysicalTestResponse>

    @POST("some/api/here")
    suspend fun saveChemicalTest(@Body request: ChemicalTestModel): Response<ChemicalTestResponse>

    @POST("some/api/here")
    suspend fun getProductDetails(
        @Query("id") id: String,
        @Query("json") json: String,
        @Query("user_id") user_id: String
    ): Response<QrCodeDetailResponse>

    @POST("some/api/here")
    suspend fun checkDataExist(@Body qrModel: QrModel): Response<QrModelResponse>
}

object RetrofitApi {
    private val okHttp = OkHttpClient.Builder()
    private val builder = Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())

    private val retrofit = builder.build()

    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }
}

interface RetrofitCallback {
    fun onData(data: Any)
    fun onFailed(ex: String?)
}