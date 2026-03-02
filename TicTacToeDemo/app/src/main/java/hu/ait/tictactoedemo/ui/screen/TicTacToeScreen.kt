package hu.ait.tictactoedemo.ui.screen

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.ait.tictactoedemo.R
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable



@Composable
fun TicTacToeScreen(modifier: Modifier = Modifier,
                    ticTacToeViewModel: TicTacToeViewModel = viewModel()
                    ) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(R.string.text_welcome),
                fontSize = 30.sp
            )
        }

        val zoomState = rememberZoomState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f)
                .zoomable(zoomState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(R.string.text_current_player,
                    ticTacToeViewModel.currentPlayer),
                fontSize = 26.sp,
                modifier = Modifier.padding(bottom = 30.dp)
            )

            TicTacToeGameArea(ticTacToeViewModel)
        }

        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier.width(200.dp),
                onClick = {ticTacToeViewModel.resetGame()},
            ) {
                Text(stringResource(R.string.btn_reset))
            }
        }
    }
}


@Composable
fun TicTacToeGameArea(ticTacToeViewModel: TicTacToeViewModel){

    val imageDemo: ImageBitmap = ImageBitmap.imageResource(
        R.drawable.spaceship)
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .aspectRatio(1f)
            .pointerInput(key1 = Unit) {
                detectTapGestures { offset ->
                    Log.d(
                        "TAG_CANVAS", "${offset.x}," +
                                " ${offset.x}"
                    )
                    // offset is the x,y pixel coordinate where I clicked
                    // we transform it to 0,0 or 0,1 or... 2,2
                    val cellX = (offset.x / (size.width / 3)).toInt()
                    val cellY = (offset.y / (size.height / 3)).toInt()

                    ticTacToeViewModel.onCellClicked(
                        BoardCell(cellY, cellX)
                    )
                }
            }
    ) {
        drawImage(
            image = imageDemo,
            srcOffset = IntOffset(0,0),
            dstOffset = IntOffset(0,0),
            srcSize = IntSize(imageDemo.width,imageDemo.height),
            dstSize = IntSize((size.width/3).toInt(),
                (size.height/3).toInt())
        )

        drawImage(
            image = imageDemo,
            srcOffset = IntOffset(0,0),
            dstOffset = IntOffset(
                (size.width/3).toInt()
                ,(size.height/3).toInt()),
            srcSize = IntSize(imageDemo.width,imageDemo.height),
            dstSize = IntSize((size.width/3).toInt(),
                (size.height/3).toInt())
        )

        val textLayoutResult: TextLayoutResult  =
            textMeasurer.measure(
                text = "5",
                style = TextStyle(fontSize =
                    (size.minDimension / 3).toSp(),
                    fontWeight = FontWeight.Bold
                )
            )
        val textSize = textLayoutResult.size
        drawText(
            textLayoutResult,
            topLeft = Offset(
                x= size.width/3+(size.width/3)/2-textSize.width/2,
                y= 0f
            )
        )

        // Draw the grid
        val gridSize = size.minDimension
        val thirdSize = gridSize / 3

        /*for (i in 0..2) {
            for (j in 0..2) {
                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = Offset(
                        x  = (thirdSize/2 + i*thirdSize) - textSize.width/2,
                        y = (thirdSize/2 + j*thirdSize) - textSize.height/2
                    ),
                )
            }
        }*/



        for (i in 1..2) {
            drawLine(
                color = Color.Black,
                strokeWidth = 8f,
                pathEffect = PathEffect.cornerPathEffect(4f),
                start = androidx.compose.ui.geometry.Offset(thirdSize * i, 0f),
                end = androidx.compose.ui.geometry.Offset(thirdSize * i, gridSize)
            )
            drawLine(
                color = Color.Black,
                strokeWidth = 8f,

                start = androidx.compose.ui.geometry.Offset(0f, thirdSize * i),
                end = androidx.compose.ui.geometry.Offset(gridSize, thirdSize * i),
            )
        }

        // Draw the X and O
        for (row in 0..2) {
            for (col in 0..2) {
                val player = ticTacToeViewModel.board[row][col]
                if (player != null) {
                    val centerX = col * thirdSize + thirdSize / 2
                    val centerY = row * thirdSize + thirdSize / 2
                    if (player == Player.X) {
                        drawLine(
                            color = Color.Black,
                            strokeWidth = 8f,
                            pathEffect = PathEffect.cornerPathEffect(4f),
                            start = androidx.compose.ui.geometry.Offset(centerX - thirdSize / 4, centerY - thirdSize / 4),
                            end = androidx.compose.ui.geometry.Offset(centerX + thirdSize / 4, centerY + thirdSize / 4),
                        )
                        drawLine(
                            color = Color.Black,
                            strokeWidth = 8f,
                            pathEffect = PathEffect.cornerPathEffect(4f),
                            start = androidx.compose.ui.geometry.Offset(centerX + thirdSize / 4, centerY - thirdSize / 4),
                            end = androidx.compose.ui.geometry.Offset(centerX - thirdSize / 4, centerY + thirdSize / 4),
                        )
                    } else {
                        drawCircle(
                            color = Color.Black,
                            style = Stroke(width = 8f),
                            center = androidx.compose.ui.geometry.Offset(centerX, centerY),
                            radius = thirdSize / 4,
                        )
                    }
                }
            }
        }
    }
}