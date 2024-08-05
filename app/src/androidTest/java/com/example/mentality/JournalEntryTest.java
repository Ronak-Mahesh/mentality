package com.example.mentality;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;

import org.junit.Before;
import org.junit.Test;

public class JournalEntryTest
{

    private final ActivityScenario<JournalEntry> scenario = ActivityScenario.launch(JournalEntry.class);
    @Before
    public void setUp() {}

    @Test
    public void testUploadButtonClicks()
    {
        onView(withId(R.id.uploadButton)).perform(ViewActions.click());
    }

    @Test
    public void testSaveButtonClicks()
    {
        onView(withId(R.id.saveButton)).perform(ViewActions.click());
    }

    @Test
    public void testTextBoxClicks()
    {
        onView(withId(R.id.JournalText)).perform(ViewActions.click());
    }

    @Test
    public void testTextBoxType()
    {
        onView(withId(R.id.JournalText)).perform(ViewActions.typeText("Hello"));
        onView(withId(R.id.JournalText)).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.saveButton)).perform(ViewActions.click());

        scenario.recreate();

        onView(withId(R.id.JournalText)).check(matches(withText(containsString("Hello"))));
    }
}
