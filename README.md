[![CI Status](https://travis-ci.org/kaltura/playkit-android-broadpeak-smartlib.svg?branch=develop)](https://travis-ci.com/github/kaltura/playkit-android-broadpeak-smartlib)
[![Download](https://img.shields.io/maven-central/v/com.kaltura.playkit/broadpeakplugin?label=Download)](https://search.maven.org/artifact/com.kaltura.playkit/broadpeakplugin)
[![License](https://img.shields.io/badge/license-AGPLv3-black.svg)](https://github.com/kaltura/playkit-android/blob/master/LICENSE)
![Android](https://img.shields.io/badge/platform-android-green.svg)

# Kaltura plugin for Broadpeak SmartLib for Kaltura Player on Android

## About Kaltura Plugin for Broadpeak SmartLib
Broadpeak plugin is designed for integration a [Broadpeak service](https://broadpeak.tv/)

## Kaltura Plugin for Broadpeak SmartLib Integration
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

Like Kaltura's other Playkit plugins, **Kaltura Plugin for Broadpeak SmartLib** includes configurations that can be used by your application.

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

Broadpeak SmartLib libraries the Broadpeak SDK, and all associated APIs,  and header files (collectively, the “Broadpeak Materials”) are the exclusive property of Broadpeak and subject to Broadpeak license terms.
Notwithstanding anything to the contrary stated in this repository, Kaltura, as copyright holder on the Plug in program, hereby 
(i)	confirms that the Broadpeak Materials is an independent program and set of materials, and are not licensed under nor intended to be otherwise subject to the AGPL-3.0 license nor any other open source license, but used only under the terms of Broadpeak license  and ;
(ii)	gives express permission, as a special exception to AGPL license terms, to link such AGPL code or portion of this code, with the Broadpeak Materials or any part thereof, regardless of the license terms of the AGPL code, and to produce an executable, copy and distribute the resulting executable, provided that you also mee the applicable license terms and conditions of the Broadpeak Materials and the AGPL code;  
To obtain licensing information regarding the Broadpeak Materials, contact: https://broadpeak.tv/form/contact-broadpeak/ 

[Broadpeak®  SmartLib]
Copyright © 2020–2021 Broadpeak, S.A.S.
All Rights Reserved
 
This program contains proprietary information, which is confidential and
trade secret of Broadpeak, SAS.
 
It is protected by intellectual property as an unpublished work under
applicable Copyright laws and international conventions.
 
Certain functionalities and/or features of such program may also be subject
to some patent and pending patent applications.
 
Broadpeak® is registered trademark and trade name of Broadpeak, S.A.S., and
shall not be used in any manner without express written from Broadpeak, S.A.S.
 
Unless expressly otherwise agreed in writing by Broadpeak, such as under
commercial agreement executed by authorized representative of Broadpeak,
the use of the program and its documentation is strictly limited to
your own internal evaluation of such program and its functionalities for
potential collaboration with Broadpeak. Recipient shall retain this program,
documentations, and materials in confidence and is not allowed to make
any copy, decompile, reverse engineer, disassemble, and attempt to derive
the source code of, modify, or create derivative works of the program, any
update, or any part thereof, other than as expressly permitted or expressly
made mandatory by applicable laws.
 
Any attempt to do so is a violation of the rights of Broadpeak. If you or
any person under your control or authority breach this restriction, you may
be subject to prosecution and damages.
 
Contact: https://broadpeak.tv/form/contact-broadpeak/
