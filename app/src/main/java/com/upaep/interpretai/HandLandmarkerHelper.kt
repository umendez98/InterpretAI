package com.upaep.interpretai

import android.content.Context
import android.util.Log
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult

class HandLandmarkerHelper(
    context: Context,
    private val onResult: (FloatArray?, HandLandmarkerResult?) -> Unit
) {
    private var handLandmarker: HandLandmarker

    init {
        handLandmarker = HandLandmarker.createFromFile(context, "hand_landmarker.task")
    }

    fun detect(mpImage: MPImage) {
        try {
            val result: HandLandmarkerResult = handLandmarker.detect(mpImage)

            if (result.landmarks().isNotEmpty()) {
                val landmarks = result.landmarks()[0]

                val xList = mutableListOf<Float>()
                val yList = mutableListOf<Float>()

// Recolectamos primero todas las coordenadas
                for (i in 0 until 21) {
                    xList.add(landmarks[i].y())
                    yList.add(1f - landmarks[i].x())
                }

                // Normalización: x - min(x), y - min(y)
                val minX = xList.minOrNull() ?: 0f
                val minY = yList.minOrNull() ?: 0f

                val input = FloatArray(42)
                for (i in 0 until 21) {
                    input[i * 2] = xList[i] - minX
                    input[i * 2 + 1] = yList[i] - minY
                }

                onResult(input, result)
            } else {
                onResult(null, result)
            }

        } catch (e: Exception) {
            Log.e("HandLandmarkerHelper", "Error detectando: ${e.message}")
            onResult(null, null)
        }
    }
}
