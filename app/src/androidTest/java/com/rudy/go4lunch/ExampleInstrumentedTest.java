package com.rudy.go4lunch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.rudy.go4lunch.ui.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule runtimePermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    protected final Context context = ApplicationProvider.getApplicationContext();
    UiDevice mDevice;

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Intent intent = new Intent();
        intent.setClass(InstrumentationRegistry.getInstrumentation().getTargetContext(), MainActivity.class);
        InstrumentationRegistry.getInstrumentation().startActivitySync(intent);
    }



    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.rudy.go4lunch", appContext.getPackageName());
    }

//    login test
    @Test
    public void login() throws UiObjectNotFoundException {
        onView(withId(R.id.googleButton)).perform(click());
//        lVEnpdd4JnbMm061CTc8gR8Rbb33   id compte google login in firebase
        UiObject accountList = mDevice.findObject(new UiSelector().resourceId("lVEnpdd4JnbMm061CTc8gR8Rbb33"));
        UiObject firstAccount = accountList.getChild(new UiSelector().index(0));
        firstAccount.click();
    }

    @Test
    public void loginUser() throws UiObjectNotFoundException {
        mDevice.waitForWindowUpdate(BuildConfig.APPLICATION_ID, 1000);
        onView(withId(R.id.googleButton)).perform(click());

        mDevice.waitForWindowUpdate(BuildConfig.APPLICATION_ID, 1000);
        UiObject emailText = mDevice.findObject(new UiSelector().text("rudylepretre@gmail.com"));
        emailText.click();
        mDevice.waitForWindowUpdate(BuildConfig.APPLICATION_ID, 1000);
    }

    //test les viewmodels avec mockito

//    test detail restaurant view
    @Test
    public void shouldShowDetailRestaurant() {
        onView(withId(R.id.navigation_restaurant)).perform(click());
        onView(withId(R.id.recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.detailRestaurantActivity)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void  shouldShowChat() {
        onView(withId(R.id.navigation_workmate)).perform(click());
        onView(withId(R.id.chatRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.chatActivity)).check(ViewAssertions.matches(isDisplayed()));
    }

//    voir retroMock
}