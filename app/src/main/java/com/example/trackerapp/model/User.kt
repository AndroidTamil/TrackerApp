package com.example.trackerapp.model

import io.realm.RealmObject

open class User:RealmObject() {

    var username: String=""
    var password: String=""

}