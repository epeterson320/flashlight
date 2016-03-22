package co.ericp.flashlight.core;

public interface Flashlight {
    void turnOn() throws Exception;
    void turnOff() throws Exception;
    boolean isOn() throws Exception;
}
