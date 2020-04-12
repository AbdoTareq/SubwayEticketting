package com.abdotareq.subway_e_ticketing.utility.imageUtil

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.util.Base64
import java.io.IOException

/**
 * An AsyncTask Class used to handle the process of converting the image from
 * Bitmap object to a String base64 (byte array) in a another thread in order not to
 * overhead the main thread
 */
class StringBase64Converter(private val mContext: Context, private val responseListener: AsyncResponse) : AsyncTask<Uri?, Void?, String?>() {

    interface AsyncResponse {
        fun processFinish(output: String?)
        fun processFailed()
    }

    override fun doInBackground(vararg p0: Uri?): String? {
        try {

            //convert the image into input stream
//            assert uris != null;
//            InputStream iStream = mContext.getContentResolver().openInputStream(uris[0]);
//
//            //get bytes from the input stream into byte array
//            assert iStream != null;
//            byte[] inputData = ImageUtil.getBytes(iStream);
            val inputData = ImageUtil.resizeImage(mContext, *p0)

            //encode the byte array into BASE64 string

            //set the user image
            return Base64.encodeToString(inputData, Base64.DEFAULT)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(s: String?) {
        super.onPostExecute(s)
        if (s != null) responseListener.processFinish(s) else responseListener.processFailed()
    }

}