package com.rudy.go4lunch;

import androidx.annotation.NonNull;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class RepeatRule implements TestRule {
    private int retryCount;

    public RepeatRule(int retryCount) {
        this.retryCount = retryCount;
    }

    public Statement apply(@NonNull Statement statement, @NonNull Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Throwable throwable = null;
                for (int i = 0; i < retryCount; i++) {
                    try {
                        statement.evaluate();
                        return;
                    } catch (Throwable t) {
                        throwable = t;
                        System.err.println(description.getDisplayName() + ": run " + (i + 1) + " failed");
                    }
                }
                System.err.println(description.getDisplayName() + ": giving up after " + retryCount + " failures");
                throw throwable;
            }
        };
    }
}

