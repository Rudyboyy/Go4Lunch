package com.rudy.go4lunch.ui;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.rudy.go4lunch.BuildConfig;
import com.rudy.go4lunch.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest2 {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION");

    UiDevice mDevice;
    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void loginUser() throws UiObjectNotFoundException {
//        mDevice.waitForWindowUpdate(BuildConfig.APPLICATION_ID, 1000);
        onView(withId(R.id.fcbButton)).perform(click());//.check(ViewAssertions.matches(isDisplayed()));
//       todo test ca    un truc du genre      Thread.sleep(7000); + faire les methode utils

//        mDevice.waitForWindowUpdate(BuildConfig.APPLICATION_ID, 1000);
//        UiObject emailText = mDevice.findObject(new UiSelector().text("rudylepretre@gmail.com"));
//        emailText.click();
//        mDevice.waitForWindowUpdate(BuildConfig.APPLICATION_ID, 1000);
    }

    @Test
    public void mainActivityTest2() throws UiObjectNotFoundException {
//        ViewInteraction materialButton = onView(
//                allOf(withId(R.id.googleButton), withText("Sign in with Google    "),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                5),
//                        isDisplayed()));
//        materialButton.perform(click());

        mDevice.waitForWindowUpdate(BuildConfig.APPLICATION_ID, 1000);
        onView(withId(R.id.googleButton)).perform(click());

        mDevice.waitForWindowUpdate(BuildConfig.APPLICATION_ID, 1000);
        UiObject emailText = mDevice.findObject(new UiSelector().text("rudylepretre@gmail.com"));
        emailText.click();
        mDevice.waitForWindowUpdate(BuildConfig.APPLICATION_ID, 1000);

        ViewInteraction textView = onView(
                allOf(withText("I'm hungry !"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        textView.check(matches(withText("I'm hungry !")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
