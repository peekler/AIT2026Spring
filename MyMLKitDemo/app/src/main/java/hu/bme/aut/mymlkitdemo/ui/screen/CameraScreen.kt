package hu.bme.aut.mymlkitdemo.ui.screen


import android.annotation.SuppressLint
import android.util.Size
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.face.Face
import hu.bme.aut.mymlkitdemo.ui.mlkit.FaceAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

@SuppressLint("RestrictedApi")
@Composable
fun CameraScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var faces by remember { mutableStateOf<List<Face>>(emptyList()) }
    var frameSize by remember { mutableStateOf(Size(1, 1)) }

    val previewView = remember {
        PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    LaunchedEffect(Unit) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val analysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetResolution(Size(1024, 768))
            .build()

        analysis.setAnalyzer(
            Dispatchers.Default.asExecutor(),
            FaceAnalyzer(
                onResult = { list, size ->
                    faces = list
                    frameSize = size
                }
            )
        )

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA, // front camera is fun for students
                preview,
                analysis
            )
        } catch (_: Exception) { /* handle if needed */ }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        // Overlay
        FaceOverlay(
            faces = faces,
            frameSize = frameSize
        )

        // Simple status chip
        Surface(
            modifier = Modifier
                .padding(12.dp)
                .align(androidx.compose.ui.Alignment.TopStart),
            color = Color.Black.copy(alpha = 0.5f),
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = if (faces.isEmpty()) "No face" else "Faces: ${faces.size}",
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun FaceOverlay(faces: List<Face>, frameSize: Size) {
    // We must map from the frame (analysis) size to the canvas (screen) size
    Canvas(modifier = Modifier.fillMaxSize()) {
        val scaleX = size.width / frameSize.width.toFloat()
        val scaleY = size.height / frameSize.height.toFloat()

        faces.forEach { face ->
            val rect = face.boundingBox
            val left = rect.left * scaleX
            val top = rect.top * scaleY
            val right = rect.right * scaleX
            val bottom = rect.bottom * scaleY

            // Draw face box
            drawRect(
                color = Color.Green,
                topLeft = androidx.compose.ui.geometry.Offset(left, top),
                size = androidx.compose.ui.geometry.Size(right - left, bottom - top),
                style = Stroke(width = 4f)
            )

            // Draw emotion label
            val label = inferEmotion(
                smile = face.smilingProbability ?: -1f,
                leftEye = face.leftEyeOpenProbability ?: -1f,
                rightEye = face.rightEyeOpenProbability ?: -1f
            )
            drawContext.canvas.nativeCanvas.apply {
                val p = android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 40f
                    isAntiAlias = true
                }
                drawText("$label", left, top - 10f, p)
            }
        }
    }
}

/** Very simple, teachable heuristic */
private fun inferEmotion(smile: Float, leftEye: Float, rightEye: Float): String {
    val eyesClosed = (leftEye in 0f..0.3f) && (rightEye in 0f..0.3f)
    return when {
        smile >= 0.6f -> "😊 Happy"
        eyesClosed -> "😴 Sleepy"
        else -> "😐 Neutral"
    }
}