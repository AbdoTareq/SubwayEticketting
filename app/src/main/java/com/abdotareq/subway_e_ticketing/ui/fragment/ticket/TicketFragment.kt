package com.abdotareq.subway_e_ticketing.ui.fragment.ticket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.FragmentSignInBinding
import com.abdotareq.subway_e_ticketing.databinding.FragmentTicketBinding
import com.abdotareq.subway_e_ticketing.viewmodels.SigninViewModel
import com.abdotareq.subway_e_ticketing.viewmodels.factories.SigninViewModelFactory


/**
 * A simple [Fragment] subclass.
 * Use the [TicketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TicketFragment : Fragment() {

    private lateinit var viewModelFactory: SigninViewModelFactory
    private lateinit var viewModel: SigninViewModel

    private var _binding: FragmentTicketBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTicketBinding.inflate(inflater, container, false)
        val view = binding.root



        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
