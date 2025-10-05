package com.example.banking;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.IdlingRegistry;

import org.junit.Rule;
import org.junit.Test;

public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);
    @Test
    public void validLoginTest() {
        // Enter Username
        Espresso.onView(withId(R.id.editUsernameText))
                .perform(ViewActions.typeText("sa-1"));

        // Enter Password
        Espresso.onView(withId(R.id.editPasswordText))
                .perform(ViewActions.typeText("sa-1"));

        // Click Login Button
        Espresso.onView(withId(R.id.loginButton))
                .perform(ViewActions.click());

        // Set up an ElapsedTimeIdlingResource or use your custom IdlingResource
        ElapsedTimeIdlingResource idlingResource = new ElapsedTimeIdlingResource(1000);

        // Register the IdlingResource with Espresso
        IdlingRegistry.getInstance().register(idlingResource);

        // Validate Customer Name
        Espresso.onView(withId(R.id.displayCustomerNameTextView))
                .check(matches(withText("Hello, John")));

        IdlingRegistry.getInstance().unregister(idlingResource);
    }
    @Test
    public void invalidUserLoginTest() {
        // Enter Invalid Username
        Espresso.onView(withId(R.id.editUsernameText))
                .perform(ViewActions.typeText("bob"));

        // Enter Password
        Espresso.onView(withId(R.id.editPasswordText))
                .perform(ViewActions.typeText("password"));

        // Click Login Button
        Espresso.onView(withId(R.id.loginButton))
                .perform(ViewActions.click());

        // Validate Login Error Message
        Espresso.onView(withId(R.id.loginErrorMessageTextView))
                .check(matches(withText("User not found")));
    }
    @Test
    public void invalidCredentialsTest() {
        // Enter Username
        Espresso.onView(withId(R.id.editUsernameText))
                .perform(ViewActions.typeText("sa-1"));

        // Enter Invalid Password
        Espresso.onView(withId(R.id.editPasswordText))
                .perform(ViewActions.typeText("password"));

        // Click Login Button
        Espresso.onView(withId(R.id.loginButton))
                .perform(ViewActions.click());

        ElapsedTimeIdlingResource idlingResource = new ElapsedTimeIdlingResource(1000);

        // Register the IdlingResource with Espresso
        IdlingRegistry.getInstance().register(idlingResource);

        // Validate Login Error Message
        Espresso.onView(withId(R.id.loginErrorMessageTextView))
                .check(matches(withText("Invalid Credentials")));

        IdlingRegistry.getInstance().unregister(idlingResource);
    }
}
