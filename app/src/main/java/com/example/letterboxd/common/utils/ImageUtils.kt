package com.example.letterboxd.common.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.IOException

fun getRotationDegrees(filePath: String): Float {
    return try {
        val exif = ExifInterface(filePath)
        when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }
    } catch (e: IOException) {
        0f
    }
}

fun rotateBitmap(bitmap: Bitmap, rotationDegrees: Float): Bitmap {
    val matrix = Matrix().apply {
        postRotate(rotationDegrees)
    }

    return Bitmap.createBitmap(
        bitmap,
        0, 0,
        bitmap.width,
        bitmap.height,
        matrix,
        true
    )
}