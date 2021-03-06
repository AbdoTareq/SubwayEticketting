package com.abdotareq.subway_e_ticketing.ui.fragment.settings

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.FragmentProfileSettingsBinding
import com.abdotareq.subway_e_ticketing.data.model.User
import com.abdotareq.subway_e_ticketing.data.model.UserInterface
import com.abdotareq.subway_e_ticketing.ui.activity.RegisterActivity
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.utility.Util
import com.abdotareq.subway_e_ticketing.utility.imageUtil.BitmapConverter
import com.abdotareq.subway_e_ticketing.utility.imageUtil.ImageUtil
import com.abdotareq.subway_e_ticketing.viewmodels.factories.ViewModelFactory
import com.abdotareq.subway_e_ticketing.viewmodels.register.ProfileViewModel
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 *  [ProfileSettingsFragment] responsible for user profile settings & changes.
 */
class ProfileSettingsFragment : Fragment() {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ProfileViewModel

    private var _binding: FragmentProfileSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var user = User()

    private var mYear = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(activity).application

        viewModelFactory = ViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)

        binding.viewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        viewModel.eventChangePass.observe(viewLifecycleOwner, Observer {
            if (it) {
                openChangePassDialog()
                viewModel.onChangePassComplete()
            }
        })

        viewModel.eventLogout.observe(viewLifecycleOwner, Observer {
            if (it) {
                confirmLogOut()
                viewModel.onLogoutComplete()
            }
        })

        viewModel.eventSave.observe(viewLifecycleOwner, Observer {
            if (it) {
                saveBtnClk()
                viewModel.onSaveComplete()
            }
        })

        viewModel.eventBirth.observe(viewLifecycleOwner, Observer {
            if (it) {
                birthDateDialog()
                viewModel.onBirthComplete()
            }
        })

        viewModel.eventGender.observe(viewLifecycleOwner, Observer {
            if (it) {
                genderDialogBuilder()
                viewModel.onGenderComplete()
            }
        })

        viewModel.eventChangePhoto.observe(viewLifecycleOwner, Observer {
            if (it) {
                pickFromGallery()
                viewModel.onChangePhotoComplete()
            }
        })

        return view
    }

    private fun birthDateDialog() {
        val calendar = Calendar.getInstance()
        // Get Current Date
        val datePickerDialog = DatePickerDialog(requireContext(),
                OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    calendar[Calendar.YEAR] = year
                    calendar[Calendar.MONTH] = monthOfYear
                    calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val currentDateString: String = dateFormat.format(calendar.time)
                    binding.calender.text = currentDateString

                }, mYear, mMonth, mDay)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        // this value = 100 years converted from
        // https://codechi.com/dev-tools/date-to-millisecond-calculators/
        datePickerDialog.datePicker.minDate = calendar.timeInMillis.minus(3153600000000)
        datePickerDialog.show()
    }

    private fun genderDialogBuilder() {
        val genderList = resources.getStringArray(R.array.genderList)

        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.select_gender))
        builder.setItems(genderList) { _, position ->
            binding.genderBtn.text = genderList[position]
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun updateUser() {
        var bearerToken = "Bearer "
        bearerToken += SharedPreferenceUtil.getSharedPrefsTokenId(context)

        Timber.e("bearerToken $bearerToken")

        val progressDialog = Util.initProgress(context, getString(R.string.progMessage))
        progressDialog.show()

        val profileInterface = object : UserInterface {
            override fun onSuccess() {
                Toast.makeText(context, getString(R.string.data_saved), Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
                SharedPreferenceUtil.setSharedPrefsName(context, "${user.first_name} ${user.last_name} ")
            }

            override fun onFail(responseCode: String) {
                progressDialog.dismiss()
                Toast.makeText(context, viewModel.getErrorMess(responseCode), Toast.LENGTH_LONG).show()
            }
        }
        viewModel.saveUserCall(bearerToken, user.image, viewModel.user.value!!.id, profileInterface)
    }

    private fun saveBtnClk() {
        //check for all inputs from user are correct
        if (viewModel.user.value?.first_name.isNullOrEmpty()) {
            binding.firstNameEt.error = getString(R.string.first_name_warrning)
            binding.firstNameEt.requestFocus()
            return
        } else if (viewModel.user.value?.last_name.isNullOrEmpty()) {
            binding.lastNameEt.error = getString(R.string.last_name)
            binding.lastNameEt.requestFocus()
            return
        }

        updateUser()
    }

    private fun openChangePassDialog() {
        val passDialog = ChangePassDialogFragment(binding.mail.text.toString())
        requireActivity().supportFragmentManager
                .let { passDialog.show(it, "Pass Dialog") }
    }

    private fun confirmLogOut() {
        //create a dialog interface to notify user that he is going to log out
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    //reset the saved user data from the shared preferences
                    SharedPreferenceUtil.setSharedPrefsLoggedIn(context, false)
                    SharedPreferenceUtil.setSharedPrefsTokenId(context, "-1")
                    //start the sign in activity
                    val intent = Intent(context, RegisterActivity::class.java)
                    startActivity(intent)
                    requireActivity().finishAffinity()
                }
            }
        }
        //Create AlertDialog Builder and the AlertDialog with the desired message
        val builder = context?.let { AlertDialog.Builder(it) }
        val dialog = builder!!.setMessage(getString(R.string.do_u_want_logout))
                .setPositiveButton(getText(R.string.ok), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).create()

        //change the color of the buttons
        dialog.setOnShowListener { //set the negative button with the red color
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(requireContext(), android.R.color.holo_red_dark))
            //set the positive button with the primary color
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(requireContext(), R.color.primaryColor))
        }
        //show the dialog
        dialog.show()
    }

    /**
     * A method called to start picking image from the gallery
     */
    private fun pickFromGallery() {
        if (Build.VERSION.SDK_INT <= 19) {
            val i = Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT
            i.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(i, 10)
        } else if (Build.VERSION.SDK_INT > 19) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 10)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            val dialog = Util.initProgress(context, getString(R.string.prog_image_message))
            dialog.show()

            if (requestCode == 10) {
                try {
                    val selectedImageUri: Uri? = data!!.data
//                    val selectedImagePath: String? = getRealPathFromURI(selectedImageUri)
                    binding.profileImage.setImageURI(selectedImageUri)

                    convertImageToString(selectedImageUri)

                    if (user.image != null) {
                        val bitMapCon = BitmapConverter(BitmapConverter.AsyncResponse {
                            binding.profileImage.setImageBitmap(it)
                        })
                        bitMapCon.execute(user.image)
                    }

                } catch (e: Exception) {
                    Timber.e("error happened    $e")
                }
                dialog.dismiss()
            } else {
                //notify user that a problem occurred
                Toast.makeText(context
                        , getString(R.string.some_error), Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
        }
    }

    /**
     * convert Image To String to store it in user image and send it to server
     * */
    private fun convertImageToString(selectedImageUri: Uri?) {
        // how to store image in string format
        val cropImage = ImageUtil.resizeImage(requireContext(), selectedImageUri)
        val imageString = Base64.encodeToString(cropImage, 4)
        user.image = imageString
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
