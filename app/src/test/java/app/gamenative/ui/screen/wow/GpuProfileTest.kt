package app.gamenative.ui.screen.wow

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GpuProfileTest {
    @Test
    fun everyAdreno8xxGetsTheAdreno8xxProfile() {
        assertTrue(isAdreno8xx("Adreno (TM) 825", "SM8735", "qcom"))
        assertTrue(isAdreno8xx("", "SM8735", "qcom"))
        assertTrue(isAdreno8xx("", "SM8845", "qcom"))
        assertTrue(isAdreno8xx("Adreno (TM) 830", "", "qcom"))
        assertTrue(isAdreno8xx("", "SM8850", "qcom"))
        assertTrue(isAdreno8xx("", "", "sun"))
    }

    @Test
    fun adreno7xxIsNot8xx() {
        assertFalse(isAdreno8xx("Adreno (TM) 740", "SM8550", "kalama"))
        assertFalse(isAdreno8xx("Adreno (TM) 750", "SM8650", "pineapple"))
        assertFalse(isAdreno8xx("Adreno (TM) 735", "SM7675", "qcom"))
    }
}
