package com.example.trackerapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.trackerapp.model.User
import com.example.trackerapp.repository.UserRepository

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()

    fun getUserList(): LiveData<List<User>> {
        return userRepository.getUserList()
    }
}
