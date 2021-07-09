package danggai.app.parcelwhere

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import danggai.app.parcelwhere.ui.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityUiTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun emptyRecyclerViewTest(){
        Espresso.onView(ViewMatchers.withText(R.string.msg_empty_track_list))
            .check(matches(ViewMatchers.isDisplayed()))
    }


    @Test
    fun tutorialTest() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_tutorial))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText(R.string.msg_tutorial_1))
            .check(matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.btn_next))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText(R.string.msg_tutorial_2))
            .check(matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.btn_next))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText(R.string.msg_tutorial_3))
            .check(matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.btn_next))
            .perform(ViewActions.click())
    }
}