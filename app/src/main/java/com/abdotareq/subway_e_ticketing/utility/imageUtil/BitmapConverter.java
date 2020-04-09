package com.abdotareq.subway_e_ticketing.utility.imageUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * An AsyncTask Class used to handle the process of converting the image from
 * String base64 (byte array) to a Bitmap object in a another thread in order not to
 * overhead the main thread
 */
public class BitmapConverter extends AsyncTask<String, Void, Bitmap> {

    private AsyncResponse responseListener;

    public interface AsyncResponse {
        void processFinish(Bitmap output);
    }

    public BitmapConverter(AsyncResponse responseListener) {
        this.responseListener = responseListener;
    }

    @Override
    protected Bitmap doInBackground(String... imageBase64) {

        try {

        //convert image into byte array using BASE64 decoder
        byte[] im = ImageUtil.imageFromString(imageBase64[0]);

        //convert the byte array into bitmap return it
        return BitmapFactory.decodeByteArray(im, 0, im.length);

        }catch (NullPointerException ne){

            //convert the byte array into bitmap return it
            return BitmapFactory.decodeByteArray(new byte[0], 0, 0);

        }

    }

    @Override
    protected void onPostExecute(Bitmap output) {
        super.onPostExecute(output);
        responseListener.processFinish(output);
    }
}
