package com.abdotareq.subway_e_ticketing.ui.fragment.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.BuyTicketDialogFragmentBinding
import com.abdotareq.subway_e_ticketing.model.TicketType
import com.abdotareq.subway_e_ticketing.utility.imageUtil.BitmapConverter
import com.abdotareq.subway_e_ticketing.viewmodels.factories.BuyTicketViewModelFactory
import com.abdotareq.subway_e_ticketing.viewmodels.home.BuyTicketViewModel
import com.google.android.gms.wallet.*
import org.json.JSONArray
import org.json.JSONObject


class BuyTicketDialogFragment(val ticketType: TicketType) : AppCompatDialogFragment() {

    /**
     * A client for interacting with the Google Pay API.
     *
     * @see [PaymentsClient](https://developers.google.com/android/reference/com/google/android/gms/wallet/PaymentsClient)
     */
    private lateinit var paymentsClient: PaymentsClient
    private val shippingCost = (90 * 1000000).toLong()

    private lateinit var garmentList: JSONArray
    private lateinit var selectedGarment: JSONObject

    /**
     * Arbitrarily-picked constant integer you define to track a request for payment data activity.
     *
     * @value #LOAD_PAYMENT_DATA_REQUEST_CODE
     */
    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 991

    private lateinit var viewModelFactory: BuyTicketViewModelFactory
    private lateinit var viewModel: BuyTicketViewModel

    private lateinit var binding: BuyTicketDialogFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BuyTicketDialogFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(activity).application

        viewModelFactory = BuyTicketViewModelFactory(ticketType, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(BuyTicketViewModel::class.java)

        binding.viewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        // show ticket number
        viewModel.ticketNum.observe(viewLifecycleOwner, Observer {
            if (it > 0) {
                val cost = viewModel.ticketNum.value!! * ticketType.price
                Toast.makeText(context, "${viewModel.ticketNum.value}", Toast.LENGTH_SHORT).show()
                binding.totalCost.text = String.format(
                        context!!.getString(R.string.ticket_cost_format, cost))
            }

        })

        // to convert stringImage to bitMap
        if (ticketType.icon != null) {
            val bitMapCon = BitmapConverter(BitmapConverter.AsyncResponse {
                binding.ticketIcon.setImageBitmap(it)
            })
            bitMapCon.execute(ticketType.icon)
        }


        return view
    }


}