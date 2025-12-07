package com.guesstheimpostor

import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.accessibility.AccessibilityEvent
import androidx.core.view.ViewCompat

/**
 * Adjuntar a la vista que muestra la palabra revelada (revealView) y a la vista que representa
 * el estado oculto (hiddenView). Mientras el usuario mantiene pulsado en revealView, se muestra
 * la palabra; al soltar se oculta.
 */
class RevealTouchHelper(
    private val revealView: View,
    private val hiddenView: View,
    private val fadeDuration: Long = 160L
) {
    private val handler = Handler(Looper.getMainLooper())

    fun attach() {
        revealView.alpha = 0f
        revealView.visibility = View.INVISIBLE
        hiddenView.alpha = 1f
        hiddenView.visibility = View.VISIBLE

        revealView.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    showReveal()
                    sendAccessibilityEvent(revealView, "Revelando palabra")
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                    hideReveal()
                    true
                }
                else -> false
            }
        }
    }

    private fun showReveal() {
        revealView.animate().cancel()
        hiddenView.animate().cancel()
        revealView.visibility = View.VISIBLE
        revealView.animate().alpha(1f).setDuration(fadeDuration).start()
        hiddenView.animate().alpha(0f).setDuration(fadeDuration).withEndAction {
            hiddenView.visibility = View.INVISIBLE
        }.start()
    }

    private fun hideReveal() {
        revealView.animate().cancel()
        hiddenView.animate().cancel()
        hiddenView.visibility = View.VISIBLE
        hiddenView.animate().alpha(1f).setDuration(fadeDuration).start()
        revealView.animate().alpha(0f).setDuration(fadeDuration).withEndAction {
            revealView.visibility = View.INVISIBLE
        }.start()
    }

    private fun sendAccessibilityEvent(v: View, message: String) {
        v.contentDescription = message
        ViewCompat.requestSendAccessibilityEvent(v, v, AccessibilityEvent.obtain().apply {
            eventType = AccessibilityEvent.TYPE_ANNOUNCEMENT
            text.add(message)
        })
    }
}
