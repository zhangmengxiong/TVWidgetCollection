package com.mx.widget.views

import android.graphics.*
import kotlin.math.max
import kotlin.math.min

/**
 * Created by ZMX on 2017/12/18.
 */

object ShapeBiz {
    fun drawShadeCircle(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float, radius: Float,
                        colors: IntArray, positions: FloatArray, lineWidth: Float, eraseCircle: Boolean = true) {
        if (colors.isEmpty() || colors.size != positions.size) return
        var radius = radius

        if ((lineWidth + radius) * 2 >= min(canvas.width.toFloat(), canvas.height.toFloat())) {
            // 边距判断
            radius = max((min(canvas.width.toFloat(), canvas.height.toFloat()) / 2f - lineWidth), 0f)
        }

        val ch = lineWidth + radius
        val mPaint = Paint()
        kotlin.run {

            /**
             * 画4条边得渐变效果
             */
            mPaint.shader = LinearGradient(lineWidth, 0f, 0f, 0f, colors, positions, Shader.TileMode.CLAMP)
            canvas.drawRect(left, top + ch, lineWidth, bottom - ch, mPaint)

            mPaint.shader = LinearGradient(0f, lineWidth, 0f, 0f, colors, positions, Shader.TileMode.CLAMP)
            canvas.drawRect(left + ch, top, right - ch, top + lineWidth, mPaint)

            mPaint.shader = LinearGradient(right - lineWidth, 0f, right, 0f, colors, positions, Shader.TileMode.CLAMP)
            canvas.drawRect(right - lineWidth, top + ch, right, bottom - ch, mPaint)

            mPaint.shader = LinearGradient(0f, bottom - lineWidth, 0f, bottom, colors, positions, Shader.TileMode.CLAMP)
            canvas.drawRect(left + ch, bottom - lineWidth, right - ch, bottom, mPaint)
        }


        kotlin.run {
            /**
             * 画4个角的渐变
             */
            val ncs = IntArray(colors.size + 1)
            (0 until ncs.size).forEach {
                if (it < 1) {
                    ncs[it] = colors.first()
                } else {
                    ncs[it] = colors[it - 1]
                }
            }
            val pcs = FloatArray(positions.size + 1)
            (0 until pcs.size).forEach {
                when (it) {
                    0 -> pcs[it] = positions.first()
                    pcs.size - 1 -> pcs[it] = positions.last()
                    1 -> pcs[it] = radius / (radius + lineWidth)
                    else -> pcs[it] = (positions[it - 1] * lineWidth + radius) / (radius + lineWidth)
                }
            }
            mPaint.shader = RadialGradient(ch, ch, ch, ncs, pcs, Shader.TileMode.CLAMP)
            var rect = RectF(left, top, left + ch * 2, top + ch * 2)
            canvas.drawArc(rect, -90f, -90f, true, mPaint)

            mPaint.shader = RadialGradient(right - ch, top + ch, ch, ncs, pcs, Shader.TileMode.CLAMP)
            rect = RectF(right - ch * 2, top, right, top + ch * 2)
            canvas.drawArc(rect, 0f, -90f, true, mPaint)

            mPaint.shader = RadialGradient(ch, bottom - ch, ch, ncs, pcs, Shader.TileMode.CLAMP)
            rect = RectF(left, bottom - ch * 2, left + ch * 2, bottom)
            canvas.drawArc(rect, 90f, 90f, true, mPaint)

            mPaint.shader = RadialGradient(right - ch, bottom - ch, ch, ncs, pcs, Shader.TileMode.CLAMP)
            rect = RectF(right - ch * 2, bottom - ch * 2, right, bottom)
            canvas.drawArc(rect, 0f, 90f, true, mPaint)
        }

        if (eraseCircle && radius > 0) {
            eraseCircle(canvas, left, top, right, bottom, radius, lineWidth)
        }
    }

    /**
     * 删除圆形中多余的部分和边缘锯齿！
     */
    fun eraseCircle(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float, radius: Float, lineWidth: Float) {
        if (radius <= 0) return

        val ch = radius + lineWidth
        /**
         * 圆角成型！
         */
        val paint = Paint()
        paint.color = Color.TRANSPARENT
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        paint.isAntiAlias = true // 抗锯齿在这里很重要！！

        canvas.drawCircle(left + ch, top + ch, radius, paint)
        canvas.drawCircle(right - ch, top + ch, radius, paint)
        canvas.drawCircle(left + ch, bottom - ch, radius, paint)
        canvas.drawCircle(right - ch, bottom - ch, radius, paint)
    }

    /**
     * 画一个圆角线条圓圈
     */
    fun drawLineCircle(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float, radius: Float, color: Int, lineWidth: Float, eraseCircle: Boolean = true) {
        val radius = if (radius >= 0) radius else 0f
        val ch = radius + lineWidth
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = color
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)

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


            if (eraseCircle) {
                paint.color = Color.TRANSPARENT
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                paint.shader = RadialGradient(radius, radius, radius, color, color, Shader.TileMode.CLAMP)
                // 左上角1/4圆
                canvas.drawCircle(left + ch, top + ch, radius, paint)
                canvas.drawCircle(right - ch, top + ch, radius, paint)
                canvas.drawCircle(left + ch, bottom - ch, radius, paint)
                canvas.drawCircle(right - ch, bottom - ch, radius, paint)
            }
        } else {
            // 角度<=0时，填充四个角
            canvas.drawRect(left, top, left + lineWidth, top + ch, paint)
            canvas.drawRect(right - lineWidth, top, right, top + lineWidth, paint)
            canvas.drawRect(left, bottom - lineWidth, left + lineWidth, bottom, paint)
            canvas.drawRect(right - lineWidth, bottom - lineWidth, right, bottom, paint)
        }
    }
}
