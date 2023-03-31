package com.rudy.go4lunch.ui;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.rudy.go4lunch.UtilsTest.childAtPosition;
import static com.rudy.go4lunch.UtilsTest.signWithTestUserAccount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

import com.rudy.go4lunch.R;
import com.rudy.go4lunch.RepeatRule;
import com.rudy.go4lunch.manager.UserManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;


@RunWith(AndroidJUnit4.class)
public class LogUserTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION");

    /**
     * The RepeatRule is used to repeat the test up to twice if it fails due to the absence of current user's connection.
     * The goal is to ensure that the test is executed with a logged-in user to ensure its validity.
     * The rule works by repeating the test a second time if the first attempt failed due to the lack of user's connection.
     */
    @Rule
    public RepeatRule repeatRule = new RepeatRule(2);

    UiDevice mDevice;
    UserManager userManager;
    private int itemCount = 0;
    private int itemCountAfterSent = 0;

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        userManager = UserManager.getInstance();
        if (!userManager.isCurrentUserLogged()) {
            signWithTestUserAccount();
        }
    }

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.rudy.go4lunch", appContext.getPackageName());
    }

    @Test
    public void loginUserTest() {
        onView(withId(R.id.toolbar)).check(ViewAssertions.matches(isDisplayed()));
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Ouvrir le menu de navigation"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());
        onView(withId(R.id.email)).check(matches(withText(userManager.getCurrentUser().getEmail())));
    }

    @Test
    public void logOutUserTest() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Ouvrir le menu de navigation"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());
        onView(withId(R.id.activity_main_drawer_logout)).perform(click());
        onView(withId(R.id.googleButton)).check(matches(isDisplayed()));
    }

    @Test
    public void chatTest() {
        onView(withId(R.id.navigation_workmate)).perform(click());
        onView(withId(R.id.recyclerview)).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.chatRecyclerView)).check((view, noViewFoundException) -> {
            RecyclerView recyclerView = (RecyclerView) view;
            itemCount = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
        });
        onView(withId(R.id.chatEditText)).perform(replaceText("Text"), closeSoftKeyboard());
        onView(withId(R.id.sendButton)).perform(click());
        onView(withId(R.id.chatRecyclerView)).check((view, noViewFoundException) -> {
            RecyclerView recyclerView = (RecyclerView) view;
            itemCountAfterSent = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
        });
        Assert.assertEquals(itemCountAfterSent, itemCount + 1);
    }

    @Test
    public void bookRestaurantTest() {

        onView(withId(R.id.navigation_restaurant)).perform(click());
        onView(withId(R.id.recyclerview)).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.floatingActionButton)).perform(click());
        pressBack();

        onView(withId(R.id.main_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.activity_main_drawer_lunch)).perform(click());
        onView(withId(R.id.detailRestaurantActivity)).check(matches(isDisplayed()));
        onView(withId(R.id.floatingActionButton)).perform(click());
        pressBack();

        onView(withId(R.id.main_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.activity_main_drawer_lunch)).perform(click());
        ViewInteraction snackbarView = onView(withId(com.google.android.material.R.id.snackbar_text));
        snackbarView.check(matches(isDisplayed()));
    }

    @Test
    public void favoriteRestaurantTest() {
        onView(withId(R.id.navigation_restaurant)).perform(click());
        onView(withId(R.id.recyclerview)).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.like)).check(matches(withText(R.string.like)));
        onView(withId(R.id.likeButton)).perform(click());
        onView(withId(R.id.like)).check(matches(withText(R.string.unlike)));
        pressBack();

        onView(withId(R.id.navigation_restaurant)).perform(click());
        onView(withId(R.id.recyclerview)).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.like)).check(matches(withText(R.string.unlike)));
        onView(withId(R.id.likeButton)).perform(click());
        onView(withId(R.id.like)).check(matches(withText(R.string.like)));
    }

    @Test
    public void shouldShowDetailRestaurant() {
        onView(withId(R.id.navigation_restaurant)).perform(click());
        onView(withId(R.id.recyclerview)).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.detailRestaurantActivity)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldShowChat() {
        onView(withId(R.id.navigation_workmate)).perform(click());
        onView(withId(R.id.recyclerview)).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.chatActivity)).check(ViewAssertions.matches(isDisplayed()));
    }
}
