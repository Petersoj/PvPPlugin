package net.jacobpeterson.spigot.util;

/**
 * The interface Initializers.
 * Used to initialize and deinitialize an object (most likely a manager or controller).
 */
public interface Initializers {

    /**
     * Init method for an object.
     *
     * @throws Exception an optional exception to throw
     */
    void init() throws Exception;

    /**
     * Deinit method for an object.
     *
     * @throws Exception an optional exception to throw
     */
    void deinit() throws Exception;

}
