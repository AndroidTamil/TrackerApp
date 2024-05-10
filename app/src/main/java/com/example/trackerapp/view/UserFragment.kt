package com.example.trackerapp.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackerapp.R
import com.example.trackerapp.model.User
import com.example.trackerapp.viewModel.UserAdapter
import com.example.trackerapp.viewModel.UserViewModel

class UserFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.userListRecyclerView)
        userAdapter = UserAdapter(emptyList()) { clickedUser ->
            navigateToLoginPage(clickedUser)
        }

        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.getUserList().observe(viewLifecycleOwner, Observer { userList ->
            userAdapter.userList = userList
            userAdapter.notifyDataSetChanged()
        })

        return view
    }

    private fun navigateToLoginPage(clickedUser: User) {

        val bundle = Bundle().apply {
            // Pass user data if needed
            putString("username", clickedUser.username)
        }

        // Navigate to the login page
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
        activity?.finish()
    }
}
