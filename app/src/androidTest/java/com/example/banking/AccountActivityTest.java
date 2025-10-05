package com.example.banking;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.IsAnything.anything;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AccountActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
        Espresso.onView(withId(R.id.editUsernameText))
                .perform(ViewActions.typeText("sa-1"));

        // Enter Password
        Espresso.onView(withId(R.id.editPasswordText))
                .perform(ViewActions.typeText("sa-1"));

        // Click Login Button
        Espresso.onView(withId(R.id.loginButton))
                .perform(click());

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
    public void ViewAccountsTest() {
        String[] accountTypes = new String[]{"Checking", "Savings"};

        for(int pos = 0; pos < accountTypes.length; pos++) {
            Espresso.onData(anything())
                    .inAdapterView(withId(R.id.accountsListView))
                    .atPosition(pos)
                    .onChildView(withId(R.id.accountTypeTextView))
                    .check(matches(withText(startsWith(accountTypes[pos]))));
        }
    }
}
