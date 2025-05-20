package com.st_10256115.flappybirdnew

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import kotlin.random.Random

class Pipe(private var x: Float, private val screenHeight: Int, private val screenWidth: Int) {

    private val pipeWidth = 200f
    private val gapHeight = 400f
    private val speed = 10f

    private var gapTop: Float = Random.nextInt(200, screenHeight - 600).toFloat()

    private val paint = Paint().apply {
        color = Color.GREEN
        isAntiAlias = true
    }

    // Track if bird has passed this pipe
    var isScored = false

    fun update() {
        x -= speed

        // If the pipe has moved off the screen, reset to the right side
        if (x + pipeWidth < 0) {
            x = screenWidth.toFloat()
            gapTop = Random.nextInt(200, screenHeight - 600).toFloat()
            isScored = false // reset scoring state
        }
    }

    fun draw(canvas: Canvas) {
        // Top pipe
        canvas.drawRect(x, 0f, x + pipeWidth, gapTop, paint)
        // Bottom pipe
        canvas.drawRect(x, gapTop + gapHeight, x + pipeWidth, screenHeight.toFloat(), paint)
    }

    fun getX(): Float = x
    fun getWidth(): Float = pipeWidth

    fun collidesWith(birdBounds: RectF): Boolean {
        val topRect = RectF(x, 0f, x + pipeWidth, gapTop)
        val bottomRect = RectF(x, gapTop + gapHeight, x + pipeWidth, screenHeight.toFloat())
        return RectF.intersects(birdBounds, topRect) || RectF.intersects(birdBounds, bottomRect)
    }
}
