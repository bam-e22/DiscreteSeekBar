package io.github.stack07142.sample.discreteseekbar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.SparseArray
import android.widget.Toast
import io.github.stack07142.kotlindiscreteslider.DiscreteSeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // slider_1
        val tickMarkTextArr = SparseArray<String>()
        tickMarkTextArr.append(-400, "low")
        tickMarkTextArr.append(0, "mid")
        tickMarkTextArr.append(400, "high")

        slider_1.setTickMarkTextArray(tickMarkTextArr)
        slider_1.setOnValueChangedListener(object : DiscreteSeekBar.OnValueChangedListener {
            override fun onValueChanged(value: Int) {
                Toast.makeText(applicationContext, "value= $value", Toast.LENGTH_SHORT).show()
            }
        })

        // slider_2
        slider_2.setOnValueChangedListener(object : DiscreteSeekBar.OnValueChangedListener {
            override fun onValueChanged(value: Int) {
                Toast.makeText(applicationContext, "value= $value", Toast.LENGTH_SHORT).show()
            }
        })

        // slider_3
        slider_3.setOnValueChangedListener(object : DiscreteSeekBar.OnValueChangedListener {
            override fun onValueChanged(value: Int) {
                Toast.makeText(applicationContext, "value= $value", Toast.LENGTH_SHORT).show()
            }
        })

        // slider_4
        slider_4.setOnValueChangedListener(object : DiscreteSeekBar.OnValueChangedListener {
            override fun onValueChanged(value: Int) {
                Toast.makeText(applicationContext, "value= $value", Toast.LENGTH_SHORT).show()
            }
        })

        // slider_5
        slider_5.setOnValueChangedListener(object : DiscreteSeekBar.OnValueChangedListener {
            override fun onValueChanged(value: Int) {
                Toast.makeText(applicationContext, "value= $value", Toast.LENGTH_SHORT).show()
            }
        })




    }
}
