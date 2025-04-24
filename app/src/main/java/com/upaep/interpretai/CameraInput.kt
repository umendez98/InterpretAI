package com.upaep.interpretai

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.PointF
import android.graphics.YuvImage
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.Executors

@Composable
fun CameraInput(
    modifier: Modifier = Modifier,
    onResult: (FloatArray?) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = context as LifecycleOwner

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            val overlay = LandmarkOverlay(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            val container = FrameLayout(ctx).apply {
                addView(previewView)
                addView(overlay)
            }

            startCamera(ctx, lifecycleOwner, previewView, overlay, onResult)
            container
        }
    )
}

@SuppressLint("UnsafeOptInUsageError")
private fun startCamera(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    overlay: LandmarkOverlay,
    onResult: (FloatArray?) -> Unit
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    val executor = Executors.newSingleThreadExecutor()

    val helper = HandLandmarkerHelper(context) { result: FloatArray?, rawResult: HandLandmarkerResult? ->
        onResult(result)

        rawResult?.landmarks()?.firstOrNull()?.let { landmarks ->
            val points = landmarks.map {
                // 🔄 INVERSIÓN HORIZONTAL: y → x, 1 - x → y, y reflejado horizontalmente
                val mirroredX = (1f - it.y()) * previewView.width
                val mirroredY = (1f - it.x()) * previewView.height
                PointF(mirroredX, mirroredY)
            }
            overlay.setLandmarks(points)
        } ?: overlay.setLandmarks(emptyList())
    }

    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(previewView.surfaceProvider)
        }

        val analyzer = ImageAnalysis.Builder().build().apply {
            setAnalyzer(executor) { imageProxy ->
                val bitmap = imageProxyToBitmap(imageProxy)
                val mpImage = BitmapImageBuilder(bitmap).build()
                helper.detect(mpImage)
                imageProxy.close()
            }
        }

        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            analyzer
        )
    }, ContextCompat.getMainExecutor(context))
}

private fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap {
    val yBuffer: ByteBuffer = imageProxy.planes[0].buffer
    val uBuffer: ByteBuffer = imageProxy.planes[1].buffer
    val vBuffer: ByteBuffer = imageProxy.planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)

    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, imageProxy.width, imageProxy.height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(android.graphics.Rect(0, 0, imageProxy.width, imageProxy.height), 100, out)
    val imageBytes = out.toByteArray()

    return android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}