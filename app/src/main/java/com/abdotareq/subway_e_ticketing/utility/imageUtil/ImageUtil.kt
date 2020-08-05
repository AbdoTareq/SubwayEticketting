package com.abdotareq.subway_e_ticketing.utility.imageUtil

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

/**
 * An image util class that contains a global static methods used across the App
 */
object ImageUtil {
    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream?): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len: Int
        while (inputStream!!.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    fun convertImageToByte(uri: Uri?, context: Context): ByteArray? {
        var data: ByteArray? = null
        try {
            val cr = context.contentResolver
            val inputStream = cr.openInputStream(uri!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            data = baos.toByteArray()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return data
    }

    @JvmStatic
    fun imageFromString(imageString: String?): ByteArray {

        // create a buffered image
        return Base64.decode(imageString, Base64.DEFAULT)
    }

    /**
     * A method called to write a bitmap image into a private
     * file to be accessed somewhere else in the app
     */
    fun createImageFromBitmap(context: Context, bitmap: Bitmap, fileName: String?): String? {
        var fileName2 = fileName
        try {
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val fo = context.openFileOutput(fileName2, Context.MODE_PRIVATE)
            fo.write(bytes.toByteArray())
            // remember close file output
            fo.close()
        } catch (e: Exception) {
            e.printStackTrace()
            fileName2 = null
        }
        return fileName2
    }

    /**
     * A method called to resize an image
     */
    @Throws(IOException::class)
    fun resizeImage(context: Context, vararg uris: Uri?): ByteArray {
        val b = MediaStore.Images.Media.getBitmap(context.contentResolver, uris[0])
        val origWidth = b.width
        val origHeight = b.height
        val destWidth = 500
        return if (origWidth > destWidth) {
            // picture is wider than we want it, we calculate its target height
            val destHeight = origHeight / (origWidth / destWidth)
            // we create an scaled bitmap so it reduces the image, not just trim it
            val b2 = Bitmap.createScaledBitmap(b, destWidth, destHeight, false)
            val outStream = ByteArrayOutputStream()
            // compress to the format you want, JPEG, PNG...
            // 70 is the 0-100 quality percentage
            b2.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            outStream.toByteArray()
        } else {


            //convert the image into input stream
            val iStream = context.contentResolver.openInputStream(uris[0]!!)!!
            getBytes(iStream)
        }
    }
}