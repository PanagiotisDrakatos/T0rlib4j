
package com.msopentech.thali.java.toronionproxy;

import java.util.concurrent.TimeUnit;

/**
 * Android uses FileObserver and Java uses the WatchService, this class abstracts the two.
 */
public interface WriteObserver {
    /**
     * Waits timeout of unit to see if file is modified
     * @param timeout How long to wait before returning
     * @param unit Unit to wait in
     * @return True if file was modified, false if it was not
     */
    boolean poll(long timeout, TimeUnit unit);
}
