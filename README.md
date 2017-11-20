# Wiman-SDK-Sample

A simple App that integrates Wiman SDK

In order to use Wiman SDK you must create your Wiman project and get your SDK key here:
* [Wiman Console](https://developers.wiman.me/console) 

The related documentation is available here:
* [Wiman Documentation](https://developers.wiman.me/console/docs/) 


## SETUP

In `SampleAppApplication` add your SDK key
```java
  WimanSDK.initialize(this, "YOUR WIMAN SDK KEY");
```
If you want to show Wiman Wi-Fi networks on google map, please add your Google API KEY in `AndroidManifest.xml`
```xml
 <meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="YOUR GOOGLE MAP KEY" />
```
