package com.rudy.go4lunch;

public class StringUtils {

    public static String getString(int resId) {
        switch (resId) {
            case R.string.closed_open_at:
                return String.valueOf(R.string.closed_open_at);
            case R.string.open_until:
                return String.valueOf(R.string.open_until);
        }
        return "getString() failed";
    }
}
