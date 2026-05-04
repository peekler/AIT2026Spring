package hu.bme.aut.mymlkitdemo.ui.mlkit

import android.util.Size
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlin.collections.emptyList

class TextAnalyzer(
    private val onResult: (Text, Size) -> Unit
) : ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: return imageProxy.close()
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        recognizer.process(image)
            .addOnSuccessListener { result ->
                onResult(result, Size(imageProxy.width, imageProxy.height))
            }
            .addOnFailureListener {
                onResult(Text("", emptyList<Any>()), Size(imageProxy.width,
                    imageProxy.height
                ))
            }
            .addOnCompleteListener { imageProxy.close() }
    }
}