/*
 * Marks an icon that comes from an icon pack, or is drawn as-is by Owl's unthemed modes.
 * Such icons are drawn without the default adaptive wrapper, so no backplate or system
 * mask is added on top of the artwork (the same as Nomina).
 */
package com.android.launcher3.icons

import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableWrapper

class OwlPackDrawable(inner: Drawable) : DrawableWrapper(inner)
