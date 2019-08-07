package net.jacobpeterson.spigot.util;

import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * This interface is meant to be implemented by classes that want to track exceptions for reporting them back later to
 * other objects.
 */
public interface ExceptionTracker {

    /**
     * Gets whether or not an exception occurred.
     *
     * @return whether or not an exception occurred
     */
    boolean didExceptionOccur();

    /**
     * Gets the list of Exception Entries, key being the message and the value being the exception.
     *
     * @return the list of exceptions
     */
    ArrayList<AbstractMap.SimpleEntry<String, Exception>> getExceptions();

}
