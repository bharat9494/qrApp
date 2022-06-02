package com.skqr.skqr.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.google.gson.JsonObject
import com.skqr.skqr.data.models.LoginModel
import com.skqr.skqr.data.models.QrCodeDetailResponse
import com.skqr.skqr.data.network.AppRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class ViewProductDetailsViewModel (application: Application) : AndroidViewModel(application) {

    var repository: AppRepo = AppRepo.getInstance(application)!!
    var productDetailsObj = MutableLiveData<Response<QrCodeDetailResponse>>()

    fun getProductDetails(id: String, json: String, userId: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = repository.getProductDetails(id,json,userId)
        productDetailsObj.postValue(response)
    }

}

class ViewProductDetailsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewProductDetailsViewModel::class.java)) {
            return ViewProductDetailsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}