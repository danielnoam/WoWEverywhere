package app.gamenative.wow.legacy

import app.gamenative.data.TouchGestureConfig
import com.winlator.container.Container

/**
 * Touch for the private server clients' menus, on top of the controller. Touchscreen mode taps where the finger is,
 * so bags, the auction house, quest text and talents work by hand. Two fingers dragged scroll the list under them,
 * holding a finger right-clicks (use or equip an item), and the cursor stays visible for tooltips.
 * Applied once, so turning touchscreen mode off or changing the gestures in the in-game menu sticks.
 */
object TouchMenus {
    const val EXTRA = "touchMenus"
    private const val VERSION = "1"
    private const val LONG_PRESS_MS = 450

    val gestures = TouchGestureConfig(
        twoFingerDragAction = TouchGestureConfig.PAN_SCROLL_WHEEL,
        longPressEnabled = true,
        longPressAction = TouchGestureConfig.ACTION_RIGHT_CLICK,
        longPressMouseBehavior = TouchGestureConfig.MOUSE_BEHAVIOR_CLICK,
        longPressDelay = LONG_PRESS_MS,
        showCursorInTouchscreenMode = true,
    )

    fun applyOnce(container: Container) {
        if (container.getExtra(EXTRA) == VERSION) return
        container.setTouchscreenMode(true)
        container.setGestureConfig(gestures.toJson())
        container.putExtra(EXTRA, VERSION)
    }
}
