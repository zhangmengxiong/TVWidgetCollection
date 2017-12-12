package com.mx.widget.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


/**
 * Created by ZMX on 2017/12/12.
 */
open class Draw9Bitmap @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mPaint: Paint? = null
    private var emptyPaint: Paint? = null
    private var mLTCircle: RadialGradient? = null // 左上角圆形
    private var mRTCircle: RadialGradient? = null // 右上角圆形
    private var mLBCircle: RadialGradient? = null // 左下角圆形
    private var mRBCircle: RadialGradient? = null // 右下角圆形

    private var mLLinear: LinearGradient? = null // 左边的边框
    private var mTLinear: LinearGradient? = null // 顶部的边框
    private var mRLinear: LinearGradient? = null // 右边的边框
    private var mBLinear: LinearGradient? = null // 底部的边框

    private val startColor = Color.parseColor("#000000") // 开始的颜色
    private val middleColor = Color.parseColor("#66000000") // 中间的颜色
    private val endColor = Color.parseColor("#00000000") // 结束的颜色
    protected var mColorWidth: Float = 30f // 绘制边框的宽度
    protected var mCircleWidth: Float = 0f // 绘制圆角的半径


    /**
     * 圆形的半径
     */
    private var mLineMargin: Float = 0f
        get() {
            return mColorWidth + mCircleWidth
        }

    init {
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        mPaint = Paint()

        emptyPaint = Paint()
        emptyPaint?.style = Paint.Style.FILL
        emptyPaint?.color = Color.TRANSPARENT
        emptyPaint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
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
        positions[1] = mCircleWidth / (mCircleWidth + mColorWidth)
        positions[2] = 0.3f
        positions[3] = 1f

        mLTCircle = RadialGradient(mLineMargin, mLineMargin, mLineMargin, colors, positions, Shader.TileMode.REPEAT)
        mRTCircle = RadialGradient(w - mLineMargin, mLineMargin, mLineMargin, colors, positions, Shader.TileMode.REPEAT)
        mLBCircle = RadialGradient(mLineMargin, h - mLineMargin, mLineMargin, colors, positions, Shader.TileMode.REPEAT)
        mRBCircle = RadialGradient(w - mLineMargin, h - mLineMargin, mLineMargin, colors, positions, Shader.TileMode.REPEAT)

//        mLLinear = LinearGradient(0f, mColorWidth, h - mColorWidth, mColorWidth, startColor, endColor, Shader.TileMode.CLAMP)
        colors = IntArray(3)
        colors[0] = startColor
        colors[1] = middleColor
        colors[2] = endColor

        val floats = FloatArray(3)
        floats[0] = 0f
        floats[1] = 0.3f
        floats[2] = 1f

        mLLinear = LinearGradient(mColorWidth, mColorWidth, 0f, mColorWidth, colors, floats, Shader.TileMode.REPEAT)
        mTLinear = LinearGradient(mColorWidth, mColorWidth, mColorWidth, 0f, colors, floats, Shader.TileMode.REPEAT)
        mRLinear = LinearGradient(w - mColorWidth, mColorWidth, w.toFloat(), mColorWidth, colors, floats, Shader.TileMode.REPEAT)
        mBLinear = LinearGradient(mColorWidth, height - mColorWidth, mColorWidth, height.toFloat(), colors, floats, Shader.TileMode.REPEAT)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCircle(canvas)
        eraseCircle(canvas)
        drawLine(canvas)
        drawCircleLine(canvas)
    }

    /**
     * 画圆形
     */
    private fun drawCircle(canvas: Canvas) {
        val width = width
        val height = height

        mPaint?.shader = mLTCircle
        canvas.drawCircle(mLineMargin, mLineMargin, mLineMargin, mPaint)

        mPaint?.shader = mRTCircle
        canvas.drawCircle(width - mLineMargin, mLineMargin, mLineMargin, mPaint)

        mPaint?.shader = mLBCircle
        canvas.drawCircle(mLineMargin, height - mLineMargin, mLineMargin, mPaint)

        mPaint?.shader = mRBCircle
        canvas.drawCircle(width - mLineMargin, height - mLineMargin, mLineMargin, mPaint)
    }

    /**
     * 删除圆形中多余的部分
     */
    private fun eraseCircle(canvas: Canvas) {
        val width = width
        val height = height
        /**
         * 删除三个方向的扇形
         */
        var rect = RectF(0f, 0f, mLineMargin * 2, mLineMargin * 2)
        canvas.drawArc(rect, -90f, 270f, true, emptyPaint)

        rect = RectF(width - mLineMargin * 2, 0f, width.toFloat(), mLineMargin * 2)
        canvas.drawArc(rect, 0f, 270f, true, emptyPaint)

        rect = RectF(0f, height - mLineMargin * 2, mLineMargin * 2, height.toFloat())
        canvas.drawArc(rect, 180f, 270f, true, emptyPaint)
        canvas.drawArc(rect, -90f, 90f, true, emptyPaint)

        rect = RectF(width - mLineMargin * 2, height - mLineMargin * 2, width.toFloat(), height.toFloat())
        canvas.drawArc(rect, 90f, 270f, true, emptyPaint)
        canvas.drawArc(rect, -90f, 0f, true, emptyPaint)

        /**
         * 圆角成型！
         */
        val paint = Paint(emptyPaint)
        paint.isAntiAlias = true // 抗锯齿在这里很重要！！
        canvas.drawCircle(mLineMargin, mLineMargin, mCircleWidth, paint)
        canvas.drawCircle(width - mLineMargin, mLineMargin, mCircleWidth, paint)
        canvas.drawCircle(mLineMargin, height - mLineMargin, mCircleWidth, paint)
        canvas.drawCircle(width - mLineMargin, height - mLineMargin, mCircleWidth, paint)
    }

    /**
     * 画四条边框
     */
    private fun drawLine(canvas: Canvas) {
        val width = width
        val height = height

        mPaint?.shader = mLLinear
        canvas.drawRect(0f, mLineMargin, mColorWidth, height - mLineMargin, mPaint)

        mPaint?.shader = mTLinear
        canvas.drawRect(mLineMargin, 0f, width - mLineMargin, mColorWidth, mPaint)

        mPaint?.shader = mRLinear
        canvas.drawRect(width - mColorWidth, mLineMargin, width.toFloat(), height - mLineMargin, mPaint)

        mPaint?.shader = mBLinear
        canvas.drawRect(mLineMargin, height - mColorWidth, width - mLineMargin, height.toFloat(), mPaint)
    }


    private fun drawCircleLine(canvas: Canvas) {

    }

}
