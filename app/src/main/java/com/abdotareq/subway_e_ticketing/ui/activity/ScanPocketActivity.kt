package com.abdotareq.subway_e_ticketing.ui.activity

import android.R.attr.bitmap
import android.os.Bundle
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidmads.library.qrgenearator.QRGSaver
import androidx.appcompat.app.AppCompatActivity
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.ActivityScanPocketBinding
import com.abdotareq.subway_e_ticketing.model.BoughtTicket
import com.abdotareq.subway_e_ticketing.model.InTicket
import timber.log.Timber


class ScanPocketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanPocketBinding

    private var num: Int = 0

    private var checkInTicket = InTicket()
    private var boughtTicket = BoughtTicket()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanPocketBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // get ticket
        try {
            num = 1
            checkInTicket = intent?.getParcelableExtra("checkInTicket") as InTicket
            Timber.e("$checkInTicket")
            binding.title.text = getString(R.string.check_out)
            binding.price.text = String.format(getString(R.string.ticket_price_format, checkInTicket.price))
            binding.instructions.text = String.format(getString(R.string.scan_mess_format, getString(R.string.exit), getString(R.string.check_out)))

        } catch (e: Exception) {
            Timber.e(e)
            num = 2
            boughtTicket = intent?.getParcelableExtra("boughtTicket") as BoughtTicket
            binding.title.text = getString(R.string.check_in)
            binding.price.text = String.format(getString(R.string.ticket_price_format, boughtTicket.price))
            binding.instructions.text = String.format(getString(R.string.scan_mess_format, getString(R.string.entrance), getString(R.string.use)))
        }

        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        val qrgEncoder = if (num == 1)
            QRGEncoder(checkInTicket.id, null, QRGContents.Type.TEXT, 650)
        else
            QRGEncoder(boughtTicket.id, null, QRGContents.Type.TEXT, 650)

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

    }
}
