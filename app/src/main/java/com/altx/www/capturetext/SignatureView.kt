package com.altx.www.capturetext
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class SignatureView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val path = Path()
    private val paint = Paint().apply {
        color = 0xFF000000.toInt()
        strokeWidth = 10f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private lateinit var bitmap: Bitmap
//    private lateinit var canvas: Canvas
private lateinit var internalCanvas: Canvas

    init {
        // Set up the bitmap and canvas
//        post {
//            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//            canvas = Canvas(bitmap)
//        }
        post { initCanvas() }
    }

    private fun initCanvas() {
        if (!::bitmap.isInitialized) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            internalCanvas = Canvas(bitmap)
        }
    }

//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//        canvas.drawPath(path, paint)
//    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    initCanvas()
        // Draw the existing bitmap
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        // Draw the current path on top
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(event.x, event.y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(event.x, event.y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                internalCanvas.drawPath(path, paint)
                path.reset()
                invalidate()
            }
        }
        return true
    }

    fun getSignatureBitmap(): Bitmap {
        return bitmap
    }
}
