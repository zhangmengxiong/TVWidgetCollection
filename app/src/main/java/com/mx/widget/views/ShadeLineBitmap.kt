package com.mx.widget.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet

/**
 * Created by ZMX on 2017/12/18.
 */

open class ShadeLineBitmap @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ShadeBitmap(context, attrs, defStyleAttr) {
    private var linePaint: Paint? = null
    private var lineWidth = 4f
    private var mStrokeWidth: Float = 10f // 绘制边框的宽度
    private var mRadiusWidth: Float = 0.0f // 绘制圆角的半径

    init {
        linePaint = Paint()
        linePaint?.color = Color.RED

    }

    override fun setStroke(w: Float) {
        mStrokeWidth = w - lineWidth
        super.setStroke(mStrokeWidth)
    }

    override fun setRadius(w: Float) {
        mRadiusWidth = w
        super.setRadius(w)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawLines(canvas)
        drawRadius(canvas)
    }

    private fun drawLines(canvas: Canvas) {
        canvas.drawRect(mStrokeWidth + mRadiusWidth, mStrokeWidth,
                width - mStrokeWidth - mRadiusWidth, mStrokeWidth + lineWidth, linePaint)
        canvas.drawRect(mStrokeWidth, mStrokeWidth + mRadiusWidth,
                mStrokeWidth + lineWidth, height - mRadiusWidth - mStrokeWidth, linePaint)
        canvas.drawRect(width - mStrokeWidth - lineWidth, mStrokeWidth + mRadiusWidth,
                width - mStrokeWidth, height - mRadiusWidth - mStrokeWidth, linePaint)
        canvas.drawRect(mStrokeWidth + mRadiusWidth, height - mStrokeWidth - lineWidth,
                width - mStrokeWidth - mRadiusWidth, height - mStrokeWidth, linePaint)
    }

    private fun drawRadius(canvas: Canvas) {

    }
}
