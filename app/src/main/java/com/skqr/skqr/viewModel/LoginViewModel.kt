package com.skqr.skqr.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.skqr.skqr.data.models.LoginModel
import com.skqr.skqr.data.models.LoginResponse
import com.skqr.skqr.data.network.AppRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel (application: Application) : AndroidViewModel(application) {

    var repository: AppRepo = AppRepo.getInstance(application)!!
    var loginResponse = MutableLiveData<Response<LoginResponse>>()

    fun validateUser(loginModel: LoginModel) = viewModelScope.launch(Dispatchers.IO) {
        val response = repository.validateUser(loginModel)
        loginResponse.postValue(response)
    }

}

class LoginViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}