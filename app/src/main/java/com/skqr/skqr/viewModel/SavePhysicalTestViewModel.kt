package com.skqr.skqr.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.skqr.skqr.data.models.PhysicalTestModel
import com.skqr.skqr.data.models.PhysicalTestResponse
import com.skqr.skqr.data.models.QrModel
import com.skqr.skqr.data.models.QrModelResponse
import com.skqr.skqr.data.network.AppRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class SavePhysicalTestViewModel(application: Application) : AndroidViewModel(application) {

    var repository: AppRepo = AppRepo.getInstance(application)!!
    var saveResponse = MutableLiveData<Response<PhysicalTestResponse>>()
    val qrModelResponse = MutableLiveData<Response<QrModelResponse>>()

    fun savePhysicalTest(physicalTestModel: PhysicalTestModel) = viewModelScope.launch(Dispatchers.IO) {
        val response = repository.savePhysicalTest(physicalTestModel)
        saveResponse.postValue(response)
    }

    fun checkIfDataExist(qrModel: QrModel) = viewModelScope.launch(Dispatchers.IO) {
        val response = repository.checkDataExist(qrModel)
        qrModelResponse.postValue(response)
    }

}

class SavePhysicalTestViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavePhysicalTestViewModel::class.java)) {
            return SavePhysicalTestViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}