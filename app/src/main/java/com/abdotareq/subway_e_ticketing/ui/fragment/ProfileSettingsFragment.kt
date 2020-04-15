package com.abdotareq.subway_e_ticketing.ui.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.ui.activity.RegisterActivity
import com.abdotareq.subway_e_ticketing.databinding.FragmentProfileSettingsBinding
import com.abdotareq.subway_e_ticketing.model.User
import com.abdotareq.subway_e_ticketing.network.UserApiObj
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.utility.imageUtil.BitmapConverter
import com.abdotareq.subway_e_ticketing.utility.imageUtil.ImageUtil
import com.abdotareq.subway_e_ticketing.utility.util
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 *  [ProfileSettingsFragment] responsible for user profile settings & changes.
 */
// TODO FIX GENDER
class ProfileSettingsFragment : Fragment() {

    private var _binding: FragmentProfileSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var user: User? = null

    private var mYear = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0

    private var birthDate: String? = user?.birth_date
    private var gender = user?.gender

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        getUserData()

        callListeners()

        return view
    }

    // get user obj from splash screen or get user call
    private fun getUserData() {
        // receive user obj from splash screen
        try {
            user = activity?.intent?.getParcelableExtra("user") as User
            showData()
        } catch (e: Exception) {
            Timber.e(" getUser $e")
            // if failed to get user obj from splash screen get user call
            getUserData(SharedPreferenceUtil.getSharedPrefsTokenId(context))
        }

    }

    private fun callListeners() {
        val genderList = arrayOf("Female", "Male")
        if (user!=null){
            gender =user?.gender
            birthDate =user?.birth_date
        }


        binding.changePassBtn.setOnClickListener {
            openDialog()
        }

        binding.logOutBtn.setOnClickListener {
            confirmLogOut()
        }

        // save button
        binding.updateBtn.setOnClickListener {
            saveBtnClk()
        }

        binding.pickImage.setOnClickListener {
            pickFromGallery()
        }

        binding.genderBtn.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.select_gender))
            builder.setItems(genderList) { dialogInterface, position ->
                gender = genderList[position]
                binding.genderBtn.text = gender
                user?.gender = gender
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }

        binding.calender.setOnClickListener {
            // Get Current Date
            val datePickerDialog = DatePickerDialog(context!!,
                    OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        val calendar = Calendar.getInstance()
                        calendar[Calendar.YEAR] = year
                        calendar[Calendar.MONTH] = monthOfYear
                        calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                        val currentDateString: String = dateFormat.format(calendar.getTime())
                        binding.calender.text = currentDateString
                        birthDate = currentDateString

                    }, mYear, mMonth, mDay)
            datePickerDialog.show()
        }


    }

    private fun getUserData(userIdToken: String) {
        var bearerToken = "Bearer "
        bearerToken += userIdToken

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(context, getString(R.string.progMessage))
        progressDialog.show()

        //start the call
        UserApiObj.retrofitService.getUser(bearerToken).enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {
                    //get user successfully
                    user = response.body()!!
                    progressDialog.dismiss()
                    showData()

                } else if (responseCode == 438) {
                    //pass not saved successfully
                    Toast.makeText(context, getString(R.string.user_not_found), Toast.LENGTH_LONG).show()
                    Timber.e(getString(R.string.user_not_found))
                    progressDialog.dismiss()
                } else {
                    //user not saved successfully
                    Toast.makeText(context, getString(R.string.else_on_repsonse), Toast.LENGTH_LONG).show()
                    Timber.e(getString(R.string.else_on_repsonse))
                    progressDialog.dismiss()

                }
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                Toast.makeText(context, getString(R.string.check_network), Toast.LENGTH_LONG).show()
                Timber.e("$t")
                progressDialog.dismiss()
            }
        })
    }

    // set profile fields
    private fun showData() {

        try {
            binding.firstNameEt.setText(user?.first_name)
            binding.lastNameEt.setText(user?.last_name)
            binding.mail.text = user?.email
            binding.genderBtn.text = user?.gender
            binding.calender.text = user?.birth_date?.substring(0..9)

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
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(context!!, R.color.colorPrimary))
        }

        //show the dialog
        dialog.show()
    }

    // this for change pass dialog
    private fun openDialog() {
        val passDialog = ChangePassDialogFragment(binding.mail.text.toString())
        fragmentManager?.let { passDialog.show(it, "Pass Dialog") }
    }

    private fun saveBtnClk() {
        //check for all inputs from user are correct
        if (TextUtils.isEmpty(binding.firstNameEt.text.toString()) && binding.firstNameEt.text.toString() == "") {
            binding.firstNameEt.hint = getText(R.string.fix_fist_name)
            return
        } else if (TextUtils.isEmpty(binding.lastNameEt.text.toString()) && binding.lastNameEt.text.toString() == "") {
            binding.lastNameEt.hint = getText(R.string.fix_last_name_mess)
            return
        }

        // update the original user with only changed data
        try {
            user?.first_name = binding.firstNameEt.text.toString()
            user?.last_name = binding.lastNameEt.text.toString()
            user?.gender = binding.genderBtn.text.toString()
            user?.birth_date = binding.calender.text.toString()

        } catch (e: Exception) {
            Timber.e("$user")
        }


        updateUser()

    }

    private fun updateUser() {
        var bearerToken = "Bearer "
        bearerToken += SharedPreferenceUtil.getSharedPrefsTokenId(context)

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(context, getString(R.string.progMessage))
        progressDialog.show()

        //start the call
        UserApiObj.retrofitService.updateUser(user!!, bearerToken)?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {
                    //get user successfully
                    progressDialog.dismiss()
                    Toast.makeText(context, getString(R.string.data_saved), Toast.LENGTH_LONG).show()

                } else if (responseCode == 438) {
                    //user not saved successfully
                    Toast.makeText(context, getString(R.string.user_not_found), Toast.LENGTH_LONG).show()
                    Timber.e(getString(R.string.user_not_found))
                    progressDialog.dismiss()
                } else {
                    //user not saved successfully
                    Toast.makeText(context, getString(R.string.else_on_repsonse), Toast.LENGTH_LONG).show()
                    Timber.e("$responseCode    $user")
                    progressDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(context, getString(R.string.check_network), Toast.LENGTH_LONG).show()
                Timber.e("$t")
                progressDialog.dismiss()
            }
        })
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

    /**
     *  to call [getRealPathFromURI] to get image uri is local from phone storage
     * */
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
        user?.image = imageString
    }

    /**
     *  method to collect image from gallery after picking [pickFromGallery]
     * */
    private fun getRealPathFromURI(uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val cursor: Cursor? = activity!!.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            val columnIndex: Int = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        }
        cursor?.close()
        return uri.path
    }

    override fun onDestroyView() {
        // this to save user data before destroy fragment or replace ir
        // (when select another fragment from bottom navigation view)
        activity!!.intent.putExtra("user", user) // saving user object.

        super.onDestroyView()
        _binding = null
    }

}
