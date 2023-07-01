package me.outspending.timespan;

import java.util.concurrent.TimeUnit;

public class TimespanTest {

    private final long interval;

    public TimespanTest(long interval) {
        this.interval = interval;
    }

    public static TimespanTest createTimespan(long seconds) {
        long interval = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds);
        return new TimespanTest(interval);
    }

    public static TimespanTest createTimespan(long seconds, long minutes) {
        long interval = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds) +
                TimeUnit.MINUTES.toMillis(minutes);
        return new TimespanTest(interval);
    }

    public static TimespanTest createTimespan(long seconds, long minutes, long hours) {
        long interval = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds) +
                TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.HOURS.toMillis(hours);
        return new TimespanTest(interval);
    }

    public static TimespanTest createTimespan(long seconds, long minutes, long hours, long days) {
        long interval = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds) +
                TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.HOURS.toMillis(hours) +
                TimeUnit.DAYS.toMillis(days);
        return new TimespanTest(interval);
    }

    public String getFormattedTime() {
        long timeLeft = getTimeLeft();
        StringBuilder builder = new StringBuilder();

        long days = TimeUnit.MILLISECONDS.toDays(timeLeft);
        long hours = TimeUnit.MILLISECONDS.toHours(timeLeft) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft) % 60;

        if (days > 0) {
            builder.append(days).append("d ");
        }
        if (hours > 0) {
            builder.append(hours).append("h ");
        }
        if (minutes > 0) {
            builder.append(minutes).append("m ");
        }
        if (seconds > 0 || builder.length() == 0) {
            builder.append(seconds).append("s ");
        }
        return builder.toString();
    }

    public boolean isFinished() {
        return System.currentTimeMillis() >= interval;
    }

    public long getInterval() {
        return interval;
    }

    public long getTimeLeft() {
        return interval - System.currentTimeMillis();
    }

    public long getSecondsLeft() {
        return TimeUnit.MILLISECONDS.toSeconds(getTimeLeft()) % 60;
    }

    public long getMinutesLeft() {
        return TimeUnit.MILLISECONDS.toMinutes(getTimeLeft()) % 60;
    }

    public long getHoursLeft() {
        return TimeUnit.MILLISECONDS.toHours(getTimeLeft()) % 24;
    }

    public long getDaysLeft() {
        return TimeUnit.MILLISECONDS.toDays(getTimeLeft());
    }

    public long getSecondsPassed() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - interval);
    }

    public long getMinutesPassed() {
        return TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - interval);
    }

    public long getHoursPassed() {
        return TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - interval);
    }

    public long getDaysPassed() {
        return TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - interval);
    }
}