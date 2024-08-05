package com.example.mentality;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import android.view.View;
import android.widget.SeekBar;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DailyMoodTest
{

    public static ViewAction setProgress(final int progress)
    {
        return new ViewAction()
        {
            @Override
            public void perform(UiController uiController, View view)
            {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setProgress(progress);
            }
            @Override
            public String getDescription()
            {
                return "";
            }
            @Override
            public Matcher<View> getConstraints()
            {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
    }

    @Before
    public void setUp()
    {
        ActivityScenario<DailyMood> scenario = ActivityScenario.launch(DailyMood.class);
    }

    @After
    public void tearDown() {}

    // Test the plus button increments the glasses drunk
    @Test
    public void testPlusButtonIncrements()
    {
        onView(withId(R.id.plusButton))
                .perform(ViewActions.click());

        // Check if a TextView with a specific text is displayed
        onView(withId(R.id.glassesDrunkText))
                .check(ViewAssertions.matches(withText("glasses drunk:1")));
    }

    // Test the minus button will not decrement below 0
    @Test
    public void testMinusButtonEdgeCase0()
    {
        onView(withId(R.id.minusButton))
                .perform(ViewActions.click());

        // Check if a TextView with a specific text is displayed
        onView(withId(R.id.glassesDrunkText))
                .check(ViewAssertions.matches(withText("glasses drunk:0")));
    }

    // Test the minus button decrements the glasses drunk
    @Test
    public void testMinusButtonDecrements()
    {
        onView(withId(R.id.plusButton))
                .perform(click());

        // Check if a TextView with a specific text is displayed
        onView(withId(R.id.glassesDrunkText))
                .check(ViewAssertions.matches(withText("glasses drunk:1")));

        onView(withId(R.id.minusButton))
                .perform(ViewActions.click());

        // Check if a TextView with a specific text is displayed
        onView(withId(R.id.glassesDrunkText))
                .check(ViewAssertions.matches(ViewMatchers.withText("glasses drunk:0")));
    }

    @Test
    public void testWeatherSpinnerCold()
    {
        String selectionText = "cold";
        onView(withId(R.id.todaysWeatherSpinner)).perform(ViewActions.click());
        onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(ViewActions.click());
        onView(withId(R.id.todaysWeatherSpinner)).check(
                ViewAssertions.matches(withSpinnerText(containsString(selectionText))));
    }

    @Test
    public void testWeatherSpinnerSnow()
    {
        String selectionText = "snow";
        onView(withId(R.id.todaysWeatherSpinner)).perform(ViewActions.click());
        onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(ViewActions.click());
        onView(withId(R.id.todaysWeatherSpinner)).check(
                ViewAssertions.matches(withSpinnerText(containsString(selectionText))));
    }
    @Test
    public void testWeatherSpinnerRain()
    {
        String selectionText = "rain";
        onView(withId(R.id.todaysWeatherSpinner)).perform(ViewActions.click());
        onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(ViewActions.click());
        onView(withId(R.id.todaysWeatherSpinner)).check(
                ViewAssertions.matches(withSpinnerText(containsString(selectionText))));
    }

    @Test
    public void testWeatherSpinnerCloud()
    {
        String selectionText = "cloud";
        onView(withId(R.id.todaysWeatherSpinner)).perform(ViewActions.click());
        onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(ViewActions.click());
        onView(withId(R.id.todaysWeatherSpinner)).check(
                ViewAssertions.matches(withSpinnerText(containsString(selectionText))));
    }

    @Test
    public void testWeatherSpinnerSunny()
    {
        String selectionText = "sunny";
        onView(withId(R.id.todaysWeatherSpinner)).perform(ViewActions.click());
        onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(ViewActions.click());
        onView(withId(R.id.todaysWeatherSpinner)).check(
                ViewAssertions.matches(withSpinnerText(containsString(selectionText))));
    }

    @Test
    public void testWeatherSpinnerHot(){
        String selectionText = "hot";
        onView(withId(R.id.todaysWeatherSpinner)).perform(ViewActions.click());
        onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(ViewActions.click());
        onView(withId(R.id.todaysWeatherSpinner)).check(
                ViewAssertions.matches(withSpinnerText(containsString(selectionText))));
    }

    @Test
    public void testMinutesTextInRange()
    {
        String input = "8";
        String output = "8";

        onView(withId(R.id.minutesSleptNumber)).perform(ViewActions.clearText(),
                ViewActions.typeText(input));
        onView(withId(R.id.minutesSleptNumber))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testMinutesTextBelowRange()
    {
        String input = "-1";
        String output = "1";
        onView(withId(R.id.minutesSleptNumber)).perform(ViewActions.clearText(),
                ViewActions.typeText(input));
        onView(withId(R.id.minutesSleptNumber))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testMinutesTextEdgeCase0()
    {
        String input = "0";
        String output = "0";
        onView(withId(R.id.minutesSleptNumber)).perform(ViewActions.clearText(),
                ViewActions.typeText(input));
        onView(withId(R.id.minutesSleptNumber))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testMinutesTextEdgeCase59()
    {
        String input = "59";
        String output = "59";
        onView(withId(R.id.minutesSleptNumber)).perform(ViewActions.clearText(),
                ViewActions.typeText(input));
        onView(withId(R.id.minutesSleptNumber))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testMinutesTextAboveRange()
    {
        String input = "60";
        String output = "6";
        onView(withId(R.id.minutesSleptNumber)).perform(ViewActions.clearText(),
                ViewActions.typeText(input));
        onView(withId(R.id.minutesSleptNumber))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testMinutesTextInvalidType()
    {
        String input = "hello world";
        String output = "";
        onView(withId(R.id.minutesSleptNumber)).perform(ViewActions.clearText(),
                ViewActions.typeText(input));
        onView(withId(R.id.minutesSleptNumber))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }


    @Test
    public void testHoursTextInRange()
    {
        String input = "8";
        String output = "8";

        onView(withId(R.id.hoursSleptNumber)).perform(ViewActions.clearText(),
                ViewActions.typeText(input));
        onView(withId(R.id.hoursSleptNumber))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testHoursTextBelowRange()
    {
        String input = "-1";
        String output = "1";
        onView(withId(R.id.hoursSleptNumber)).perform(ViewActions.clearText(),
                ViewActions.typeText(input));
        onView(withId(R.id.hoursSleptNumber))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testHoursTextEdgeCase0()
    {
        String input = "0";
        String output = "0";
        onView(withId(R.id.hoursSleptNumber)).perform(ViewActions.clearText(),
                ViewActions.typeText(input));
        onView(withId(R.id.hoursSleptNumber))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testHoursTextEdgeCase23()
    {
        String input = "23";
        String output = "23";
        onView(withId(R.id.hoursSleptNumber)).perform(ViewActions.clearText(),
                ViewActions.typeText(input));
        onView(withId(R.id.hoursSleptNumber))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testHoursTextAboveRange()
    {
        String input = "24";
        String output = "2";
        onView(withId(R.id.hoursSleptNumber)).perform(ViewActions.clearText(),
                ViewActions.typeText(input));
        onView(withId(R.id.hoursSleptNumber))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testHoursTextInvalidType()
    {
        String input = "hello world";
        String output = "";
        onView(withId(R.id.hoursSleptNumber)).perform(ViewActions.clearText(),
                ViewActions.typeText(input));
        onView(withId(R.id.hoursSleptNumber))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testSeekBarMoodEdgeCase0()
    {
        int progress = 0;
        String output = "your mood rating:0";
        onView(withId(R.id.seekBarMood)).perform(setProgress(progress));
        onView(withId(R.id.moodRating))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testSeekBarMood1()
    {
        int progress = 1;
        String output = "your mood rating:1";
        onView(withId(R.id.seekBarMood)).perform(setProgress(progress));
        onView(withId(R.id.moodRating))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testSeekBarMood2()
    {
        int progress = 2;
        String output = "your mood rating:2";
        onView(withId(R.id.seekBarMood)).perform(setProgress(progress));
        onView(withId(R.id.moodRating))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testSeekBarMood3()
    {
        int progress = 3;
        String output = "your mood rating:3";
        onView(withId(R.id.seekBarMood)).perform(setProgress(progress));
        onView(withId(R.id.moodRating))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testSeekBarMood4()
    {
        int progress = 4;
        String output = "your mood rating:4";
        onView(withId(R.id.seekBarMood)).perform(setProgress(progress));
        onView(withId(R.id.moodRating))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testSeekBarMood5()
    {
        int progress = 5;
        String output = "your mood rating:5";
        onView(withId(R.id.seekBarMood)).perform(setProgress(progress));
        onView(withId(R.id.moodRating))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testSeekBarMood6()
    {
        int progress = 6;
        String output = "your mood rating:6";
        onView(withId(R.id.seekBarMood)).perform(setProgress(progress));
        onView(withId(R.id.moodRating))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testSeekBarMood7()
    {
        int progress = 7;
        String output = "your mood rating:7";
        onView(withId(R.id.seekBarMood)).perform(setProgress(progress));
        onView(withId(R.id.moodRating))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testSeekBarMood8()
    {
        int progress = 8;
        String output = "your mood rating:8";
        onView(withId(R.id.seekBarMood)).perform(setProgress(progress));
        onView(withId(R.id.moodRating))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }


    @Test
    public void testSeekBarMood9()
    {
        int progress = 9;
        String output = "your mood rating:9";
        onView(withId(R.id.seekBarMood)).perform(setProgress(progress));
        onView(withId(R.id.moodRating))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }


    @Test
    public void testSeekBarMoodEdgeCase10()
    {
        int progress = 10;
        String output = "your mood rating:10";
        onView(withId(R.id.seekBarMood)).perform(setProgress(progress));
        onView(withId(R.id.moodRating))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testSeekBarMoodAboveBounds()
    {
        int progress = 11;
        String output = "your mood rating:10";
        onView(withId(R.id.seekBarMood)).perform(setProgress(progress));
        onView(withId(R.id.moodRating))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }

    @Test
    public void testSeekBarMoodBelowBounds()
    {
        int progress = -1;
        String output = "your mood rating:0";
        onView(withId(R.id.seekBarMood)).perform(setProgress(progress));
        onView(withId(R.id.moodRating))
                .check(ViewAssertions.matches(ViewMatchers.withText(output)));
    }
}