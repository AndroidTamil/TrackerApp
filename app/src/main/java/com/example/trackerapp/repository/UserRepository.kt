package com.example.trackerapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trackerapp.model.User
import io.realm.Realm

class UserRepository {
    private val realm = Realm.getDefaultInstance()

    fun getUserList(): LiveData<List<User>> {
        val userListLiveData = MutableLiveData<List<User>>()
        userListLiveData.value = realm.where(User::class.java).findAll()
        return userListLiveData
    }
}
