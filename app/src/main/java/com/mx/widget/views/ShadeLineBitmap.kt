package com.mx.widget.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import kotlin.math.max
import kotlin.math.min

/**
 * Created by ZMX on 2017/12/18.
 */

open class ShadeLineBitmap @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {
    private var linePaint: Paint? = null
    private var mPaint: Paint? = null // 阴影画笔
    private var emptyPaint: Paint? = null // 圆角删除画笔
    private var strokeDelete: Paint? = null// 边缘锯齿删除画笔

    private var colorsWeight = 0.4f //渐变颜色权重,0..1之间

    private var mLTCircle: RadialGradient? = null // 左上角圆形
    private var mRTCircle: RadialGradient? = null // 右上角圆形
    private var mLBCircle: RadialGradient? = null // 左下角圆形
    private var mRBCircle: RadialGradient? = null // 右下角圆形

    private var mLLinear: LinearGradient? = null // 左边的边框
    private var mTLinear: LinearGradient? = null // 顶部的边框
    private var mRLinear: LinearGradient? = null // 右边的边框
    private var mBLinear: LinearGradient? = null // 底部的边框

    private var lineColor = Color.BLACK
    private var startColor = Color.parseColor("#00c3ff") // 开始的颜色
    private var middleColor = Color.parseColor("#6600c3ff") // 中间的颜色
    private var endColor = Color.parseColor("#0000c3ff") // 结束的颜色

    private var mLineWidth = 4f
    private var mStrokeWidth: Float = 10f // 绘制边框的宽度
    private var mRadiusWidth: Float = 0.0f // 绘制圆角的半径

    /**
     * 四个角画圆形的半径
     */
    private var mLineMargin: Float = 0f

    init {
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        linePaint = Paint()
        linePaint?.color = lineColor

        mPaint = Paint()
//        mPaint?.isAntiAlias = true

        emptyPaint = Paint()
        emptyPaint?.style = Paint.Style.FILL
        emptyPaint?.color = Color.TRANSPARENT
        emptyPaint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        strokeDelete = Paint()
        strokeDelete?.strokeWidth = 1.0.toFloat()              //线宽
        strokeDelete?.style = Paint.Style.STROKE
        strokeDelete?.color = Color.TRANSPARENT
        strokeDelete?.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    /**
     * 设置边框的宽度
     */
    open fun setStroke(w: Float) {
        if (w >= 0) {
            mStrokeWidth = w - mLineWidth
        }
    }

    protected fun getStroke(): Float = mStrokeWidth

    /**
     * 设置圆角半径
     */
    open fun setRadius(w: Float) {
        if (w >= 0f) mRadiusWidth = w
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

        initGradient(width, height)
        drawCircle(canvas)
        drawLine(canvas)
        eraseCircle(canvas)
    }

    private fun initGradient(w: Int, h: Int) {
        if ((mStrokeWidth + mRadiusWidth) * 2 >= min(w, h)) {
            // 边距判断
            mRadiusWidth = max((min(w, h) / 2f - mStrokeWidth), 0f)
        }
        mLineMargin = mStrokeWidth + mRadiusWidth

        var colors = IntArray(4)
        colors[0] = startColor
        colors[1] = startColor
        colors[2] = middleColor
        colors[3] = endColor
        val positions = FloatArray(4)
        positions[0] = 0f
        positions[1] = mRadiusWidth / (mRadiusWidth + mStrokeWidth)
        positions[2] = (colorsWeight * mStrokeWidth + mRadiusWidth) / mLineMargin
        positions[3] = 1f

        mLTCircle = RadialGradient(mLineMargin, mLineMargin, mLineMargin, colors, positions, Shader.TileMode.REPEAT)
        mRTCircle = RadialGradient(w - mLineMargin, mLineMargin, mLineMargin, colors, positions, Shader.TileMode.REPEAT)
        mLBCircle = RadialGradient(mLineMargin, h - mLineMargin, mLineMargin, colors, positions, Shader.TileMode.REPEAT)
        mRBCircle = RadialGradient(w - mLineMargin, h - mLineMargin, mLineMargin, colors, positions, Shader.TileMode.REPEAT)

//        mLLinear = LinearGradient(0f, mStrokeWidth, h - mStrokeWidth, mStrokeWidth, startColor, endColor, Shader.TileMode.CLAMP)
        colors = IntArray(3)
        colors[0] = startColor
        colors[1] = middleColor
        colors[2] = endColor

        val floats = FloatArray(3)
        floats[0] = 0f
        floats[1] = colorsWeight
        floats[2] = 1f

        mLLinear = LinearGradient(mStrokeWidth, mStrokeWidth, 0f, mStrokeWidth, colors, floats, Shader.TileMode.REPEAT)
        mTLinear = LinearGradient(mStrokeWidth, mStrokeWidth, mStrokeWidth, 0f, colors, floats, Shader.TileMode.REPEAT)
        mRLinear = LinearGradient(w - mStrokeWidth, mStrokeWidth, w.toFloat(), mStrokeWidth, colors, floats, Shader.TileMode.REPEAT)
        mBLinear = LinearGradient(mStrokeWidth, height - mStrokeWidth, mStrokeWidth, height.toFloat(), colors, floats, Shader.TileMode.REPEAT)
    }

    /**
     * 画圆形
     */
    private fun drawCircle(canvas: Canvas) {
        val width = width
        val height = height

        // 左上角画圆角阴影
        mPaint?.shader = mLTCircle
        var rect = RectF(0f, 0f, mLineMargin * 2, mLineMargin * 2)
        canvas.drawArc(rect, -90f, -90f, true, mPaint)
        // 删除圆角边缘可能存在的黑点，删除1个像素！
        strokeDelete?.shader = mLTCircle
        canvas.drawArc(rect, -90f, -90f, false, strokeDelete)

        // 右上角画圆角阴影
        mPaint?.shader = mRTCircle
        rect = RectF(width - mLineMargin * 2, 0f, width.toFloat(), mLineMargin * 2)
        canvas.drawArc(rect, 0f, -90f, true, mPaint)
        // 删除圆角边缘可能存在的黑点，删除1个像素！
        strokeDelete?.shader = mRTCircle
        canvas.drawArc(rect, 0f, -90f, false, strokeDelete)

        // 左下角画圆角阴影
        mPaint?.shader = mLBCircle
        rect = RectF(0f, height - mLineMargin * 2, mLineMargin * 2, height.toFloat())
        canvas.drawArc(rect, 90f, 90f, true, mPaint)
        // 删除圆角边缘可能存在的黑点，删除1个像素！
        strokeDelete?.shader = mLBCircle
        canvas.drawArc(rect, 90f, 90f, false, strokeDelete)

        // 右下角画圆角阴影
        mPaint?.shader = mRBCircle
        rect = RectF(width - mLineMargin * 2, height - mLineMargin * 2, width.toFloat(), height.toFloat())
        canvas.drawArc(rect, 0f, 90f, true, mPaint)
        // 删除圆角边缘可能存在的黑点，删除1个像素！
        strokeDelete?.shader = mRBCircle
        canvas.drawArc(rect, 0f, 90f, false, strokeDelete)


        kotlin.run {
            val cw = mLineWidth + mRadiusWidth
            val paint = Paint(linePaint)

            paint.shader = RadialGradient(cw, cw, cw, Color.RED, Color.RED, Shader.TileMode.MIRROR)
            paint.isAntiAlias = true
            rect = RectF(mStrokeWidth, mStrokeWidth, cw * 2 + mStrokeWidth, cw * 2 + mStrokeWidth)
            canvas.drawArc(rect, -90f, -90f, true, paint)

            rect = RectF(width - mStrokeWidth - cw * 2, mStrokeWidth, width - mStrokeWidth, cw * 2 + mStrokeWidth)
            canvas.drawArc(rect, 0f, -90f, true, paint)
        }
    }

    /**
     * 画四条边框
     */
    private fun drawLine(canvas: Canvas) {
        val width = width
        val height = height
        kotlin.run {
            /**
             * 画阴影四边
             */
            mPaint?.shader = mLLinear
            canvas.drawRect(0f, mLineMargin, mStrokeWidth, height - mLineMargin, mPaint)

            mPaint?.shader = mTLinear
            canvas.drawRect(mLineMargin, 0f, width - mLineMargin, mStrokeWidth, mPaint)

            mPaint?.shader = mRLinear
            canvas.drawRect(width - mStrokeWidth, mLineMargin, width.toFloat(), height - mLineMargin, mPaint)

            mPaint?.shader = mBLinear
            canvas.drawRect(mLineMargin, height - mStrokeWidth, width - mLineMargin, height.toFloat(), mPaint)
        }

        kotlin.run {
            canvas.drawRect(mStrokeWidth + mRadiusWidth, mStrokeWidth,
                    width - mStrokeWidth - mRadiusWidth, mStrokeWidth + mLineWidth, linePaint)
            canvas.drawRect(mStrokeWidth, mStrokeWidth + mRadiusWidth,
                    mStrokeWidth + mLineWidth, height - mRadiusWidth - mStrokeWidth, linePaint)
            canvas.drawRect(width - mStrokeWidth - mLineWidth, mStrokeWidth + mRadiusWidth,
                    width - mStrokeWidth, height - mRadiusWidth - mStrokeWidth, linePaint)
            canvas.drawRect(mStrokeWidth + mRadiusWidth, height - mStrokeWidth - mLineWidth,
                    width - mStrokeWidth - mRadiusWidth, height - mStrokeWidth, linePaint)
        }
    }

    /**
     * 删除圆形中多余的部分和边缘锯齿！
     */
    private fun eraseCircle(canvas: Canvas) {
        val width = width
        val height = height
        val mLineMargin = mLineMargin + mLineWidth
        /**
         * 圆角成型！
         */
        val paint = Paint(emptyPaint)
        paint.isAntiAlias = true // 抗锯齿在这里很重要！！
        canvas.drawCircle(mLineMargin, mLineMargin, mRadiusWidth, paint)
        canvas.drawCircle(width - mLineMargin, mLineMargin, mRadiusWidth, paint)
        canvas.drawCircle(mLineMargin, height - mLineMargin, mRadiusWidth, paint)
        canvas.drawCircle(width - mLineMargin, height - mLineMargin, mRadiusWidth, paint)
    }
}
