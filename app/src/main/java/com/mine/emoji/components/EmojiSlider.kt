package com.mine.emoji.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmojiSlider(
    modifier: Modifier = Modifier,
    width: Dp = 220.dp,
    height: Dp = 80.dp,
    progressColor: Color = Color(0xFFE1306C),
    emoji: String = Emoji.loveFace,
    emojiSize: Float = 66f,
    progressWidth: Float = 18f,
    onSlide: (Float) -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(10f) }
    var progress by remember { mutableStateOf(0f) }
    Box(modifier = modifier) {
        Card(
            shape = RoundedCornerShape(8.dp),
            backgroundColor = Color.White,
            modifier = Modifier
                .height(height)
                .width(width)
                .align(Alignment.Center)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        offsetX += delta
                    },
                    onDragStopped = { isPressed = false },
                    onDragStarted = { isPressed = true }
                )
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                offsetX = offsetX.coerceIn(0f, canvasWidth)
                progress = (offsetX / canvasWidth) * 100
                onSlide(progress)

                //Gray Line
                drawLine(
                    start = Offset(x = 0f, y = canvasHeight / 2),
                    end = Offset(x = canvasWidth, y = canvasHeight / 2),
                    color = Color.LightGray,
                    strokeWidth = progressWidth,
                    cap = StrokeCap.Round
                )
                //progress Line
                drawLine(
                    start = Offset(x = 0f, y = canvasHeight / 2),
                    end = Offset(x = offsetX, y = canvasHeight / 2),
                    color = progressColor,
                    strokeWidth = progressWidth,
                    cap = StrokeCap.Round
                )
                //Emoji as Text
                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawText(
                        emoji,
                        offsetX - 26f, // due to diff in coordinate system
                        (canvasHeight / 2) + 16f,
                        Paint().asFrameworkPaint().apply {
                            textSize = emojiSize
                        }
                    )
                }
            }
        }

        if (isPressed) {
            Text(text = emoji,
                fontSize = progress.coerceIn(20f, 80f).sp,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset {
                        IntOffset(offsetX.toInt(), -200)
                    })
        }
    }
}

object Emoji {

    val smiley = getEmojiByUnicode(0x1F60A)
    val heart = getEmojiByUnicode(0x2764)
    val loveFace = getEmojiByUnicode(0x1F60D)
    val funnyFace = getEmojiByUnicode(0x1F602)
    val yummyFace = getEmojiByUnicode(0x1F60B)
    val claps = getEmojiByUnicode(0x1F44F)
    val fire = getEmojiByUnicode(0x1F525)
    val angryFace = getEmojiByUnicode(0x1F621)
    val shittyFace = getEmojiByUnicode(0x1F4A9)
    val ghost = getEmojiByUnicode(0x1F47B)
    val alien = getEmojiByUnicode(0x1F47D)
    val thumbsUp = getEmojiByUnicode(0x1F44D)
    val thumbsDown = getEmojiByUnicode(0x1F44E)
    val punch = getEmojiByUnicode(0x1F44A)
    val brokenHeart = getEmojiByUnicode(0x1F494)


    private fun getEmojiByUnicode(unicode: Int): String {
        return String(Character.toChars(unicode))
    }
}
