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
        val tickMarkTextArr4 = SparseArray<String>()
        tickMarkTextArr4.append(0, "0")

        slider_4.getConfigBuilder()
                .setOnValueChangedListener(object : DiscreteSeekBar.OnValueChangedListener {
                    override fun onValueChanged(value: Int) {
                        Toast.makeText(applicationContext, "value= $value", Toast.LENGTH_SHORT).show()
                    }
                })
                .build()

        /*
         * slider_5
         */
        slider_5.getConfigBuilder()
                .setOnValueChangedListener(object : DiscreteSeekBar.OnValueChangedListener {
                    override fun onValueChanged(value: Int) {
                        Toast.makeText(applicationContext, "value= $value", Toast.LENGTH_SHORT).show()
                    }
                })
                .build()
    }
}
