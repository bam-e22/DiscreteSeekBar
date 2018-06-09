package io.github.stack07142.sample.discreteseekbar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.SparseArray
import android.widget.Toast
import io.github.stack07142.discreteseekbar.DiscreteSeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
         * slider_1
         */

        val contentDescriptionArray1 = SparseArray<String>()
        contentDescriptionArray1.append(-400, "low")
        contentDescriptionArray1.append(-200, "mid low")
        contentDescriptionArray1.append(0, "mid")
        contentDescriptionArray1.append(200, "mid high")
        contentDescriptionArray1.append(400, "high")

        val tickMarkTextArr1 = SparseArray<String>()
        tickMarkTextArr1.append(-400, "low")
        tickMarkTextArr1.append(0, "mid")
        tickMarkTextArr1.append(400, "high")

        slider_1.getConfigBuilder()
                .setTickMarkTextArray(tickMarkTextArr1)
                .setContentDescriptionArray(contentDescriptionArray1)
                .setOnValueChangedListener(object : DiscreteSeekBar.OnValueChangedListener {
                    override fun onValueChanged(value: Int) {
                        Toast.makeText(applicationContext, "value= $value", Toast.LENGTH_SHORT).show()
                    }
                })
                .build()

        /*
         * slider_2
         */
        val tickMarkTextArr2 = SparseArray<String>()
        tickMarkTextArr2.append(-100, "-100")
        tickMarkTextArr2.append(-50, "-50")
        tickMarkTextArr2.append(0, "0")
        tickMarkTextArr2.append(50, "50")
        tickMarkTextArr2.append(100, "100")

        slider_2.getConfigBuilder()
                .setTrackTouchEnable(false)
                .setMinValue(-100)
                .setMaxValue(100)
                .setValue(-50)
                .setSectionCount(4)
                .setThumbColor(R.color.colorPurple500)
                .setTickMarkTextArray(tickMarkTextArr2)
                .setOnValueChangedListener(object : DiscreteSeekBar.OnValueChangedListener {
                    override fun onValueChanged(value: Int) {
                        Toast.makeText(applicationContext, "value= $value", Toast.LENGTH_SHORT).show()
                    }
                })
                .build()

        /*
         * slider_3
         */
        val tickMarkTextArr3 = SparseArray<String>()
        tickMarkTextArr3.append(-200, "a")
        tickMarkTextArr3.append(-100, "b")
        tickMarkTextArr3.append(0, "c")
        tickMarkTextArr3.append(100, "d")
        tickMarkTextArr3.append(200, "e")

        slider_3.getConfigBuilder()
                .setThumbDefaultSize(24)
                .setThumbPressedSize(30)
                .setTickMarkTextArray(tickMarkTextArr3)
                .setOnValueChangedListener(object : DiscreteSeekBar.OnValueChangedListener {
                    override fun onValueChanged(value: Int) {
                        Toast.makeText(applicationContext, "value= $value", Toast.LENGTH_SHORT).show()
                    }
                })
                .build()

        /*
         * slider_4
         */
        slider_4.getConfigBuilder()
                .setTrackWidth(3)
                .setTrackColor(R.color.colorPink200)
                .setOnValueChangedListener(object : DiscreteSeekBar.OnValueChangedListener {
                    override fun onValueChanged(value: Int) {
                        Toast.makeText(applicationContext, "value= $value", Toast.LENGTH_SHORT).show()
                    }
                })
                .build()
    }
}
