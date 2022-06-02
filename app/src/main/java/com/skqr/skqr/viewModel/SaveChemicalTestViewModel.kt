package com.skqr.skqr.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.skqr.skqr.data.models.*
import com.skqr.skqr.data.network.AppRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class SaveChemicalTestViewModel (application: Application) : AndroidViewModel(application) {

    var repository: AppRepo = AppRepo.getInstance(application)!!
    var saveResponse = MutableLiveData<Response<ChemicalTestResponse>>()
    val qrModelResponse = MutableLiveData<Response<QrModelResponse>>()

    fun saveChemicalTest(chemicalTestModel: ChemicalTestModel) = viewModelScope.launch(Dispatchers.IO) {
        val response = repository.saveChemicalTest(chemicalTestModel)
        saveResponse.postValue(response)
    }

    fun checkIfDataExist(qrModel: QrModel) = viewModelScope.launch(Dispatchers.IO) {
        val response = repository.checkDataExist(qrModel)
        qrModelResponse.postValue(response)
    }

}

class SaveChemicalTestViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SaveChemicalTestViewModel::class.java)) {
            return SaveChemicalTestViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}