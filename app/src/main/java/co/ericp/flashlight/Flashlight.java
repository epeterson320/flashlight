package co.ericp.flashlight;

/**
 * Interface to the device flashlight.
 */
public interface Flashlight {
    /**
     * Toggle the flashlight on or off.
     *
     * @return true if the flashlight is now on.
     */
    boolean toggle();
}
