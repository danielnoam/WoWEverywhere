package app.gamenative.wow.legacy

import app.gamenative.data.TouchGestureConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TouchMenusTest {

    @Test
    fun gesturesSurviveTheContainerJson() {
        val saved = TouchGestureConfig.fromJson(TouchMenus.gestures.toJson())
        assertEquals(TouchGestureConfig.PAN_SCROLL_WHEEL, saved.twoFingerDragAction)
        assertTrue(saved.longPressEnabled)
        assertEquals(TouchGestureConfig.ACTION_RIGHT_CLICK, saved.longPressAction)
        assertEquals(TouchGestureConfig.MOUSE_BEHAVIOR_CLICK, saved.longPressMouseBehavior)
        assertTrue(saved.showCursorInTouchscreenMode)
        assertTrue(saved.tapEnabled)
    }

    @Test
    fun scrollWheelIsOfferedInTheGestureSettings() {
        assertTrue(TouchGestureConfig.PAN_SCROLL_WHEEL in TouchGestureConfig.PAN_ACTIONS)
    }
}
