/*
 * Copyright 2016 Eric Peterson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.ericp.flashlight;

/**
 * Interface to the device flashlight.
 */
interface Flashlight {
    /**
     * Toggle the flashlight on or off.
     */
    void toggle() throws FlashlightUnavailableException;

    void release();
    // Nice-to-have methods:
    // boolean isOn() throws CameraUnavailableException;
    // void turnOn() throws CameraUnavailableException;
    // void turnOff() throws CameraUnavailableException;
}
