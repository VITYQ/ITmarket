package com.andrewmdr.itmarket.presentation.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.andrewmdr.itmarket.R
import com.andrewmdr.itmarket.databinding.FragmentLoginBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.andrewmdr.itmarket.data.model.LoginCredentials


class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    lateinit var viewModel: LoginViewModel
    var admin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        //admin = arguments!!.getBoolean("is_admin")
        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.clearData()
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mbLogin.setOnClickListener {
            val login = binding.tilLogin.editText?.text.toString()
            val password = binding.tilPassword.editText?.text.toString()
            if (login.isNullOrEmpty() || password.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Нельзя оставлять поля пустыми", Toast.LENGTH_SHORT).show()
            }
            else{
                Log.d("checklogin", "$login $password")
                viewModel.login(login, password)
            }

        }

        viewModel.loginHandler.observe(viewLifecycleOwner){
            Log.d("checklogin", "token $it")
            if (!it.data?.token.isNullOrEmpty()){
                Log.d("checklogin", "logged")
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToOrdersFragmnt(false))
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.clearData()
    }


}