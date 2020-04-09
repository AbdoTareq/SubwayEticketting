package com.abdotareq.subway_e_ticketing.utility.imageUtil;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * An image util class that contains a global static methods used across the App
 */
public class ImageUtil {

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static byte[] convertImageToByte(Uri uri, Context context) {
        byte[] data = null;
        try {
            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static byte[] imageFromString(String imageString) {

        // create a buffered image
        byte[] imageByte;

        imageByte = Base64.decode(imageString, Base64.DEFAULT);

        return imageByte;
    }


    /**
     * A method called to write a bitmap image into a private
     * file to be accessed somewhere else in the app
     */
    public static String createImageFromBitmap(Context context, Bitmap bitmap, String fileName) {

        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    /**
     * A method called to resize an image
     */
    public static byte[] resizeImage(Context context, Uri... uris) throws IOException {

        assert uris != null;

        Bitmap b = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uris[0]);

        int origWidth = b.getWidth();
        int origHeight = b.getHeight();

        final int destWidth = 500;

        if (origWidth > destWidth) {
            // picture is wider than we want it, we calculate its target height
            int destHeight = origHeight / (origWidth / destWidth);
            // we create an scaled bitmap so it reduces the image, not just trim it
            Bitmap b2 = Bitmap.createScaledBitmap(b, destWidth, destHeight, false);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            // compress to the format you want, JPEG, PNG...
            // 70 is the 0-100 quality percentage
            b2.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

            byte[] byteArray = outStream.toByteArray();

            return byteArray;
        } else {


            //convert the image into input stream
            InputStream iStream = context.getContentResolver().openInputStream(uris[0]);

            //get bytes from the input stream into byte array
            assert iStream != null;
            return ImageUtil.getBytes(iStream);
        }

    }

}
