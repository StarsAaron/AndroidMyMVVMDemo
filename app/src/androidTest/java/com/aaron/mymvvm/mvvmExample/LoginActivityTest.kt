package com.aaron.mymvvm.mvvmExample

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.aaron.mymvvm.R
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 作者：Aaron
 * 时间：2019/10/25:14:25
 * 邮箱：
 * 说明：
 */
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @Rule
    val mActivityRule = ActivityTestRule<LoginActivity>(LoginActivity::class.java)

    @Test
    fun useAppContext() {
        // 1.首先，找到ID为editText的view，输入Peter，然后关闭键盘；
        onView(withId(R.id.et_password)).perform(typeText("123"), closeSoftKeyboard())
        // 2.接下来，点击Say hello!的View，我们没有在布局的XML中为这个Button设置id，因此，通过搜索它上面的文字来找到它；
        onView(withText("登录")).perform(click())
        // 3.最后，将TextView上的文本同预期结果对比，如果一致则测试通过
        val expectedText = "Hello, petter!"
        onView(withId(R.id.textView)).check(matches(withText(expectedText)))

        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.aaron.mymvvm", appContext.packageName)
    }

    @Test
    fun testEvent() {
        val scenario = launchActivity<LoginActivity>()
        scenario.moveToState(State.CREATED)
    }

    @Test
    fun testEvent2() {
        val scenario = launchActivity<MyActivity>()
        scenario.onActivity { activity ->
            startActivity(Intent(activity, MyOtherActivity::class.java))
            activity.handleSwipeToRefresh()
        }
        val originalActivityState = scenario.state
    }

    @Test
    fun testEvent3() {
        val scenario = launchActivity<MyActivity>()
        scenario.recreate()
    }

    @Test
    fun testResult() {
        val scenario = launchActivity<MyActivity>()
        onView(withId(R.id.finish_button)).perform(click())
        // Activity under test is now finished.
        val resultCode = scenario.result.resultCode
        val resultData = scenario.result.resultData
    }

}