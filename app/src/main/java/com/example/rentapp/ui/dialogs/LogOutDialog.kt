package com.example.rentapp.ui.dialogs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.rentapp.databinding.LogOutDialogViewBinding
import com.example.rentapp.ui.activies.MainActivity
import com.example.rentapp.uitls.UserDataStore
import kotlinx.coroutines.launch

class LogOutDialog : DialogFragment() {

    private lateinit var binding: LogOutDialogViewBinding
    private lateinit var dataStore: UserDataStore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LogOutDialogViewBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStore = UserDataStore(requireContext())
        binding.cancelLogout.setOnClickListener{
            dismiss()
        }

        binding.Logout.setOnClickListener {
            lifecycleScope.launch {
                dataStore.delete()
                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                requireActivity().finish()
                dismiss()
            }
        }

    }

}