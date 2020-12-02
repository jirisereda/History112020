package com.history.core

import com.history.core.events.ErrorEvent


fun Throwable.toErrorEvent(): ErrorEvent = ErrorEvent("", localizedMessage ?: "")