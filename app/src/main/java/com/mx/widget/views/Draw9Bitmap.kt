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
    private val TRANS_COLOR = Color.parseColor("#00000000")

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

    private val startColor = Color.parseColor("#FF4081") // 开始的颜色
    private val endColor = Color.parseColor("#00FF4081") // 结束的颜色
    protected var mColorWidth: Float = 40f // 绘制边框的宽度
    protected var mCircleWidth: Float = 5f // 绘制圆角的半径

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
        emptyPaint?.color = 0x00ffffff
        emptyPaint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        var colors = IntArray(3)
        colors[0] = startColor
        colors[1] = startColor
        colors[2] = endColor
        val positions = FloatArray(3)
        positions[0] = 0f
        positions[1] = mCircleWidth / (mCircleWidth + mColorWidth)
        positions[2] = 1f

        mLTCircle = RadialGradient(mLineMargin, mLineMargin, mLineMargin, colors, positions, Shader.TileMode.REPEAT)
        mRTCircle = RadialGradient(w - mLineMargin, mLineMargin, mLineMargin, colors, positions, Shader.TileMode.REPEAT)
        mLBCircle = RadialGradient(mLineMargin, h - mLineMargin, mLineMargin, colors, positions, Shader.TileMode.REPEAT)
        mRBCircle = RadialGradient(w - mLineMargin, h - mLineMargin, mLineMargin, colors, positions, Shader.TileMode.REPEAT)

//        mLLinear = LinearGradient(0f, mColorWidth, h - mColorWidth, mColorWidth, startColor, endColor, Shader.TileMode.CLAMP)
        colors = IntArray(2)
        colors[0] = startColor
        colors[1] = endColor

        mLLinear = LinearGradient(mColorWidth, mColorWidth, 0f, mColorWidth, colors, null, Shader.TileMode.REPEAT)
        mTLinear = LinearGradient(mColorWidth, mColorWidth, mColorWidth, 0f, colors, null, Shader.TileMode.REPEAT)
        mRLinear = LinearGradient(w - mColorWidth, mColorWidth, w.toFloat(), mColorWidth, colors, null, Shader.TileMode.REPEAT)
        mBLinear = LinearGradient(mColorWidth, height - mColorWidth, mColorWidth, height.toFloat(), colors, null, Shader.TileMode.REPEAT)
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
//        canvas.drawCircle(mLineMargin, mLineMargin, mCircleWidth, emptyPaint)
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
}
