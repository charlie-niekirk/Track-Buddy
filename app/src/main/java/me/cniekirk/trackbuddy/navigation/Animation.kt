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

// Default activity animation interpolator: fast_out_v_slow_in.xml
private val path = Path().apply {
    moveTo(0f, 0f)
    cubicTo(0.05f, 0f, 0.133333f, 0.06f, 0.166666f, 0.4f)
    cubicTo(0.208333f, 0.82f, 0.25f, 1f, 1f, 1f)
}

private val easing = Easing {
    PathInterpolatorCompat.create(path).getInterpolation(it)
}

private val emphasisedIntSpec = tween<IntOffset>(500, 0, easing)
private val emphasisedFloatSpec = tween<Float>(500, 0, easing)

private val emphasisedPopIntSpec = tween<IntOffset>(300, 0, easing)
private val emphasisedPopFloatSpec = tween<Float>(300, 0, easing)


val enterAnimation = {
    slideInHorizontally(
        animationSpec = emphasisedIntSpec,
        initialOffsetX = { it / 5 }
    ) + fadeIn(emphasisedFloatSpec, 0f)
}

val exitAnimation = {
    slideOutHorizontally(
        animationSpec = emphasisedIntSpec,
        targetOffsetX = { -(it / 5) }
    ) + fadeOut(emphasisedFloatSpec, 0f)
}

val popEnterAnimation = {
    slideInHorizontally(
        animationSpec = emphasisedPopIntSpec,
        initialOffsetX = { -(it / 5) }
    ) + fadeIn(emphasisedPopFloatSpec, 0f)
}

val popExitAnimation = {
    slideOutHorizontally(
        animationSpec = emphasisedPopIntSpec,
        targetOffsetX = { it / 5 }
    ) + fadeOut(emphasisedPopFloatSpec, 0f)
}