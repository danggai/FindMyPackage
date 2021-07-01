package danggai.app.parcelwhere

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import danggai.app.parcelwhere.ui.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TrackAddActivityUiTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun fakeItemAddTest() {
        val sampleTrackId = "123412341234"

        Espresso.onView(ViewMatchers.withId(R.id.btn_add)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText("CJ대한통운")).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.tv_item_name)).perform(ViewActions.typeText("TEST ITEM"))
        Espresso.onView(ViewMatchers.withId(R.id.tv_track_id)).perform(ViewActions.typeText(sampleTrackId))

        Espresso.onView(ViewMatchers.withText(R.string.track_add)).perform(ViewActions.click())

        Thread.sleep(100)

        Espresso.onView(ViewMatchers.withText(R.string.confirm)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText(sampleTrackId))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun realItemAddTest() {
        val sampleTrackId = "641168771133"

        Espresso.onView(ViewMatchers.withId(R.id.btn_add)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText("CJ대한통운")).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.tv_item_name)).perform(ViewActions.typeText("gpemtpt"))
        Espresso.onView(ViewMatchers.withId(R.id.tv_track_id)).perform(ViewActions.typeText(sampleTrackId))

        Espresso.onView(ViewMatchers.withText(R.string.track_add)).perform(ViewActions.click())

        Thread.sleep(100)

        Espresso.onView(ViewMatchers.withText(sampleTrackId))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}