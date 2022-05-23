package cz.vratislavjindra.alzacasestudy.core.utils

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.grid.LazyGridState

// Check if scroll fraction is slightly over zero to overcome float precision issues.
private const val MIN_SCROLL_OFFSET = 0.01f

val LazyGridState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > MIN_SCROLL_OFFSET

val ScrollState.isScrolled: Boolean
    get() = value > MIN_SCROLL_OFFSET