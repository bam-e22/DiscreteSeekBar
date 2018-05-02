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
        implementation 'com.github.stack07142:DiscreteSeekBar:1.0.0'
}
```

## Usage

```xml
<io.github.stack07142.discreteseekbar.DiscreteSeekBar
    android:id="@+id/slider_1"
    android:layout_width="300dp"
    android:layout_height="wrap_content"
    app:attr_maxValue="400"
    app:attr_minValue="-400"
    app:attr_sectionCount="4"
    app:attr_thumbColor="@color/colorPrimaryDark"
    app:attr_thumbDefaultSize="16dp"
    app:attr_thumbPressedSize="24dp"
    app:attr_tickMarkDrawable="@drawable/tickmark"
    app:attr_tickMarkTextColor="@color/colorBlack"
    app:attr_tickMarkTextSize="12sp"
    app:attr_tickMarkTopMargin="18dp"
    app:attr_trackColor="@color/colorBlueGrey900"
    app:attr_trackTouchEnable="true"
    app:attr_trackWidth="1dp"
    app:attr_value="0" />
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