package com.skqr.skqr.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.skqr.skqr.data.models.TypeMasterModel
import com.skqr.skqr.data.models.TypeMasterResponse
import com.skqr.skqr.data.network.AppRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class ShowTypeMasterViewModel(application: Application) : AndroidViewModel(application) {

    var repository: AppRepo = AppRepo.getInstance(application)!!
    val listOfTypes = MutableLiveData<Response<TypeMasterResponse>>()

    init {
        getTypeMasterLink()
    }

    fun getTypeMasterLink() = viewModelScope.launch(Dispatchers.IO) {
        listOfTypes.postValue(repository.getTypeMaster())
    }

}

class ShowTypeMasterViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowTypeMasterViewModel::class.java)) {
            return ShowTypeMasterViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}