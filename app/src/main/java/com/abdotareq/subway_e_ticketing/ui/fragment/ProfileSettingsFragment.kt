package com.abdotareq.subway_e_ticketing.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.abdotareq.subway_e_ticketing.ExampleDialog
import com.abdotareq.subway_e_ticketing.databinding.FragmentProfileSettingsBinding
import com.abdotareq.subway_e_ticketing.model.dto.User
import com.abdotareq.subway_e_ticketing.utility.imageUtil.BitmapConverter
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class ProfileSettingsFragment : Fragment() {

    private var _binding: FragmentProfileSettingsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var user: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        showData()

        binding.changePassBtn.setOnClickListener {
            openDialog()
        }

        return view
    }

    // set profile fields
    private fun showData() {
        // receive user obj from splash screen
        try {
            user = activity?.intent?.getSerializableExtra("user") as User
            user.let { // means if not null or empty
                Timber.e("$user")
            }
        } catch (e: Exception) {
            Timber.e("$e")
        }

        try {
            binding.firstNameEt.setText(user?.first_name)
            binding.lastNameEt.setText(user?.last_name)
            binding.mail.setText(user?.email)

            //if image exists
            if (user!!.image != null) {
                val bitMapCon = BitmapConverter(BitmapConverter.AsyncResponse {
                    binding.profileImage.setImageBitmap(it)
                })
                bitMapCon.execute(user?.image)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // this for change pass dialog
    private fun openDialog() {
        val exampleDialog = ExampleDialog()
        fragmentManager?.let { exampleDialog.show(it,"example") }
    }


}
