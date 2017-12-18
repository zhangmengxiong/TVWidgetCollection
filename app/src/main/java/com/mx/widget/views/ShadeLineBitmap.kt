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


    /**
     * 画一个圆角线条圓圈
     */
    private fun drawLineCircle(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float, radius: Float, color: Int, lineWidth: Float) {
        val radius = if (radius >= 0) radius else 0f
        val ch = radius + lineWidth
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = color

        canvas.drawRect(left, top + ch, left + lineWidth, bottom - ch, paint)
        canvas.drawRect(left + ch, top, right - ch, top + lineWidth, paint)
        canvas.drawRect(right - lineWidth, top + ch, right, bottom - ch, paint)
        canvas.drawRect(left + ch, bottom - lineWidth, right - ch, bottom, paint)

        if (radius > 0) {
            // 角度大于0时，画圆角
            paint.shader = RadialGradient(ch, ch, ch, color, color, Shader.TileMode.CLAMP)
            // 左上角1/4圆
            var rect = RectF(left, top, left + ch * 2, top + ch * 2)
            canvas.drawArc(rect, -90f, -90f, true, paint)

            // 右上角1/4圆
            rect = RectF(right - ch * 2, top, right, top + ch * 2)
            canvas.drawArc(rect, 0f, -90f, true, paint)

            // 左下角1/4圆
            rect = RectF(left, bottom - ch * 2, left + ch * 2, bottom)
            canvas.drawArc(rect, 90f, 90f, true, paint)

            // 右下角1/4圆
            rect = RectF(right - ch * 2, bottom - ch * 2, right, bottom)
            canvas.drawArc(rect, 0f, 90f, true, paint)


            paint.color = Color.TRANSPARENT
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            paint.shader = RadialGradient(radius, radius, radius, color, color, Shader.TileMode.CLAMP)

            // 左上角1/4圆
            canvas.drawCircle(left + ch, top + ch, radius, paint)
            canvas.drawCircle(right - ch, top + ch, radius, paint)
            canvas.drawCircle(left + ch, bottom - ch, radius, paint)
            canvas.drawCircle(right - ch, bottom - ch, radius, paint)
        } else {
            // 角度<=0时，填充四个角
            canvas.drawRect(left, top, left + lineWidth, top + ch, paint)
            canvas.drawRect(right - lineWidth, top, right, top + lineWidth, paint)
            canvas.drawRect(left, bottom - lineWidth, left + lineWidth, bottom, paint)
            canvas.drawRect(right - lineWidth, bottom - lineWidth, right, bottom, paint)
        }
    }
}
