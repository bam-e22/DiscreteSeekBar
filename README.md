[![API](https://img.shields.io/badge/API-19%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=19)
[![License](http://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat)](https://opensource.org/licenses/Apache-2.0)
[![](https://jitpack.io/v/stack07142/DiscreteSeekBar.svg)](https://jitpack.io/#stack07142/DiscreteSeekBar)

# DiscreteSeekBar

DiscreteSeekBar allow users to select a specific value from a range.<br />
The thumb snaps to evenly spaced tick marks along the slider rail. 

## Demo

![](https://media.giphy.com/media/KZf0RBM5LoUTxMvVVO/giphy.gif)

## Installation

1. Add it in your root build.gradle at the end of repositories:

```Gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. Add the following dependency to your build.gradle file:
```Gradle
dependencies {
        implementation 'com.github.stack07142:DiscreteSeekBar:1.0.1'
}
```

## Usage

```xml
<io.github.stack07142.discreteseekbar.DiscreteSeekBar
    android:id="@+id/slider_1"
    android:layout_width="300dp"
    android:layout_height="wrap_content"
    app:discrete_seekbar_maxValue="400"
    app:discrete_seekbar_minValue="-400"
    app:discrete_seekbar_sectionCount="4"
    app:discrete_seekbar_thumbColor="@color/colorPrimaryDark"
    app:discrete_seekbar_thumbDefaultSize="16dp"
    app:discrete_seekbar_thumbPressedSize="24dp"
    app:discrete_seekbar_tickMarkDrawable="@drawable/tickmark"
    app:discrete_seekbar_tickMarkTextColor="@color/colorBlack"
    app:discrete_seekbar_tickMarkTextSize="12sp"
    app:discrete_seekbar_tickMarkTopMargin="18dp"
    app:discrete_seekbar_trackColor="@color/colorBlueGrey900"
    app:discrete_seekbar_trackTouchEnable="true"
    app:discrete_seekbar_trackWidth="1dp"
    app:discrete_seekbar_value="0" />
```

```Kotlin
val tickMarkTextArr1 = SparseArray<String>()
tickMarkTextArr1.append(-400, "low")
tickMarkTextArr1.append(0, "mid")
tickMarkTextArr1.append(400, "high")

slider_1.getConfigBuilder()
        .setTickMarkTextArray(tickMarkTextArr1)
        .setOnValueChangedListener(object : DiscreteSeekBar.OnValueChangedListener {
            override fun onValueChanged(value: Int) {
                Toast.makeText(applicationContext, "value= $value", Toast.LENGTH_SHORT).show()
            }
        })
        .build()
```

Check out the sample codes for more details. [here](https://github.com/stack07142/DiscreteSeekBar/tree/master/app)

## License
```
Copyright 2017 stack07142

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
