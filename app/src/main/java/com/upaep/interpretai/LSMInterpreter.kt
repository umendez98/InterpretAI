package com.upaep.interpretai

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class LSMInterpreter(context: Context) {

    private var interpreter: Interpreter
    private var labels: List<String>

    init {
        interpreter = Interpreter(loadModelFile(context, "lsm_model.tflite"))
        labels = context.assets.open("labels.txt").bufferedReader().readLines()
    }

    private fun loadModelFile(context: Context, modelFileName: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelFileName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength)
    }

    fun predict(inputData: FloatArray): String {
        val input = arrayOf(inputData)
        val output = Array(1) { FloatArray(labels.size) }

        interpreter.run(input, output)

        val maxIdx = output[0].indices.maxByOrNull { output[0][it] } ?: -1
        return labels[maxIdx]
    }
}
