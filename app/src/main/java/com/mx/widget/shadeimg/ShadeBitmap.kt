package com.mx.widget.shadeimg

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.mx.widget.views.ShapeBiz


/**
 * 浮动焦点动态绘制背景阴影
 * Created by ZMX on 2017/12/12.
 */
open class ShadeBitmap @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    private var colorsWeight = 0.3f //渐变颜色权重,0..1之间
    private var startColor = Color.parseColor("#000000") // 开始的颜色
    private var middleColor = Color.parseColor("#66000000") // 中间的颜色
    private var endColor = Color.TRANSPARENT // 结束的颜色
    private var mStrokeWidth: Float = 10f // 绘制边框的宽度
    private var mRadiusWidth: Float = 0.0f // 绘制圆角的半径

    init {
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    /**
     * 设置边框的宽度
     */
    open fun setStroke(w: Float) {
        mStrokeWidth = if (w >= 0) w else 0f
    }

    open fun getStroke(): Float = mStrokeWidth

    /**
     * 设置圆角半径
     */
    open fun setRadius(w: Float) {
        mRadiusWidth = if (w >= 0f) w else 0f
    }

    fun setColors(start: Int, middle: Int, end: Int) {
        startColor = start
        middleColor = middle
        endColor = end
    }

    fun setColorsWeight(float: Float) {
        if (float in 0..1) {
            colorsWeight = float
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (width * height <= 0) return

        val colors = IntArray(3)
        colors[0] = startColor
        colors[1] = middleColor
        colors[2] = endColor

        val positions = FloatArray(3)
        positions[0] = 0f
        positions[1] = colorsWeight
        positions[2] = 1f
        ShapeBiz.drawShadeCircle(canvas, 0f, 0f, width.toFloat(), height.toFloat(), mRadiusWidth, colors, positions, mStrokeWidth)
    }
}
