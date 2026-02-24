package cat.dam.andy.database_room_compose.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.InputStream

object ImageUtils {

    private const val JPEG_QUALITY = 85
    private const val MAX_SIZE_PX = 512

    fun decodeBytes(bytes: ByteArray?): Bitmap? {
        if (bytes == null || bytes.isEmpty()) return null
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        if (degrees == 0) return bitmap
        val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }

    fun bitmapToJpegBytes(bitmap: Bitmap): ByteArray {
        val scaled = scaleDownIfNeeded(bitmap)
        val stream = ByteArrayOutputStream()
        scaled.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, stream)
        if (scaled != bitmap) scaled.recycle()
        return stream.toByteArray()
    }

    fun uriToBytes(context: Context, uri: Uri): ByteArray? {
        context.contentResolver.openInputStream(uri)?.use { input: InputStream ->
            val bitmap = BitmapFactory.decodeStream(input) ?: return null
            return bitmapToJpegBytes(bitmap)
        }
        return null
    }

    private fun scaleDownIfNeeded(bitmap: Bitmap): Bitmap {
        val max = maxOf(bitmap.width, bitmap.height)
        if (max <= MAX_SIZE_PX) return bitmap
        val scale = MAX_SIZE_PX.toFloat() / max
        val w = (bitmap.width * scale).toInt().coerceAtLeast(1)
        val h = (bitmap.height * scale).toInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(bitmap, w, h, true)
    }
}
