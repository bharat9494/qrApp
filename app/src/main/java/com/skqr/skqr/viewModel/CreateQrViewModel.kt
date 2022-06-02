package com.skqr.skqr.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.skqr.skqr.data.models.CreateQrModel
import com.skqr.skqr.data.models.CreateQrResponse
import com.skqr.skqr.data.network.AppRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class CreateQrViewModel (application: Application) : AndroidViewModel(application) {

    var repository: AppRepo = AppRepo.getInstance(application)!!
    var typeId = ""

    var qrResponse = MutableLiveData<Response<CreateQrResponse>>()

    fun createQr(createQrModel: CreateQrModel) = viewModelScope.launch(Dispatchers.IO) {
        qrResponse.postValue(repository.createQr(createQrModel))
    }

}

class CreateQrViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateQrViewModel::class.java)) {
            return CreateQrViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}