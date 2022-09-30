package com.example.rentapp.ui.dialogs

import android.app.ActionBar.LayoutParams
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.rentapp.databinding.DialogAddUsernameBinding
import com.example.rentapp.view_models.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class AddUsernameDialog(private val account: GoogleSignInAccount?) : DialogFragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: DialogAddUsernameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = DialogAddUsernameBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(AuthViewModel::class.java)

    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(LayoutParams.MATCH_PARENT, 1000)
    }


}