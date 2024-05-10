package com.example.trackerapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.trackerapp.databinding.ActivityLoginBinding
import com.example.trackerapp.model.User
import com.example.trackerapp.viewModel.UserAdapter
import io.realm.Realm
import io.realm.kotlin.where


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)





        binding.loginButton.setOnClickListener {

            val username = binding.etUsernameLogin.text.toString().trim()
            val password = binding.etPasswordLogin.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()){

                Toast.makeText(this,"Enter All the Details", Toast.LENGTH_SHORT).show()

            }else{

                val providedUsername = username
                val providedPassword = password

                if (checkCredentials(providedUsername, providedPassword)) {
                    // Username and password are valid

                    val intent = Intent(this, MainActivity::class.java)

                    startActivity(intent)
                    finish()
                } else {
                    // Invalid username or password
                    println("Invalid username or password!")
                    Toast.makeText(this,"Invalid username or password!",Toast.LENGTH_LONG).show()
                }

        }

            }

        binding.userSignupButton.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SignupActivity::class.java
                )
            )


        }

        binding.switchUserButton.setOnClickListener {

                     replaceFragment(UserFragment())

        }

    }

    private fun replaceFragment(fragment: Fragment) {

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id,fragment)
        transaction.addToBackStack(null) // Add to back stack so user can navigate back

        transaction.commit()


    }
}

    private fun checkCredentials(username: String, password: String): Boolean {
        val username = username
        val password = password
        val realm = Realm.getDefaultInstance()
        val user = realm.where<User>().equalTo("username", username).findFirst()
        val isValidCredentials = user != null && user.password == password
        realm.close()
        return isValidCredentials
    }
