package net.jacobpeterson.spigot.util;

/**
 * This interface is used as a {@link Runnable}, but allows for passing in of a single variable.
 *
 * @param <T> the type of Object to pass to the {@link #run(Object)}
 */
public interface InstanceRunnable<T> {

    /**
     * Method to run.
     *
     * @param t the object passed in
     */
    void run(T t);
}
