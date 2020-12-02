package com.history.core.events

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavArgs
import androidx.navigation.NavDirections

object EmptyArgs : NavArgs

interface Event

object EmptyEvent : Event

data class NavigationEvent(@IdRes val action: Int, val args: Bundle? = null) : Event

fun NavDirections.toNavEvent(): NavigationEvent = NavigationEvent(actionId, arguments)

data class ErrorEvent(val title: String, val message: String): Event

object RetryEvent : Event
object BackEvent : Event

data class ScrollEvent(val positionX: Int, val positionY: Int) : Event

data class OpenUrlEvent(val url: String) : Event

data class ApiErrorEvent(
    val message: String
) : Event