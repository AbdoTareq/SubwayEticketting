package com.abdotareq.subway_e_ticketing.ui.fragment.registration

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.FragmentGoogleRegisterBinding
import com.abdotareq.subway_e_ticketing.model.RegisterInterface
import com.abdotareq.subway_e_ticketing.ui.activity.HomeLandActivity
import com.abdotareq.subway_e_ticketing.utility.AnimationUtil
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.utility.Util
import com.abdotareq.subway_e_ticketing.viewmodels.factories.GoogleRegisterViewModelFactory
import com.abdotareq.subway_e_ticketing.viewmodels.register.GoogleRegisterViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class GoogleRegisterFragment : Fragment() {

    private lateinit var viewModelFactory: GoogleRegisterViewModelFactory
    private lateinit var viewModel: GoogleRegisterViewModel


    private var _binding: FragmentGoogleRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var genderList = arrayOf("Female", "Male")
    private var mYear = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var gender = ""
    private var birthDate: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGoogleRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        val safeArgs: GoogleRegisterFragmentArgs by navArgs()
        val token = safeArgs.token

        val application = requireNotNull(activity).application

        viewModelFactory = GoogleRegisterViewModelFactory(token, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(GoogleRegisterViewModel::class.java)

        binding.viewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        AnimationUtil.fadeAnimate(binding.instructions)

        viewModel.gender.observe(viewLifecycleOwner, Observer {
            if (viewModel.validate()){
                register()
            }
        })
        viewModel.birthDate.observe(viewLifecycleOwner, Observer {
            if (viewModel.validate()){
                register()
            }
        })

        callListeners()

        return view
    }

    private fun callListeners() {
        binding.genderBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.select_gender))
            builder.setItems(genderList) { _, position ->
                gender = genderList[position]
                binding.genderBtn.text = gender
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }
        binding.birthDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(requireContext(),
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        calendar[Calendar.YEAR] = year
                        calendar[Calendar.MONTH] = monthOfYear
                        calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                        val currentDateString: String = dateFormat.format(calendar.time)
                        binding.birthDate.text = currentDateString
                        birthDate = currentDateString
                    }, mYear, mMonth, mDay)
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
            // this value = 100 years converted from
            // https://codechi.com/dev-tools/date-to-millisecond-calculators/
            datePickerDialog.datePicker.minDate = calendar.timeInMillis.minus(3153600000000)
            datePickerDialog.show()
        }

    }

    private fun register() {
        //initialize and show a progress dialog to the user
        val progressDialog = Util.initProgress(context, getString(R.string.progMessage))
        progressDialog.show()

        val registerInterface = object : RegisterInterface {
            override fun onSuccess(token: String) {
                //write token into SharedPreferences to use in remember user
                SharedPreferenceUtil.setSharedPrefsLoggedIn(context, true)
                SharedPreferenceUtil.setSharedPrefsTokenId(context, token)
                progressDialog.dismiss()
                val intent = Intent(context, HomeLandActivity::class.java)
                startActivity(intent)
                activity!!.finishAffinity()
            }

            override fun onFail(responseCode: String) {
                progressDialog.dismiss()
                Toast.makeText(context, viewModel.getErrorMess(responseCode), Toast.LENGTH_LONG).show()
            }
        }
        viewModel.registerWithGoogle(registerInterface)

    }

    override fun onDestroyView() {
        // this to save user data before destroy fragment or replace ir
        // (when select another fragment from bottom navigation view)
        super.onDestroyView()
        _binding = null
    }
}
