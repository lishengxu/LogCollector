package com.deepindex.logcollector;

import android.util.Log;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by DeepIndex.
 */
public class LogCollectorTest {
    @Test
    public void getMomentLog() {
        Log.e("LogCollector", "test getMomentLog");
        String result = LogCollector.collect();
        assertTrue(result.contains("test getMomentLog"));
    }

    @Test
    public void clear() {
        String info = "test clear";
        Log.e("LogCollector", info);
        String result = LogCollector.collect();
        assertTrue(result.contains(info));
        LogCollector.clear();
        try {
            // The logcat -c is asynchronous operation,
            // so a delay to ensure that the clear is complete.
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result2 = LogCollector.collect();
        assertFalse(result2.contains(info));
    }
}