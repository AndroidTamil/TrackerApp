package com.example.trackerapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.trackerapp.model.User
import com.example.trackerapp.databinding.ActivitySignupBinding
import io.realm.Realm

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupSaveButton.setOnClickListener {

            val username = binding.etEmailSignup.text.toString().trim()
            val password = binding.etPasswordSignup.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()){

                Toast.makeText(this,"Enter All the Details",Toast.LENGTH_SHORT).show()

            }else{
                  registerUser(username,password)
            }
        }
    }

    private fun registerUser(username: String, password: String) {
        Realm
            .getDefaultInstance()
            .executeTransaction{realm ->
                val userObject = realm.createObject(User::class.java)
                userObject.username=username
                userObject.password=password
            }
        Toast.makeText(this,"Registered Successfully",Toast.LENGTH_SHORT).show()

    }
}