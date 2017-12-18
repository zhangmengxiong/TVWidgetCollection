package com.mx.widget.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.max
import kotlin.math.min

/**
 * Created by ZMX on 2017/12/18.
 */

open class ShadeLineBitmap @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ShadeBitmap(context, attrs, defStyleAttr) {
    private var lineWidth = 5f
    private var strokeWidth = 1f
    private var mRadius = 0f

    override fun setStroke(w: Float) {
        strokeWidth = w - lineWidth
        super.setStroke(strokeWidth)
    }

    override fun setRadius(w: Float) {
        mRadius = w
        super.setRadius(if (w > 0) w + lineWidth else 0f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (width * height <= 0) return

        drawLineCircle(canvas, strokeWidth, strokeWidth, width - strokeWidth, height - strokeWidth
                , mRadius, Color.BLUE, lineWidth)
    }

}
