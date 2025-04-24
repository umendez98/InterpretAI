//Aquí se visualizan los puntos de referencia de la mano
package com.upaep.interpretai

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View

class LandmarkOverlay(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val landmarkPoints = mutableListOf<PointF>()
    private val paintPoint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        strokeWidth = 10f
    }
    private val paintLine = Paint().apply {
        color = Color.GREEN
        strokeWidth = 4f
    }

    fun setLandmarks(landmarks: List<PointF>) {
        landmarkPoints.clear()
        landmarkPoints.addAll(landmarks)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (landmarkPoints.size == 21) {
            drawConnections(canvas)
            landmarkPoints.forEach {
                canvas.drawCircle(it.x, it.y, 8f, paintPoint)
            }
        }
    }

    private fun drawConnections(canvas: Canvas) {
        val connections = listOf(
            0 to 1, 1 to 2, 2 to 3, 3 to 4,      // thumb
            0 to 5, 5 to 6, 6 to 7, 7 to 8,      // index
            5 to 9, 9 to 10, 10 to 11, 11 to 12, // middle
            9 to 13, 13 to 14, 14 to 15, 15 to 16, // ring
            13 to 17, 17 to 18, 18 to 19, 19 to 20, // pinky
            0 to 17 // palm base to pinky base
        )

        for ((start, end) in connections) {
            val p1 = landmarkPoints[start]
            val p2 = landmarkPoints[end]
            canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paintLine)
        }
    }
}
