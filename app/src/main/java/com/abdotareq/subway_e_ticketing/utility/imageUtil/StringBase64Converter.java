package com.abdotareq.subway_e_ticketing.utility.imageUtil;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.IOException;

/**
 * An AsyncTask Class used to handle the process of converting the image from
 * Bitmap object to a String base64 (byte array) in a another thread in order not to
 * overhead the main thread
 */
public class StringBase64Converter extends AsyncTask<Uri, Void, String> {

    private AsyncResponse responseListener;
    private Context mContext;

    public interface AsyncResponse {
        void processFinish(String output);

        void processFailed();
    }

    public StringBase64Converter(Context context, AsyncResponse responseListener) {
        this.responseListener = responseListener;
        this.mContext = context;
    }

    @Override
    protected String doInBackground(Uri... uris) {

        try {

            //convert the image into input stream
//            assert uris != null;
//            InputStream iStream = mContext.getContentResolver().openInputStream(uris[0]);
//
//            //get bytes from the input stream into byte array
//            assert iStream != null;
//            byte[] inputData = ImageUtil.getBytes(iStream);
            byte[] inputData = ImageUtil.resizeImage(mContext, uris);

            //encode the byte array into BASE64 string
            String encodedImage = Base64.encodeToString(inputData, Base64.DEFAULT);

            //set the user image
            return encodedImage;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (s != null)
            this.responseListener.processFinish(s);
        else
            this.responseListener.processFailed();
    }
}
