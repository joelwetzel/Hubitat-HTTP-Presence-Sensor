# HTTP Presence Sensor for Hubitat
A virtual presence sensor for Hubitat that determines its state by trying to access an HTTP endpoint.  If it gets good responses, it reports present.  If it gets a few bad responses in a row, it reports not present.

## Use Cases
- Pinging a device (that has an HTTP endpoint) to see if it is on the house's wifi network.  If that device is present, then it means someone is home.
- When running HomeBridge on a Raspberry Pi, sometimes the homebridge process stops responding to HomeKit and Hubitat.  Use this virtual device to regularly check and see if HomeBridge is still up and running.  If it isn't, you can use Rule Machine rules to cycle the power on the Raspberry Pi.  (I have my RPi plugged into a z-wave outlet for this reason.)

## Installation

The best way to install this code is by using [Hubitat Package Manager](https://community.hubitat.com/t/beta-hubitat-package-manager).

However, if you must install manually:

1. Open your Hubitat web page
2. Go to the "Drivers Code" page
3. Click "+ New Driver"
4. Paste in the contents of httpPresenceSensor.groovy
5. Click "Save"
6. Go to the "Devices" page
7. Click "+ Add Virtual Device"
8. Set "Device Name" and "Device Network Id" to anything you like.  Set "Type" to "HTTP Presence Sensor".
9. Click "Save Device"
10. On the device list, click the name of your new sensor
11. Set "Endpoint URL" to the url that you want the sensor to regularly ping.  Example:  On my HomeBridge setup, the hubitat plugin exposes an endpoint at "http://192.168.1.37:8005/".  This is what I enter for the Endpoint URL.
12. Click "Save Preferences"
