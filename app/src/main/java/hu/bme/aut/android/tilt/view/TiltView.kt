package hu.bme.aut.android.tilt.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import hu.bme.aut.android.tilt.model.TiltModel
import hu.bme.aut.android.tilt.model.TiltModel.getModelString
import hu.bme.aut.android.tilt.model.TiltModel.initGame
import hu.bme.aut.android.tilt.model.TiltModel.model
import java.lang.Integer.min

class TiltView : View {

    private val paintBg = Paint()
    private val paintLine = Paint()
    private val paintRed = Paint()
    private val paintGreen = Paint()
    private val paintWall = Paint()
    private val paintHole = Paint()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        paintBg.color = Color.LTGRAY
        paintBg.style = Paint.Style.FILL

        paintLine.color = Color.DKGRAY
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5F

        paintRed.color = Color.RED
        paintRed.style = Paint.Style.FILL

        paintGreen.color = Color.GREEN
        paintGreen.style = Paint.Style.FILL

        paintWall.color = Color.DKGRAY
        paintWall.style = Paint.Style.FILL

        paintHole.color = Color.BLACK
        paintHole.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paintBg)

        drawGameArea(canvas)
        drawElements(canvas)
    }

    private fun drawGameArea(canvas: Canvas) {
        val widthFloat: Float = width.toFloat()
        val heightFloat: Float = height.toFloat()

        // border
        canvas.drawRect(0F, 0F, widthFloat, heightFloat, paintLine)

        // four horizontal lines
        canvas.drawLine(0F, heightFloat / 5, widthFloat, widthFloat / 5, paintLine)
        canvas.drawLine(0F, 2 * heightFloat / 5, widthFloat, 2 * heightFloat / 5, paintLine)
        canvas.drawLine(0F, 3 * heightFloat / 5, widthFloat, 3 * heightFloat / 5, paintLine)
        canvas.drawLine(0F, 4 * heightFloat / 5, widthFloat, 4 * heightFloat / 5, paintLine)

        // four vertical lines
        canvas.drawLine(widthFloat / 5, 0F, widthFloat / 5, heightFloat, paintLine)
        canvas.drawLine(2 * widthFloat / 5, 0F, 2 * widthFloat / 5, heightFloat, paintLine)
        canvas.drawLine(3 * widthFloat / 5, 0F, 3 * widthFloat / 5, heightFloat, paintLine)
        canvas.drawLine(4 * widthFloat / 5, 0F, 4 * widthFloat / 5, heightFloat, paintLine)
    }

    private fun drawElements(canvas: Canvas) {
        for (i in 0 until 5) {
            for (j in 0 until 5) {
                when (TiltModel.getFieldContent(i, j)) {
                    TiltModel.RED_PUCK -> {
                        val centerX = i * width / 5 + width / 10
                        val centerY = j * height / 5 + height / 10
                        val radius = height / 10 - 2
                        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), radius.toFloat(), paintRed)
                    }
                    TiltModel.GREEN_PUCK -> {
                        val centerX = i * width / 5 + width / 10
                        val centerY = j * height / 5 + height / 10
                        val radius = height / 10 - 2
                        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), radius.toFloat(), paintGreen)
                    }
                    TiltModel.HOLE -> {
                        val centerX = i * width / 5 + width / 10
                        val centerY = j * height / 5 + height / 10
                        val radius = height / 10 - 2
                        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), radius.toFloat(), paintHole)
                    }
                    TiltModel.WALL -> {
                        val left = i * width / 5
                        val top = j * height / 5
                        val right = (i+1) * width / 5
                        val bottom = (j+1) * height / 5
                        canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paintWall)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        val d: Int

        when {
            w == 0 -> { d = h }
            h == 0 -> { d = w }
            else -> { d = min(w, h) }
        }

        setMeasuredDimension(d, d)
    }

    fun swipe (dir: Short) {
        TiltModel.move(dir)
        invalidate()
    }


}