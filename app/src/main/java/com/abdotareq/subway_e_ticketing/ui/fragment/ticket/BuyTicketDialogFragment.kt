package com.abdotareq.subway_e_ticketing.ui.fragment.ticket

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.abdotareq.subway_e_ticketing.utility.imageUtil.payment.PaymentsUtil
import com.abdotareq.subway_e_ticketing.viewmodels.BuyTicketViewModel
import com.abdotareq.subway_e_ticketing.viewmodels.factories.BuyTicketViewModelFactory
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import kotlinx.android.synthetic.main.buy_ticket_dialog_fragment.*
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import kotlin.math.roundToLong


class BuyTicketDialogFragment(val ticketType: TicketType) : AppCompatDialogFragment() {

    private lateinit var paymentsClient: PaymentsClient

    private lateinit var viewModelFactory: BuyTicketViewModelFactory
    private lateinit var viewModel: BuyTicketViewModel

    private lateinit var binding: BuyTicketDialogFragmentBinding

    /**
     * Arbitrarily-picked constant integer you define to track a request for payment data activity.
     *
     * @value #LOAD_PAYMENT_DATA_REQUEST_CODE
     */
    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 991

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

        // Initialize a Google Pay API client for an environment suitable for testing.
        // It's recommended to create the PaymentsClient object inside of the onCreate method.
        paymentsClient = PaymentsUtil.createPaymentsClient(activity!!)
        possiblyShowGooglePayButton()

        binding.googlePayButton.setOnClickListener { requestPayment() }

        return view
    }

    /**
     * Determine the viewer's ability to pay with a payment method supported by your app and display a
     * Google Pay payment button.
     *
     * @see [](https://developers.google.com/android/reference/com/google/android/gms/wallet/PaymentsClient.html.isReadyToPay
    ) */
    private fun possiblyShowGooglePayButton() {

        val isReadyToPayJson = PaymentsUtil.isReadyToPayRequest() ?: return
        val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString()) ?: return

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        val task = paymentsClient.isReadyToPay(request)
        task.addOnCompleteListener { completedTask ->
            try {
                completedTask.getResult(ApiException::class.java)?.let(::setGooglePayAvailable)
            } catch (exception: ApiException) {
                // Process error
                Timber.e("isReadyToPay failed: $exception")
            }
        }
    }

    /**
     * If isReadyToPay returned `true`, show the button and hide the "checking" text. Otherwise,
     * notify the user that Google Pay is not available. Please adjust to fit in with your current
     * user flow. You are not required to explicitly let the user know if isReadyToPay returns `false`.
     *
     * @param available isReadyToPay API response.
     */
    private fun setGooglePayAvailable(available: Boolean) {
        if (available) {
            googlePayButton.visibility = View.VISIBLE
        } else {
            Toast.makeText(
                    context,
                    "Unfortunately, Google Pay is not available on this device",
                    Toast.LENGTH_LONG).show();
        }
    }

    private fun requestPayment() {

        // Disables the button to prevent multiple clicks.
        googlePayButton.isClickable = false

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        val price = ticketType.price

        val paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(price.toString())
        if (paymentDataRequestJson == null) {
            Timber.e("RequestPayment Can't fetch payment data request")
            return
        }
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    paymentsClient.loadPaymentData(request), activity!!, LOAD_PAYMENT_DATA_REQUEST_CODE)
        }
    }

    /**
     * Handle a resolved activity from the Google Pay payment sheet.
     *
     * @param requestCode Request code originally supplied to AutoResolveHelper in requestPayment().
     * @param resultCode Result code returned by the Google Pay API.
     * @param data Intent from the Google Pay API containing payment or error data.
     * @see [Getting a result
     * from an Activity](https://developer.android.com/training/basics/intents/result)
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            // value passed in AutoResolveHelper
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK ->
                        data?.let { intent ->
                            PaymentData.getFromIntent(intent)?.let(::handlePaymentSuccess)
                        }
                    Activity.RESULT_CANCELED -> {
                        // Nothing to do here normally - the user simply cancelled without selecting a
                        // payment method.
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            handleError(it.statusCode)
                        }
                    }
                }
                // Re-enables the Google Pay payment button.
                googlePayButton.isClickable = true
            }
        }
    }

    /**
     * PaymentData response object contains the payment information, as well as any additional
     * requested information, such as billing and shipping address.
     *
     * @param paymentData A response object returned by Google after a payer approves payment.
     * @see [Payment
     * Data](https://developers.google.com/pay/api/android/reference/object.PaymentData)
     */
    private fun handlePaymentSuccess(paymentData: PaymentData) {
        val paymentInformation = paymentData.toJson() ?: return

        try {
            // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
            val paymentMethodData = JSONObject(paymentInformation).getJSONObject("paymentMethodData")

            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".
            if (paymentMethodData
                            .getJSONObject("tokenizationData")
                            .getString("type") == "PAYMENT_GATEWAY" && paymentMethodData
                            .getJSONObject("tokenizationData")
                            .getString("token") == "examplePaymentMethodToken") {

                AlertDialog.Builder(context)
                        .setTitle("Warning")
                        .setMessage("Gateway name set to \"example\" - please modify " +
                                "Constants.java and replace it with your own gateway.")
                        .setPositiveButton("OK", null)
                        .create()
                        .show()
            }

            val billingName = paymentMethodData.getJSONObject("info")
                    .getJSONObject("billingAddress").getString("name")
            Timber.e(billingName)

            Toast.makeText(context, getString(R.string.payments_show_name, billingName), Toast.LENGTH_LONG).show()

            // Logging token string.
            Timber.e("GooglePaymentToken ${paymentMethodData.getJSONObject("tokenizationData").getString("token")}")

        } catch (e: JSONException) {
            Timber.e("Error: %s", e.toString())
        }

    }

    /**
     * At this stage, the user has already seen a popup informing them an error occurred. Normally,
     * only logging is required.
     *
     * @param statusCode will hold the value of any constant from CommonStatusCode or one of the
     * WalletConstants.ERROR_CODE_* constants.
     * @see [
     * Wallet Constants Library](https://developers.google.com/android/reference/com/google/android/gms/wallet/WalletConstants.constant-summary)
     */
    private fun handleError(statusCode: Int) {
        Timber.tag("loadPaymentData failed").w(String.format("Error code: %d", statusCode))
    }


}