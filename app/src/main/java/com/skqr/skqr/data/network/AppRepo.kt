package com.skqr.skqr.data.network

import android.content.Context
import com.google.gson.JsonObject
import com.skqr.skqr.data.models.*
import retrofit2.Response

class AppRepo {

    companion object {

        var apiService = RetrofitApi.buildService(RetrofitService::class.java)
        private var instance: AppRepo? = null

        fun getInstance(context: Context): AppRepo? {
            if (instance == null) {
                instance = AppRepo()
            }
            return instance
        }

    }

    suspend fun validateUser(loginModel: LoginModel): Response<LoginResponse> {
        return apiService.validateUser(loginModel)
    }

    suspend fun getTypeMaster(): Response<TypeMasterResponse>  {
        return apiService.getTypeMaster()
    }

    suspend fun createQr(createQrModel: CreateQrModel): Response<CreateQrResponse> {
        return apiService.createQr(createQrModel)
    }

    suspend fun savePhysicalTest(physicalTestModel: PhysicalTestModel): Response<PhysicalTestResponse> {
        return apiService.savePhysicalTest(physicalTestModel)
    }

    suspend fun saveChemicalTest(chemicalTestModel: ChemicalTestModel): Response<ChemicalTestResponse> {
        return apiService.saveChemicalTest(chemicalTestModel)
    }

    suspend fun getProductDetails(id: String, json: String, userId: String): Response<QrCodeDetailResponse> {
        return apiService.getProductDetails(id, json, userId)
    }

    suspend fun checkDataExist(qrModel: QrModel): Response<QrModelResponse> {
        return apiService.checkDataExist(qrModel)
    }

}