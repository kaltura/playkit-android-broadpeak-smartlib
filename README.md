[![CI Status](https://travis-ci.org/kaltura/playkit-android-broadpeak-smartlib.svg?branch=develop)](https://travis-ci.com/github/kaltura/playkit-android-broadpeak-smartlib)
[![Download](https://img.shields.io/maven-central/v/com.kaltura.playkit/broadpeakplugin?label=Download)](https://search.maven.org/artifact/com.kaltura.playkit/broadpeakplugin)
[![License](https://img.shields.io/badge/license-AGPLv3-black.svg)](https://github.com/kaltura/playkit-android/blob/master/LICENSE)
![Android](https://img.shields.io/badge/platform-android-green.svg)

# Broadpeak plugin for Kaltura Player on Android

## About Broapeak plugin
Broadpeak plugin is designed for integration a [Broadpeak service](https://broadpeak.tv/)

## BroadpeakPlugin Integration
The easiest way to get the **BroadpeakPlugin** is to add it as a Gradle dependency.
First, add `Smartlib` repository with your credentials to your root `build.gradle`
```Groovy
allprojects {
    repositories {
        google()
        jcenter()
        maven {
            credentials {
                username "username"
                password "password"
            }
            url "https://delivery-platform.broadpeak.tv/android/repository/smartlib"
        }
    }
}
```
Next, add the **BroadpeakPlugin** dependency to the `build.gradle` file of your app module
```Groovy
implementation 'com.kaltura.playkit:broadpeakplugin:4.x.x'
```

This dependency includes **com.kaltura.player:tvplayer** internally, so no need to add them to the client app's `build.gradle`.

Next, lets see how to use the **BroadpeakPlugin** in your application.

## Plugin Configurations

Like Kaltura's other Playkit plugins, **BroadpeakPlugin** includes configurations that can be used by your application.

In the following code snippet, you can see how to configure **BroadpeakPlugin** with custom parameters. Below are detailed explanation of each field.

```Kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var player: KalturaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadPlaykitPlayer()
    }
    private fun loadPlaykitPlayer() {
        // PlayerInitOptions
        val playerInitOptions = PlayerInitOptions(PARTNER_ID)
        playerInitOptions.setAutoPlay(true)
        playerInitOptions.setAllowCrossProtocolEnabled(true)
        // Broadpeak Configuration
        val pkPluginConfigs = PKPluginConfigs()
        val broadpeakConfig = BroadpeakConfig().apply {
            // analyticsAddress - The address of the analytics server to send metrics to
            analyticsAddress = "https://analytics.kaltura.com/api_v3/index.php"
            // nanoCDNHost The address inside the home network of the device where the nanoCDN is embedded or "discover" if the discovery is enabled on the nanoCDN.
            nanoCDNHost = "cdnapisec.kaltura.com"
            // broadpeakDomainNames The domain name list to use to identify url(s) using broadpeak product (i.e "cdn.broadpeak.com,cdn2.broadpeak.com"). "*" specific value is used to declare that all given url are using broadpeak product. Empty value "" is used to declare that all given url are not using broadpeak value
            broadpeakDomainNames = "*"
        }
        pkPluginConfigs.setPluginConfig(BroadpeakPlugin.factory.name, broadpeakConfig)
        playerInitOptions.setPluginConfigs(pkPluginConfigs)
        player = KalturaOttPlayer.create(this@MainActivity, playerInitOptions)

        // handle Broadpeak error event
         player.addListener(this, BroadpeakEvent.error) { event ->
            Log.i(TAG, "BROADPEAK ERROR " + event.errorMessage)
        }
    }
}
```

## Error Handling

Incase there is failure from the Broadpeak SDK side error will be fired and in case app is registered to this error it will be received in the listener.

#### The event payload contains 3 parameters

```
 BroadpeakEvent.Type.ERROR
 errorCode
 errorMessage
```

```
player?.addListener(this, BroadpeakEvent.error) { event ->
            Log.i(TAG, "BROADPEAK ERROR " + event.errorMessage)
}
```

## Sample

[Broadpeak Sample](https://github.com/kaltura/kaltura-player-android-samples/tree/master/AdvancedSamples/Broadpeak)

In this sample you will have to provide the username and password for the broadpeak sdk (in build.gradle)
and the partner and media information as well
in order for it to work properly.

## License and Copyright Information

All code in this project is released under the [AGPLv3 license](http://www.gnu.org/licenses/agpl-3.0.html) unless a different license for a particular library is specified in the applicable library path.   

Copyright © Kaltura Inc. All rights reserved.   
Authors and contributors: See [GitHub contributors list](https://github.com/kaltura/playkit-android-broadpeak-smartlib/graphs/contributors).
