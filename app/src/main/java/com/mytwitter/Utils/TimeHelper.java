/*
 *
 * Copyright 2016 Manish Patel (MD)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.mytwitter.Utils;

import android.text.format.Time;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeHelper {

    /**
     * Constants for time
     */
    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int INTERVAL_HALF_SECOND = 500;
    public static final int INTERVAL_ONE_SECOND = 1000;
    public static final int INTERVAL_TWO_SECONDS = 2000;
    public static final int INTERVAL_FIVE_SECONDS = INTERVAL_ONE_SECOND * 5;
    public static final int INTERVAL_TEN_SECONDS = INTERVAL_ONE_SECOND * 10;
    public static final int INTERVAL_FIFTEEN_SECONDS = INTERVAL_ONE_SECOND * 15;
    public static final int INTERVAL_THIRTY_SECONDS = INTERVAL_ONE_SECOND * 30;
    public static final int INTERVAL_ONE_MINUTE = INTERVAL_ONE_SECOND * 60;
    public static final int INTERVAL_TEN_MINUTES = INTERVAL_ONE_MINUTE * 10;
    public static final long INTERVAL_FIFTEEN_MINUTES = INTERVAL_ONE_MINUTE * 15;
    public static final long INTERVAL_HALF_HOUR = INTERVAL_FIFTEEN_MINUTES * 2;
    public static final long INTERVAL_HOUR = INTERVAL_HALF_HOUR * 2;
    public static final long INTERVAL_HALF_DAY = INTERVAL_HOUR * 12;
    public static final long INTERVAL_DAY = INTERVAL_HOUR * 24;
    public static final String TIMESTAMP_SHORT_DATE = "yyyy-MM-dd";
    public static final String TIMESTAMP_SHORT_TIME = "HH:mm:ss";
    public static final String TIMESTAMP_SHORT_TIME_NO_SECONDS = "HH:mm";
    public static final String TIMESTAMP_FORMAT = TIMESTAMP_SHORT_DATE + " " + TIMESTAMP_SHORT_TIME; // "yyyy-MM-dd HH:mm:ss"
    public static final String TIMESTAMP_FORMAT_LONG_DATE = "yyyyMMddHHmmss";
    public static final String TIMESTAMP_FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String TIMESTAMP_FORMAT_ISO8601_FULL = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String TIMESTAMP_UTC_ZONE = "UTC";
    public static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone(TIMESTAMP_UTC_ZONE);
    /**
     * Time formatters
     */
    private static final SimpleDateFormat timeAndDateFormatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
    private static final SimpleDateFormat timeFormatter = new SimpleDateFormat(TIMESTAMP_SHORT_TIME);
    private static final SimpleDateFormat timeFormatterDefault = new SimpleDateFormat(TIMESTAMP_SHORT_TIME, Locale.getDefault());
    private static final SimpleDateFormat timeFormatterDefaultNoSeconds = new SimpleDateFormat(TIMESTAMP_SHORT_TIME_NO_SECONDS, Locale.getDefault());
    private static final SimpleDateFormat shortDateFormatter = new SimpleDateFormat(TIMESTAMP_SHORT_DATE);
    private static final SimpleDateFormat longDateFormatter = new SimpleDateFormat(TIMESTAMP_FORMAT_LONG_DATE);
    private static final SimpleDateFormat timeAndDateIsoFormatter = new SimpleDateFormat(TIMESTAMP_FORMAT_ISO8601);

    /**
     * Calculates difference between two times
     *
     * @param startDateTime Start time
     * @param endDateTime   End time
     * @return Time difference in milliseconds
     */
    public static long compareDateStrings(String startDateTime, String endDateTime) throws ParseException {
        Date start = timeAndDateFormatter.parse(startDateTime);
        Date end = timeAndDateFormatter.parse(endDateTime);
        return end.getTime() - start.getTime();
    }

    /**
     * Compares two timestamps
     * @param t1 Timestamp 1
     * @param t2 Timestamp 2
     * @return 0 if the two Timestamp objects are equal; a value less than 0 if this Timestamp object
     * is before the given argument; and a value greater than 0 if this Timestamp object is after the
     * given argument.
     */
    public static long compareTimestamps(Timestamp t1, Timestamp t2) {
        long l1 = t1.getTime();
        long l2 = t2.getTime();

        if (l2 > l1)
            return 1;
        else if (l1 > l2)
            return -1;
        else
            return 0;
    }

    /**
     * Calculates difference between the start time and 'now'
     *
     * @param startTime Start time
     * @return Time difference in milliseconds
     * @throws ParseException
     */
    public static long getCurrentTimeDifference(String startTime) throws ParseException {
        String currentTime = timeAndDateFormatter.format(new Date());
        return compareDateStrings(startTime, currentTime);
    }

    /**
     * Calculates a difference between two times.
     *
     * @param startTime start time
     * @param endTime   end time
     * @return difference as a "HH:mm:ss" string
     */
    public static String getHumanReadableDifference(long startTime, long endTime) {
        long timeDiff = endTime - startTime;

        return String.format("%02d:%02d:%02d",
                TimeUnit.MICROSECONDS.toHours(timeDiff),
                TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff)),
                TimeUnit.MILLISECONDS.toSeconds(timeDiff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeDiff)));
    }

    /**
     * Returns current time in 24 hour format "HH:mm:ss"
     * @return
     */
    public static String getCurrentTimeString() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        return timeFormatterDefault.format(calendar.getTime());
    }

    public static String getCurrentHourMinutes() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        return timeFormatterDefaultNoSeconds.format(calendar.getTime());
    }

    public static String getTimeInHoursMinutes(Timestamp time) {
        return timeFormatterDefaultNoSeconds.format(time);
    }

    /**
     * Calculates a difference between two time and shows it in human-readable form.
     *
     * @param startTime start time
     * @param endTime   end time
     * @return e.g. "03 min 49 sec"
     */
    public static String getHumanTimeDifference(long startTime, long endTime) {
        long timeDiff = endTime - startTime;

        return String.format("%d min %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(timeDiff),
                TimeUnit.MILLISECONDS.toSeconds(timeDiff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeDiff)));
    }

    /**
     * Returns current date ('now') as a "yyyy-MM-dd" string.
     */
    public static String getTodayShortDateString() {
        return shortDateFormatter.format(new Date());
    }

    /**
     * Returns current date and time as a "yyyyMMddHHmmss" string.
     */
    public static String getTodayLongDateString() {
        return longDateFormatter.format(new Date());
    }

    /**
     * Returns tomorrow's date as a string
     *
     * @return
     */
    public static String getTomorrowShortDateString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);

        return shortDateFormatter.format(calendar.getTime());
    }

    /**
     * Converts a Calendar to its long value.
     *
     * @param date Calendar date
     * @return
     */
    public static long getLongDate(Calendar date) {
        return Long.parseLong(longDateFormatter.format(date.getTime()));
    }

    public static Date getLongDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = getCalendar(year, month, day, hour, minute, second);
        return calendar.getTime();
    }
    
    /**
     * Returns current date ('now') as a "yyyy-MM-dd" string.
     */
    public static String getToday() {
        return shortDateFormatter.format(new Date());
    }

    /**
     * Returns current date and time as a "yyyyMMddHHmmss" string.
     */
    public static String getNow() {
        return longDateFormatter.format(new Date());
    }

    /**
     * Converts a Calendar date to yyyy-MM-dd" string
     *
     * @param calendar
     * @return
     */
    public static String getShortDate(Calendar calendar) {
        return shortDateFormatter.format(calendar.getTime());
    }

    /**
     * Creates a date in "yyyy-MM-dd" format.
     */
    public static String getShortDate(int year, int month, int day) {
        Calendar calendar = TimeHelper.getCalendar(year, month, day);
        return shortDateFormatter.format(calendar.getTime());
    }

    public static Date getShortTime(int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, hourOfDay, minute, second);
        return calendar.getTime();
    }


    /**
     * Creates a Calendar date from its long value.
     *
     * @param date
     * @return
     */
    public static Calendar getCalendar(long date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(longDateFormatter.parse(String.valueOf(date)));

        return calendar;
    }


    public static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    /**
     * Creates a new instance of GregorianCalendar.
     *
     * @param year
     * @param month Month (in Java months numbers are 0-11)
     * @param day
     * @return
     */
    public static Calendar getCalendar(int year, int month, int day) {
        Calendar date = Calendar.getInstance();

        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, day);

        return date;
    }

    /**
     * Creates a new instance of GregorianCalendar.
     *
     * @param year
     * @param month     Month (in Java months numbers are 0-11)
     * @param day
     * @param hourOfDay
     * @param minute
     * @param second
     * @return
     */
    public static Calendar getCalendar(int year, int month, int day, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        return calendar;
    }

    /**
     * Convert a date string into a Timestamp.
     *
     * @param dateString
     * @return
     */
    public static Timestamp getTimeStamp(String dateString) throws ParseException {
        Date date = timeAndDateFormatter.parse(dateString);
        return new Timestamp(date.getTime());
    }

    /**
     * Creates a timestamp from date parts.
     */
    public static Timestamp getTimeStampFromDateParts(int year, int month, int day, int hour, int minute, int second) throws ParseException {
        String dateTimeString = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
        Date date = timeAndDateFormatter.parse(dateTimeString);
        return new Timestamp(date.getTime());
    }

    /**
     * Caonverts a date in Calendar form into a Timestamp.
     *
     * @param date
     * @return
     */
    public static Timestamp getTimeStampFromCalendar(Calendar date) {
        return new Timestamp(date.getTimeInMillis());
    }

    /**
     * Returns current date/time as a Timestamp.
     * The value is the number of milliseconds since Jan. 1, 1970, midnight GMT.
     *
     * @return
     */
    public static Timestamp getCurrentTimestamp() {
        Date now = Calendar.getInstance().getTime();
        return new Timestamp(now.getTime());
    }

    /**
     * Returns the current time in the time zone that is currently set for the device.
     *
     * @return An instance of the Time class representing the current moment, specified with second precision
     */
    public static Time getCurrentTime() {
        Time now = new Time(Time.getCurrentTimezone());
        now.setToNow();

        return now;
    }

    /**
     * Converts a date to "yyyy-MM-dd'T'HH:mm:ss" string in UTC timezone.
     */
    public static String dateToUtcIsoString(Date date) {
        timeAndDateIsoFormatter.setTimeZone(TIMEZONE_UTC);
        return timeAndDateIsoFormatter.format(date);
    }

    /**
     * Converts a date to "yyyy-MM-dd HH:mm:ss" string in UTC timezone.
     */
    public static String dateToUtcString(Date date) {
        timeAndDateFormatter.setTimeZone(TIMEZONE_UTC);
        return timeAndDateFormatter.format(date);
    }

    /**
     * Converts a date to "yyyy-MM-dd'T'HH:mm:ss" (ISO8601) string.
     */
    public static String dateToIsoString(Date date) {
        return timeAndDateIsoFormatter.format(date);
    }

    /**
     * Converts milliseconds to "HH:mm:ss" (used by countdown timers, so time can be shown as negative)
     */
    public static String millisTo24HourClock(long millis) {
        long hours = Math.abs((millis / (1000 * 60 * 60)) % 24);
        long minutes = Math.abs((millis / (1000 * 60)) % 60);
        long seconds = Math.abs((millis / 1000) % 60);

        if (hours == 0)
            return String.format("%02d:%02d", minutes, seconds);
        else
            return String.format("%1d:%02d:%02d", hours, minutes, seconds);
    }

    public static Date getDate(String date) throws ParseException {
        Date result = (Date) timeAndDateFormatter.parse(date);
        return result;
    }

    /**
     * Converts a timestamp to "yyyy-MM-dd HH:mm:ss" string.
     */
    public static String getString(Timestamp timestamp) {
        return timeAndDateFormatter.format(timestamp.getTime());
    }

    /**
     * Converts a Date to "yyyy-MM-dd HH:mm:ss" string.
     */
    public static String getString(Date date) {
        return timeAndDateFormatter.format(date);
    }

    /**
     * Converts a Timestamp to a human-readable string
     *
     * @param timestamp
     * @param format
     * @return
     */
    public static String getString(Timestamp timestamp, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(timestamp.getTime());
    }

    /**
     * Returns current timestamp as a string
     * @return
     */
    public static String getCurrentTimestampString() {
        return getString(new Date());
    }

    /**
     * Converts time to "yyyy-MM-dd'T'HH:mm:ss" string in UTS zone
     *
     * @param time
     * @return
     */
    public static String getUtcString(long time) {
        timeAndDateIsoFormatter.setTimeZone(TIMEZONE_UTC);
        return timeAndDateIsoFormatter.format(time);
    }

    public static String getUnixString(String unixTimestamp) {
        long timestamp = Long.parseLong(unixTimestamp);
        String result = new SimpleDateFormat(TIMESTAMP_FORMAT, Locale.UK).format(new Date(timestamp * 1000));

        return result;
    }


    public static Calendar setToMidnight(Calendar cal){
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static Calendar setHoursMinutes(Calendar cal, int hours, int minutes){
        //Calendar newCalendar = Calendar.getInstance();
        Calendar newCalendar = (Calendar)cal.clone();
        newCalendar.set(Calendar.HOUR_OF_DAY, hours);
        newCalendar.set(Calendar.MINUTE, minutes);
        return newCalendar;
    }

}
