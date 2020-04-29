package com.abdotareq.subway_e_ticketing.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.FragmentScanPocketBinding
import timber.log.Timber


class ScanTicketFragment : Fragment() {

    private var num: Int = 0

    private var _binding: FragmentScanPocketBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentScanPocketBinding.inflate(inflater, container, false)
        val view = binding.root

        // get ticket
        val safeArgs: ScanTicketFragmentArgs by navArgs()
        val checkInTicket = safeArgs.inTicket
        val boughtTicket = safeArgs.boughtTicket

        if (checkInTicket != null) {
            num = 1
            Timber.e("$checkInTicket")
            binding.title.text = getString(R.string.check_out)
            binding.price.text = String.format(getString(R.string.ticket_price_format, checkInTicket.price))
            binding.instructions.text = String.format(getString(R.string.scan_mess_format, getString(R.string.exit), getString(R.string.check_out)))
        } else if (boughtTicket != null) {
            num = 2
            Timber.e("$boughtTicket")
            binding.title.text = getString(R.string.check_in)
            binding.price.text = String.format(getString(R.string.ticket_price_format, boughtTicket.price))
            binding.instructions.text = String.format(getString(R.string.scan_mess_format, getString(R.string.entrance), getString(R.string.use)))
        }

        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        val qrgEncoder = if (num == 1)
            QRGEncoder(checkInTicket!!.id, null, QRGContents.Type.TEXT, 650)
        else
            QRGEncoder(boughtTicket!!.id, null, QRGContents.Type.TEXT, 650)

        // to control it's colors
//        qrgEncoder.setColorBlack(Color.RED);
//        qrgEncoder.setColorWhite(Color.BLUE);

        try {
            // Getting QR-Code as Bitmap
            val bitmap = qrgEncoder.bitmap
            // Setting Bitmap to ImageView
            binding.qrImage.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Timber.e(e)
        }

        // Save with location, value, bitmap returned and type of Image(JPG/PNG).
//        val qrgSaver = QRGSaver()
//        qrgSaver.save(savePath, edtValue.getText().toString().trim(), bitmap, QRGContents.ImageType.IMAGE_JPEG)

        return view
    }
}
