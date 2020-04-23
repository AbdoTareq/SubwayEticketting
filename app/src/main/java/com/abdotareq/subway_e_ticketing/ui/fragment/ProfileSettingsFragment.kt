package com.abdotareq.subway_e_ticketing.ui.fragment

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
import com.abdotareq.subway_e_ticketing.model.GetUserInterface
import com.abdotareq.subway_e_ticketing.model.UserInterface
import com.abdotareq.subway_e_ticketing.model.User
import com.abdotareq.subway_e_ticketing.ui.activity.RegisterActivity
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.utility.imageUtil.BitmapConverter
import com.abdotareq.subway_e_ticketing.utility.imageUtil.ImageUtil
import com.abdotareq.subway_e_ticketing.utility.util
import com.abdotareq.subway_e_ticketing.viewmodels.ProfileViewModel
import com.abdotareq.subway_e_ticketing.viewmodels.factories.ProfileViewModelFactory
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 *  [ProfileSettingsFragment] responsible for user profile settings & changes.
 */
// TODO FIX photo can't be set or shown from view model bu user object as I couldn't convert string image in user to src property
class ProfileSettingsFragment : Fragment() {

    private lateinit var viewModelFactory: ProfileViewModelFactory
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

        getUserFromSplash()

        viewModelFactory = ProfileViewModelFactory(user, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)

        binding.viewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        // if failed to get user obj from splash screen get user call
        if (user.first_name.isNullOrEmpty()) {
            getUserFromCall(SharedPreferenceUtil.getSharedPrefsTokenId(context))
        }

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
                genderDialogBuilder(arrayOf("Female", "Male"))
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

    // get user obj from splash screen or get user call
    private fun getUserFromSplash() {
        // receive user obj from splash screen
        try {
            user = activity?.intent?.getParcelableExtra("user") as User
            viewModel.user.value?.first_name = user.first_name
            viewModel.user.value?.last_name = user.last_name
            viewModel.user.value?.email = user.email
            viewModel.user.value?.gender = user.gender

        } catch (e: Exception) {
            Timber.e(" getUser $e")
        }
        if (user.image != null) {
            val bitMapCon = BitmapConverter(BitmapConverter.AsyncResponse {
                binding.profileImage.setImageBitmap(it)
            })
            bitMapCon.execute(user.image)
        }
    }

    private fun birthDateDialog() {
        // Get Current Date
        val datePickerDialog = DatePickerDialog(context!!,
                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar[Calendar.YEAR] = year
                    calendar[Calendar.MONTH] = monthOfYear
                    calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val currentDateString: String = dateFormat.format(calendar.time)
                    binding.calender.text = currentDateString

                }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    private fun genderDialogBuilder(genderList: Array<String>) {
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.select_gender))
        builder.setItems(genderList) { _, position ->
            binding.genderBtn.text = genderList[position]
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun getUserFromCall(userIdToken: String) {
        var bearerToken = "Bearer "
        bearerToken += userIdToken

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(context, getString(R.string.progMessage))
        progressDialog.show()

        //start the call
        val getUserInterface = object : GetUserInterface {
            override fun onSuccess(userPassed: User) {
                user = userPassed
                progressDialog.dismiss()

                binding.firstNameEt.setText(user.first_name)
                binding.lastNameEt.setText(user.last_name)
                binding.mail.text = user.email
                binding.genderBtn.text = user.gender
                binding.calender.text = user.birth_date?.substring(0..9)

                if (user.image != null) {
                    val bitMapCon = BitmapConverter(BitmapConverter.AsyncResponse {
                        binding.profileImage.setImageBitmap(it)
                    })
                    bitMapCon.execute(user.image)
                }

                if (user.image != null) {
                    val bitMapCon = BitmapConverter(BitmapConverter.AsyncResponse {
                        binding.profileImage.setImageBitmap(it)
                    })
                    bitMapCon.execute(user.image)
                }

                Toast.makeText(context, getString(R.string.data_saved), Toast.LENGTH_LONG).show()
                progressDialog.dismiss()

            }

            override fun onFail(responseCode: Int) {
                progressDialog.dismiss()
                Toast.makeText(context, viewModel.getErrorMess(responseCode), Toast.LENGTH_LONG).show()

            }
        }

        viewModel.getUser(bearerToken, getUserInterface)

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
                    activity!!.finishAffinity()
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
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(context!!, android.R.color.holo_red_dark))
            //set the positive button with the primary color
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(context!!, R.color.primaryColor))
        }
        //show the dialog
        dialog.show()
    }

    // this for change pass dialog
    private fun openChangePassDialog() {
        val passDialog = ChangePassDialogFragment(binding.mail.text.toString())
        requireActivity().supportFragmentManager
                .let { passDialog.show(it, "Pass Dialog") }
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

    private fun updateUser() {
        var bearerToken = "Bearer "
        bearerToken += SharedPreferenceUtil.getSharedPrefsTokenId(context)

        val progressDialog = util.initProgress(context, getString(R.string.progMessage))
        progressDialog.show()

        val profileInterface = object : UserInterface {
            override fun onSuccess() {
                Toast.makeText(context, getString(R.string.data_saved), Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
            }

            override fun onFail(responseCode: Int) {
                progressDialog.dismiss()
                Toast.makeText(context, viewModel.getErrorMess(responseCode), Toast.LENGTH_LONG).show()
            }
        }
        viewModel.saveUserCall(bearerToken, profileInterface)
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

            val dialog = util.initProgress(context, getString(R.string.prog_image_message))
            dialog.show()

            if (requestCode == 10) {
                try {
                    val selectedImageUri: Uri? = data!!.data
//                    val selectedImagePath: String? = getRealPathFromURI(selectedImageUri)
                    binding.profileImage.setImageURI(selectedImageUri)

                    convertImageToString(selectedImageUri)

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
        val cropImage = ImageUtil.resizeImage(context, selectedImageUri)
        val imageString = Base64.encodeToString(cropImage, 4)
        user.image = imageString
    }

    override fun onDestroyView() {
        // this to save user data before destroy fragment or replace ir
        // (when select another fragment from bottom navigation view)
        activity!!.intent.putExtra("user", user) // saving user object.

        super.onDestroyView()
        _binding = null
    }

}
