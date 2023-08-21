package me.cniekirk.trackbuddy.navigation

import android.graphics.Path
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.unit.IntOffset
import androidx.core.view.animation.PathInterpolatorCompat

private const val OFF_SCREEN_ALPHA = 0f
private const val SCREEN_WIDTH_ANIM_DIVISOR = 5
private const val EMPHASISED_DURATION = 500
private const val POP_DURATION = 300
private const val ANIM_DELAY = 0

// Default activity animation interpolator: fast_out_v_slow_in.xml
private val path = Path().apply {
    moveTo(0f, 0f)
    cubicTo(0.05f, 0f, 0.133333f, 0.06f, 0.166666f, 0.4f)
    cubicTo(0.208333f, 0.82f, 0.25f, 1f, 1f, 1f)
}

private val easing = Easing {
    PathInterpolatorCompat.create(path).getInterpolation(it)
}

val emphasisedIntSpec = tween<IntOffset>(EMPHASISED_DURATION, ANIM_DELAY, easing)
val emphasisedFloatSpec = tween<Float>(EMPHASISED_DURATION, ANIM_DELAY, easing)

val emphasisedPopIntSpec = tween<IntOffset>(POP_DURATION, ANIM_DELAY, easing)
val emphasisedPopFloatSpec = tween<Float>(POP_DURATION, ANIM_DELAY, easing)


val enterAnimation = {
    slideInHorizontally(
        animationSpec = emphasisedIntSpec,
        initialOffsetX = { it / SCREEN_WIDTH_ANIM_DIVISOR }
    ) + fadeIn(emphasisedFloatSpec, OFF_SCREEN_ALPHA)
}

val exitAnimation = {
    slideOutHorizontally(
        animationSpec = emphasisedIntSpec,
        targetOffsetX = { -(it / SCREEN_WIDTH_ANIM_DIVISOR) }
    ) + fadeOut(emphasisedFloatSpec, OFF_SCREEN_ALPHA)
}

val popEnterAnimation = {
    slideInHorizontally(
        animationSpec = emphasisedPopIntSpec,
        initialOffsetX = { -(it / SCREEN_WIDTH_ANIM_DIVISOR) }
    ) + fadeIn(emphasisedPopFloatSpec, OFF_SCREEN_ALPHA)
}

val popExitAnimation = {
    slideOutHorizontally(
        animationSpec = emphasisedPopIntSpec,
        targetOffsetX = { it / SCREEN_WIDTH_ANIM_DIVISOR }
    ) + fadeOut(emphasisedPopFloatSpec, OFF_SCREEN_ALPHA)
}