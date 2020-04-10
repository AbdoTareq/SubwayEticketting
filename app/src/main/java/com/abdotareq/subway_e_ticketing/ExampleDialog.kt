package com.abdotareq.subway_e_ticketing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import com.abdotareq.subway_e_ticketing.databinding.ChangePassDialogeBinding
import timber.log.Timber

class ExampleDialog : AppCompatDialogFragment() {

    private var oldPass: EditText? = null
    private var newPass: EditText? = null
    private var confirmNewPass: EditText? = null
    private lateinit var binding: ChangePassDialogeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ChangePassDialogeBinding.inflate(inflater, container, false)
        val view = binding.root

        oldPass = binding.oldPass
        newPass = binding.newPassword
        confirmNewPass = binding.confirmNewPassword

        binding.confirmBtn.setOnClickListener {
            Timber.i("${oldPass!!.text}  new: ${newPass!!.text}   CONFIRM: ${confirmNewPass!!.text}")
            // to close dialog
            dismiss()
        }

        return view
    }


}