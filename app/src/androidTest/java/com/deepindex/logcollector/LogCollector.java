package com.deepindex.logcollector;

import android.util.Log;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DeepIndex.
 * Test tool class for collecting android logs, is used for the android test.
 */

public class LogCollector {
    private static final String TAG = LogCollector.class.getSimpleName();
    private static final int BUF_SIZE = 8192;
    private static final long DEFAULT_RECENT_INTERVAL_TIME = 1 * 1000L;

    private LogCollector() {
    }

    /**
     * collect logs. call logcat command: logcat ...
     * @param intervalTime log time interval.
     * @return the collected logs.
     */
    public static String collect(long intervalTime) {
        if (intervalTime <= 0) {
            intervalTime = DEFAULT_RECENT_INTERVAL_TIME;
        }
        StringBuilder sb = new StringBuilder();
        InputStreamReader input = null;
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.mmm");
            date.setTime(System.currentTimeMillis() - intervalTime);
            java.lang.Process logcat =
                new ProcessBuilder("/system/bin/logcat", "-b", "events", "-b", "system", "-b",
                    "main", "-b", "crash", "-d", "-t", sdf.format(date)).redirectErrorStream(true)
                    .start();
            try {
                logcat.getOutputStream().close();
                logcat.getErrorStream().close();
            } catch (IOException e) {
            }
            input = new InputStreamReader(logcat.getInputStream());

            int num;
            char[] buf = new char[BUF_SIZE];
            while ((num = input.read(buf)) > 0) {
                sb.append(buf, 0, num);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error running logcat", e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }

    public static String collect() {
        return collect(DEFAULT_RECENT_INTERVAL_TIME);
    }

    /**
     * clear the entire log. call logcat command: logcat -c
     */
    public static void clear() {
        try {
            java.lang.Process logcat =
                new ProcessBuilder("/system/bin/logcat", "-c").redirectErrorStream(true).start();
            logcat.getOutputStream().close();
            logcat.getErrorStream().close();
            logcat.getInputStream().close();
        } catch (IOException e) {
            Log.e(TAG, "Error running logcat", e);
        }
    }
}
