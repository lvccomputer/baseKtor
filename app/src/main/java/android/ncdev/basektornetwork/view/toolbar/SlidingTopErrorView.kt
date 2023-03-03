
package android.ncdev.basektornetwork.view.toolbar

import android.animation.Animator
import android.content.Context
import android.ncdev.basektornetwork.databinding.CustomSlidingTopErrorBinding
import android.ncdev.common.utils.viewbinding.viewBinding
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import java.util.*

class SlidingTopErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var isErrorShown = false

    private val errorLayoutAboveScreenStartPosition: Float
        get() = -height.toFloat() + -marginBottom.toFloat()

    private val maxDurationShownHandler: Handler by lazy {
        Handler(Looper.myLooper()!!)
    }

    private val privateMaxDurationRunnable = Runnable {
        dismissCurrentError()
    }

    private val titleDescriptionPairErrorQueue = ArrayDeque<Pair<String, CharSequence>>()

    private val binding = viewBinding(CustomSlidingTopErrorBinding::inflate)

    private fun dismissCurrentError() {
        if (titleDescriptionPairErrorQueue.size > 0) {
            maxDurationShownHandler.removeCallbacksAndMessages(null)

            titleDescriptionPairErrorQueue.removeFirst()

            moveViewToAboveOfScreen()
        }
    }

    fun addErrorMessage(title: String, description: CharSequence) {
        titleDescriptionPairErrorQueue.add(Pair(title, description))

        if (isErrorShown.not()) {
            showNextError()
        }
    }

    private fun showNextError() {
        if (titleDescriptionPairErrorQueue.size > 0) {
            isErrorShown = true

            val (title, description) = titleDescriptionPairErrorQueue.first
//            binding.slidingTopErrorTitleTextView.text = title
            binding.slidingTopErrorDescriptionTextView.text = description

            binding.slidingTopErrorLayout.doOnPreDraw {
                it.translationY = errorLayoutAboveScreenStartPosition
                moveViewToOriginalPosition()
                maxDurationShownHandler.postDelayed(privateMaxDurationRunnable, MAX_SHOWN_DURATION)
            }
        } else {
            isErrorShown = false
        }
    }

    private fun moveViewToOriginalPosition() {
        with(binding.slidingTopErrorLayout) {
            animate()
                .y(marginTop.toFloat())
                .setInterpolator(DecelerateInterpolator())
                .setListener(object : Animator.AnimatorListener {

                    override fun onAnimationRepeat(animation: Animator) {
                        // nothing to do
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        // nothing to do
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        y = errorLayoutAboveScreenStartPosition
                    }

                    override fun onAnimationStart(animation: Animator) {
                        // nothing to do
                    }
                })
                .setDuration(IN_ANIMATION_DURATION)
                .withLayer()
                .start()
        }
    }

    private fun moveViewToAboveOfScreen() {
        with(binding.slidingTopErrorLayout) {
            animate()
                .y(errorLayoutAboveScreenStartPosition)
                .setInterpolator(AccelerateInterpolator())
                .setDuration(OUT_ANIMATION_DURATION)
                .withLayer()
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator) {
                        // nothing to do
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        Handler(Looper.myLooper()!!).postDelayed({ showNextError() }, NEXT_ERROR_DELAY)
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        y = errorLayoutAboveScreenStartPosition
                    }

                    override fun onAnimationStart(animation: Animator) {
                        // nothing to do
                    }
                })
                .start()
        }
    }

    companion object {
        private const val MAX_SHOWN_DURATION = 4000L
        private const val OUT_ANIMATION_DURATION = 600L
        private const val IN_ANIMATION_DURATION = 800L
        private const val NEXT_ERROR_DELAY = 500L
    }
}
