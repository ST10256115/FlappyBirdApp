package com.st_10256115.flappybirdnew

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Bird(private var x: Float, private var y: Float) {

    private var radius = 50f
    private var velocity = 0f
    private val gravity = 1f
    private val paint = Paint().apply {
        color = Color.YELLOW
        isAntiAlias = true
    }

    fun update() {
        velocity += gravity
        y += velocity
    }

    fun flap() {
        velocity = -20f // Pushes bird upward
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(x, y, radius, paint)
    }

    fun reset(centerX: Float, centerY: Float) {
        x = centerX
        y = centerY
        velocity = 0f
    }

    fun getY(): Float = y

    fun getBounds(): RectF {
        return RectF(
            x - radius,
            y - radius,
            x + radius,
            y + radius
        )
    }
}
