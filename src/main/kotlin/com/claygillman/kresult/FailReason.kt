package com.claygillman.kresult

import java.lang.Exception

/**
 * A FailReason encapsulates human-readable data that conveys why a task fails.
 *
 * The [friendlyMessage] property is intended to convey general failure data that doesn't contain internal
 * information. This property can be used to determine what went wrong from a end-user perspective.
 *
 * The [exception] property is for data from an internal perspective of the system. This is optional because
 * failures can often be explained by a simple message, and don't need to capture stack traces.
 *
 * If the issue can be narrowed down by a single message, the [friendlyMessage] should be the only property used.
 */
class FailReason(val friendlyMessage: String? = null, val exception: Exception? = null)