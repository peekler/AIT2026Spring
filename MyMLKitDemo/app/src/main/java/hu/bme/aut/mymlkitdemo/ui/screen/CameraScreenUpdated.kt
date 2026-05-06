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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.text.Text
import hu.bme.aut.mymlkitdemo.ui.mlkit.FaceAnalyzer
import hu.bme.aut.mymlkitdemo.ui.mlkit.TextAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

data class EmotionThresholds(
    val happySmile: Float = 0.70f,  // smile >= -> Happy
    val contentSmile: ClosedFloatingPointRange<Float> = 0.40f..0.70f,
    val eyesClosed: Float = 0.30f,  // avgEye < -> Sleepy
    val eyesWide: Float = 0.80f,    // avgEye > -> Surprised/Excited
    val sadSmileMax: Float = 0.20f  // smile < & eyes not closed -> Sad
)

enum class Mode { FACE, TEXT }

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen2(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // ---- State: faces, frame size, lens, thresholds, slider panel toggle
    var faces by remember { mutableStateOf<List<Face>>(emptyList()) }
    var frameSize by remember { mutableStateOf(Size(1, 1)) }

    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_FRONT) }
    var thresholds by remember { mutableStateOf(EmotionThresholds()) }

    var recognizedText by remember { mutableStateOf<Text?>(null) }
    var textFrameSize by remember { mutableStateOf(Size(1, 1)) }

    var mode by remember { mutableStateOf(Mode.FACE) }


    // Camera preview view
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

    // (Re)bind camera whenever lensFacing changes
    LaunchedEffect(lensFacing) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }
        val analysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetResolution(Size(640, 480))
            .build()

        when (mode) {
            Mode.FACE -> analysis.setAnalyzer(
                Dispatchers.Default.asExecutor(),
                FaceAnalyzer { list, size ->
                    faces = list
                    frameSize = size
                }
            )
            Mode.TEXT -> analysis.setAnalyzer(
                Dispatchers.Default.asExecutor(),
                TextAnalyzer { result, size ->
                    recognizedText = result
                    textFrameSize = size
                }
            )
        }

        val selector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                selector,
                preview,
                analysis
            )
        } catch (_: Exception) { /* handle if needed */ }
    }

    // Scaffold with Top App Bar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Face & Emotion Detection") },
                actions = {
                    TextButton(onClick = {
                        mode = if (mode == Mode.FACE) Mode.TEXT else Mode.FACE
                    }) {
                        Text(
                            if (mode == Mode.FACE) "Text Mode" else "Face Mode"
                        )
                    }
                    IconButton(onClick = {
                        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT)
                            CameraSelector.LENS_FACING_BACK
                        else
                            CameraSelector.LENS_FACING_FRONT
                    }) {
                        Icon(Icons.Default.Cameraswitch, contentDescription = "Switch camera")
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            // Camera preview
            AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

            if (mode == Mode.FACE) {
                // your existing overlay with boxes, emotions, etc.
                FaceOverlay(faces = faces, frameSize = frameSize, thresholds = thresholds)

                // Small status chip
                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart),
                    color = Color.Black.copy(alpha = 0.55f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (faces.isEmpty()) "No face"
                        else "Faces: ${faces.size}",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            } else {
                // new overlay to show recognized text
                TextOverlay(recognizedText)
            }


        }
    }
}

@Composable
private fun TextOverlay(textResult: Text?) {
    if (textResult == null) return

    Canvas(modifier = Modifier.fillMaxSize()) {
        val frameWidth = textResult.textBlocks.firstOrNull()?.boundingBox?.width()?.toFloat() ?: size.width
        val scaleX = size.width / frameWidth
        val scaleY = size.height / frameWidth

        // Draw each block
        textResult.textBlocks.forEach { block ->
            val rect = block.boundingBox ?: return@forEach
            val left = rect.left * scaleX
            val top = rect.top * scaleY
            val right = rect.right * scaleX
            val bottom = rect.bottom * scaleY

            drawRect(
                color = Color.Yellow,
                topLeft = androidx.compose.ui.geometry.Offset(left, top),
                size = androidx.compose.ui.geometry.Size(right - left, bottom - top),
                style = Stroke(width = 4f)
            )

            drawContext.canvas.nativeCanvas.apply {
                val p = android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 42f
                    isAntiAlias = true
                }
                drawText(block.text, left, top - 10f, p)
            }
        }
    }
}

// ---------------------------
// Overlay: boxes + labels + CONFIDENCE
// ---------------------------
@Composable
private fun FaceOverlay(
    faces: List<Face>,
    frameSize: Size,
    thresholds: EmotionThresholds
) {
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

            // Compute emotion & confidence strings
            val smiling = face.smilingProbability ?: -1f
            val leftEye = face.leftEyeOpenProbability ?: -1f
            val rightEye = face.rightEyeOpenProbability ?: -1f

            val label = inferEmotion(
                smile = smiling,
                leftEye = leftEye,
                rightEye = rightEye,
                t = thresholds
            )

            val confText = buildString {
                append("smile=")
                append(if (smiling >= 0f) "%.2f".format(smiling) else "—")
                append("  leftEye=")
                append(if (leftEye >= 0f) "%.2f".format(leftEye) else "—")
                append("  rightEye=")
                append(if (rightEye >= 0f) "%.2f".format(rightEye) else "—")
            }

            // Draw label and confidences above the box
            drawContext.canvas.nativeCanvas.apply {
                val titlePaint = android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 42f
                    isAntiAlias = true
                }
                val subPaint = android.graphics.Paint().apply {
                    color = android.graphics.Color.LTGRAY
                    textSize = 32f
                    isAntiAlias = true
                }
                drawText(label, left, (top - 14f).coerceAtLeast(42f), titlePaint)
                drawText(confText, left, (top + 24f).coerceAtLeast(84f), subPaint)
            }
        }
    }
}

// ---------------------------
// Emotion inference with thresholds (includes "Sad")
// ---------------------------
private fun inferEmotion(
    smile: Float,
    leftEye: Float,
    rightEye: Float,
    t: EmotionThresholds
): String {
    val eyeVals = listOf(leftEye, rightEye).filter { it >= 0f }
    val avgEye = eyeVals.takeIf { it.isNotEmpty() }?.average()?.toFloat() ?: 0.5f

    val bigSmile = smile >= t.happySmile
    val slightSmile = smile in t.contentSmile
    val eyesClosed = avgEye < t.eyesClosed
    val eyesWide = avgEye > t.eyesWide
    val sad = (smile in 0f..t.sadSmileMax) && !eyesClosed && avgEye < 0.7f

    return when {
        bigSmile && eyesWide -> "🤩 Excited"
        bigSmile -> "😊 Happy"
        slightSmile && !eyesClosed -> "🙂 Content"
        sad -> "☹️ Sad"
        eyesClosed && !bigSmile -> "😴 Sleepy"
        !bigSmile && eyesWide -> "😮 Surprised"
        else -> "😐 Neutral"
    }
}
