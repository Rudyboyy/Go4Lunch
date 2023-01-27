package com.rudy.go4lunch;

import static org.junit.Assert.assertEquals;

import com.google.android.gms.tasks.Task;
import com.rudy.go4lunch.manager.UserManager;
import com.rudy.go4lunch.model.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

public class UsersUnitTest {

    @RunWith(JUnit4.class)
    public class UserUnitTest {
        @Test
        public void getUsersData() {
            UserManager userManager = Mockito.mock(UserManager.class);
            Task task = Mockito.mock(Task.class);
            User user = new User();

            Mockito.when(userManager.getUserData()).thenReturn(task);
            Mockito.when(task.getResult()).thenReturn(user);

            assertEquals(user, userManager.getUserData().getResult());
        }
    }
}
