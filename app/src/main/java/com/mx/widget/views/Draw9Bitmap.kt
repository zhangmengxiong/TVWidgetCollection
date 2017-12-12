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
    private var mLTCircle: RadialGradient? = null
    private var mRTCircle: RadialGradient? = null
    private var mLBCircle: RadialGradient? = null
    private var mRBCircle: RadialGradient? = null

    private var mLLinear: LinearGradient? = null
    private var mTLinear: LinearGradient? = null
    private var mRLinear: LinearGradient? = null
    private var mBLinear: LinearGradient? = null

    private val startColor = Color.parseColor("#FF4081")
    private val endColor = Color.parseColor("#00FF4081")
    protected var mColorWidth: Float = 40f // 绘制宽度

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

        mLTCircle = RadialGradient(mColorWidth, mColorWidth, mColorWidth, startColor, endColor, Shader.TileMode.REPEAT)
        mRTCircle = RadialGradient(w - mColorWidth, mColorWidth, mColorWidth, startColor, endColor, Shader.TileMode.REPEAT)
        mLBCircle = RadialGradient(mColorWidth, h - mColorWidth, mColorWidth, startColor, endColor, Shader.TileMode.REPEAT)
        mRBCircle = RadialGradient(w - mColorWidth, h - mColorWidth, mColorWidth, startColor, endColor, Shader.TileMode.REPEAT)

//        mLLinear = LinearGradient(0f, mColorWidth, h - mColorWidth, mColorWidth, startColor, endColor, Shader.TileMode.CLAMP)
        val array = IntArray(2)
        array[0] = startColor
        array[1] = endColor

        mLLinear = LinearGradient(mColorWidth, mColorWidth, 0f, mColorWidth, array, null, Shader.TileMode.REPEAT)
        mTLinear = LinearGradient(mColorWidth, mColorWidth, mColorWidth, 0f, array, null, Shader.TileMode.REPEAT)
        mRLinear = LinearGradient(w - mColorWidth, mColorWidth, w.toFloat(), mColorWidth, array, null, Shader.TileMode.REPEAT)
        mBLinear = LinearGradient(mColorWidth, height - mColorWidth, mColorWidth, height.toFloat(), array, null, Shader.TileMode.REPEAT)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCircle(canvas)
        drawLine(canvas)
    }

    private fun drawLine(canvas: Canvas) {
        val width = width
        val height = height

        mPaint?.shader = mLLinear
        canvas.drawRect(0f, mColorWidth, mColorWidth, height - mColorWidth, mPaint)

        mPaint?.shader = mTLinear
        canvas.drawRect(mColorWidth, 0f, width - mColorWidth, mColorWidth, mPaint)

        mPaint?.shader = mRLinear
        canvas.drawRect(width - mColorWidth, mColorWidth, width.toFloat(), height - mColorWidth, mPaint)

        mPaint?.shader = mBLinear
        canvas.drawRect(mColorWidth, height - mColorWidth, width - mColorWidth, height.toFloat(), mPaint)
    }

    private fun drawCircle(canvas: Canvas) {
        val width = width
        val height = height

        mPaint?.shader = mLTCircle
        canvas.drawCircle(mColorWidth, mColorWidth, mColorWidth, mPaint)

        mPaint?.shader = mRTCircle
        canvas.drawCircle(width - mColorWidth, mColorWidth, mColorWidth, mPaint)

        mPaint?.shader = mLBCircle
        canvas.drawCircle(mColorWidth, height - mColorWidth, mColorWidth, mPaint)

        mPaint?.shader = mRBCircle
        canvas.drawCircle(width - mColorWidth, height - mColorWidth, mColorWidth, mPaint)

        var rect = RectF(0f, 0f, mColorWidth * 2, mColorWidth * 2)
        canvas.drawArc(rect, -90f, 270f, true, emptyPaint)

        rect = RectF(width - mColorWidth * 2, 0f, width.toFloat(), mColorWidth * 2)
        canvas.drawArc(rect, 0f, 270f, true, emptyPaint)

        rect = RectF(0f, height - mColorWidth * 2, mColorWidth * 2, height.toFloat())
        canvas.drawArc(rect, 180f, 270f, true, emptyPaint)
        canvas.drawArc(rect, -90f, 90f, true, emptyPaint)

        rect = RectF(width - mColorWidth * 2, height - mColorWidth * 2, width.toFloat(), height.toFloat())
        canvas.drawArc(rect, 90f, 270f, true, emptyPaint)
        canvas.drawArc(rect, -90f, 0f, true, emptyPaint)
    }
}
