# LogCollector
Test tool class for collecting android logs, is used for the android test.

eg:

// collect
You can use collect function as follows:
Log.e("LogCollector", "test getMomentLog");
String result = LogCollector.collect();
assertTrue(result.contains("test getMomentLog"));

// clear
You can use clear function as follows:
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
