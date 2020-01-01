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
    private static final long DEFAULT_INTERVAL_TIME = 1 * 1000L;

    private LogCollector() {
        // null
    }

    /**
     * collect logs. call logcat command: logcat ...
     *
     * @param intervalTime log time interval.
     * @return the collected logs.
     */
    public static String collect(long intervalTime) {
        if (intervalTime <= 0) {
            intervalTime = DEFAULT_INTERVAL_TIME;
        }
        StringBuilder sb = new StringBuilder();
        InputStreamReader inputStreamReader = null;
        try {
            Date date = new Date();
            date.setTime(System.currentTimeMillis() - intervalTime);
            String timeFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.mmm").format(date);
            java.lang.Process logcatProcess =
                new ProcessBuilder("/system/bin/logcat", "-b", "events", "-b", "system", "-b",
                    "main", "-b", "crash", "-d", "-t", timeFormat).redirectErrorStream(true)
                    .start();
            try {
                logcatProcess.getOutputStream().close();
                logcatProcess.getErrorStream().close();
            } catch (IOException e) {
            }
            inputStreamReader = new InputStreamReader(logcatProcess.getInputStream());

            int size;
            char[] buffer = new char[BUF_SIZE];
            while ((size = inputStreamReader.read(buffer)) > 0) {
                sb.append(buffer, 0, size);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error running logcat", e);
        } finally {
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }

    public static String collect() {
        return collect(DEFAULT_INTERVAL_TIME);
    }

    /**
     * clear the entire log. call logcat command: logcat -c
     */
    public static void clear() {
        try {
            java.lang.Process logcatProcess =
                new ProcessBuilder("/system/bin/logcat", "-c").redirectErrorStream(true).start();
            logcatProcess.getOutputStream().close();
            logcatProcess.getErrorStream().close();
            logcatProcess.getInputStream().close();
        } catch (IOException e) {
            Log.e(TAG, "Error running logcat", e);
        }
    }
}
