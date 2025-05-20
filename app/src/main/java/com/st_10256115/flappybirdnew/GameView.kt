package com.st_10256115.flappybirdnew

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context, attrs: AttributeSet? = null) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    private val gameThread: GameThread
    private lateinit var bird: Bird
    private val pipes = mutableListOf<Pipe>()
    private val pipeSpacing = 800
    private val numberOfPipes = 3
    private var isGameOver = false

    private var score = 0
    private val scorePaint = Paint().apply {
        color = Color.WHITE
        textSize = 80f
        textAlign = Paint.Align.LEFT
    }

    init {
        holder.addCallback(this)
        gameThread = GameThread(holder, this)
        isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        startGame()
        gameThread.setRunning(true)
        gameThread.start()
    }

    private fun startGame() {
        val centerX = width / 2f
        val centerY = height / 2f
        bird = Bird(centerX, centerY)

        pipes.clear()
        for (i in 0 until numberOfPipes) {
            val pipeX = width + i * pipeSpacing
            pipes.add(Pipe(pipeX.toFloat(), height, width))
        }

        score = 0
        isGameOver = false
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        gameThread.setRunning(false)
        while (retry) {
            try {
                gameThread.join()
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawColor(Color.CYAN)

        if (::bird.isInitialized && !isGameOver) {
            bird.update()
            bird.draw(canvas)

            for (pipe in pipes) {
                pipe.update()
                pipe.draw(canvas)

                // Collision with pipe
                if (pipe.collidesWith(bird.getBounds())) {
                    isGameOver = true
                }

                // Score when passing pipe
                if (!pipe.isScored && pipe.getX() + pipe.getWidth() < bird.getBounds().left) {
                    score++
                    pipe.isScored = true
                }
            }

            // Collision with top/bottom of screen
            if (bird.getY() < 0 || bird.getY() > height) {
                isGameOver = true
            }
        }

        // Show score always
        canvas.drawText("Score: $score", 50f, 100f, scorePaint)

        if (isGameOver) {
            drawGameOver(canvas)
        }
    }

    private fun drawGameOver(canvas: Canvas) {
        val paint = Paint().apply {
            color = Color.RED
            textSize = 100f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("Game Over", width / 2f, height / 2f, paint)
    }

    private fun resetGame() {
        startGame()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (isGameOver) {
                resetGame()
            } else if (::bird.isInitialized) {
                bird.flap()
            }
            return true
        }
        return false
    }
}
