package com.guesstheimpostor

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * Mueve una vista 'highlight' desde su posición actual hasta la posición de targetView.
 */
object HighlightAnimator {
    fun animateTo(highlight: View, targetView: View, durationMs: Long = 220L) {
        val targetX = targetView.x
        val targetY = targetView.y

        val animX = ObjectAnimator.ofFloat(highlight, "x", highlight.x, targetX)
        val animY = ObjectAnimator.ofFloat(highlight, "y", highlight.y, targetY)

        AnimatorSet().apply {
            playTogether(animX, animY)
            duration = durationMs
            interpolator = DecelerateInterpolator()
            start()
        }
    }
}
