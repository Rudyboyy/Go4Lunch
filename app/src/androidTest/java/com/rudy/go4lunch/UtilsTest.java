package com.rudy.go4lunch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;

import com.google.firebase.auth.FirebaseAuth;

import junit.framework.AssertionFailedError;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class UtilsTest {

    public static void signWithTestUserAccount() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword("rudyboys27@gmail.com", "go4lunch")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.v("Connexion", "réussi");
                    } else {
                        Log.v("Connexion", "echec");
                    }
                });
    }

    public static Matcher<View> childAtPosition(
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

    public static ViewAssertion waitUntilVisible(final Matcher<View> viewMatcher, final long timeout) {
        return (view, noViewFoundException) -> {
            final CountDownLatch latch = new CountDownLatch(1);
            final AtomicBoolean visible = new AtomicBoolean(false);
            new Thread(() -> {
                long startTime = System.currentTimeMillis();
                long elapsedTime = 0L;
                while (elapsedTime < timeout) {
                    try {
                        ViewInteraction viewInteraction = onView(viewMatcher);
                        viewInteraction.check(matches(isDisplayed()));
                        visible.set(true);
                        break;
                    } catch (AssertionFailedError | NoMatchingViewException e) {
                    }
                    elapsedTime = System.currentTimeMillis() - startTime;
                }
                latch.countDown();
            }).start();
            try {
                latch.await();
                if (!visible.get()) {
                    throw new AssertionFailedError("View not visible within timeout");
                }
            } catch (InterruptedException e) {
                throw new AssertionFailedError("Thread interrupted");
            }
        };
    }
}

