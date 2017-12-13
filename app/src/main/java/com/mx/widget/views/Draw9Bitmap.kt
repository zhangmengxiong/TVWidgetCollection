package com.mx.widget.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min


/**
 * 浮动焦点动态绘制背景阴影
 * Created by ZMX on 2017/12/12.
 */
open class Draw9Bitmap @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mPaint: Paint? = null // 阴影画笔
    private var emptyPaint: Paint? = null // 圆角删除画笔
    private var strokeDelete: Paint? = null// 边缘锯齿删除画笔

    private var colorsWeight = 0.2f //渐变颜色权重,0..1之间

    private var mLTCircle: RadialGradient? = null // 左上角圆形
    private var mRTCircle: RadialGradient? = null // 右上角圆形
    private var mLBCircle: RadialGradient? = null // 左下角圆形
    private var mRBCircle: RadialGradient? = null // 右下角圆形

    private var mLLinear: LinearGradient? = null // 左边的边框
    private var mTLinear: LinearGradient? = null // 顶部的边框
    private var mRLinear: LinearGradient? = null // 右边的边框
    private var mBLinear: LinearGradient? = null // 底部的边框

    private var startColor = Color.parseColor("#000000") // 开始的颜色
    private var middleColor = Color.parseColor("#66000000") // 中间的颜色
    private var endColor = Color.TRANSPARENT // 结束的颜色
    private var mStrokeWidth: Float = 10f // 绘制边框的宽度
    private var mRadiusWidth: Float = 0f // 绘制圆角的半径


    /**
     * 圆形的半径
     */
    private var mLineMargin: Float = 0f
        get() {
            return mStrokeWidth + mRadiusWidth
        }

    init {
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
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
    fun setStroke(w: Float) {
        mStrokeWidth = w
    }

    protected fun getStroke(): Float = mStrokeWidth

    /**
     * 设置圆角半径
     */
    fun setRadius(w: Float) {
        mRadiusWidth = w
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


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (mLineMargin * 2 >= min(w, h)) {
            // 边距判断
            mRadiusWidth = min(w, h) / 2f - mStrokeWidth
        }
        initGradient(w, h)
    }

    private fun initGradient(w: Int, h: Int) {
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCircle(canvas)
        eraseCircle(canvas)
        drawLine(canvas)
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
    }

    /**
     * 删除圆形中多余的部分和边缘锯齿！
     */
    private fun eraseCircle(canvas: Canvas) {
        val width = width
        val height = height
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

    /**
     * 画四条边框
     */
    private fun drawLine(canvas: Canvas) {
        val width = width
        val height = height

        mPaint?.shader = mLLinear
        canvas.drawRect(0f, mLineMargin, mStrokeWidth, height - mLineMargin, mPaint)

        mPaint?.shader = mTLinear
        canvas.drawRect(mLineMargin, 0f, width - mLineMargin, mStrokeWidth, mPaint)

        mPaint?.shader = mRLinear
        canvas.drawRect(width - mStrokeWidth, mLineMargin, width.toFloat(), height - mLineMargin, mPaint)

        mPaint?.shader = mBLinear
        canvas.drawRect(mLineMargin, height - mStrokeWidth, width - mLineMargin, height.toFloat(), mPaint)
    }
}
