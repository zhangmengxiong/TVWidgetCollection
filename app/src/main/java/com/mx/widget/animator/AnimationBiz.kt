package com.mx.widget.animator

import android.view.animation.Animation
import android.view.animation.ScaleAnimation


/**
 * Created by ZMX on 2017/12/13.
 */

object AnimationBiz {
    fun createIncreaseScaleAnimation(scale: Float, duration: Long): ScaleAnimation {
        val animation = ScaleAnimation(1.0f, scale, 1.0f, scale,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        animation.duration = duration//设置动画持续时间
        animation.fillAfter = true
        animation.fillBefore = false
        return animation
    }

    fun createDecreaseScaleAnimation(scale: Float, duration: Long): ScaleAnimation {
        val animation = ScaleAnimation(scale, 1.0f, scale, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        animation.duration = duration//设置动画持续时间
        animation.fillBefore = true
        animation.fillAfter = false
        return animation
    }
}
