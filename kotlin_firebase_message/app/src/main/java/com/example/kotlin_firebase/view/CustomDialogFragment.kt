package com.example.kotlin_firebase.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.example.kotlin_firebase.R
import kotlinx.android.synthetic.main.fragment_custom_dialog.*
import kotlinx.android.synthetic.main.fragment_custom_dialog.view.*

class CustomDialogFragment: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_custom_dialog, container, false)
        view.cancelButton.setOnClickListener {
            dismiss()
        }
        view.submitButton.setOnClickListener {
           try {
               val selectedId = ratingRadioGroup.checkedRadioButtonId
               val radio = view.findViewById<RadioButton>(selectedId)
               var ratingResult = radio.text.toString()
               Toast.makeText(context, "You rated: $ratingResult", Toast.LENGTH_LONG).show()

           } catch (e: Exception) {
               Toast.makeText(context, "Please rate", Toast.LENGTH_LONG).show()
           }

        }


        return view
    }
}