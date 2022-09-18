package com.example.rentapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.rentapp.databinding.CarRentFinishDialogLayoutBinding

class CarRentFinishDialog(
    private var rentId: Long
) : DialogFragment() {
    private lateinit var binding: CarRentFinishDialogLayoutBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CarRentFinishDialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rentId.text = "RentId: $rentId"
        binding.Ok.setOnClickListener {
            requireActivity().onBackPressed()
              dismiss()
        }


    }


}