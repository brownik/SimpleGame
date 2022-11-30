package com.brownik.simplegame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.brownik.simplegame.databinding.ActivityMainBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var buttonStateViewModel: ButtonStateViewModel
    private lateinit var buttonStateList: List<ButtonStateData>

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        buttonStateList = makeButtonList()
        setGameLayerSideSize()

        buttonStateViewModel = ViewModelProvider(
            this,
            ButtonStateViewModelFactory()
        )[ButtonStateViewModel::class.java]
        setButtonStateLiveData()

        buttonStateViewModel.setData(buttonStateList.toMutableList())

        addOnClickListener()

    }

    private fun addOnClickListener() = with(binding) {
        startButton.setOnClickListener {
            buttonStateViewModel.setRandomData()
        }
    }

    private fun setButtonStateLiveData() = with(binding) {
        buttonStateViewModel.buttonStateData.observe(this@MainActivity, Observer {
            buttonState = buttonStateViewModel
        })
    }

    private fun setGameLayerSideSize() = with(binding) {
        val metrics = resources.displayMetrics
        val screenHeight = metrics.widthPixels
        val layoutParams = buttonLayer.layoutParams
        layoutParams.height = screenHeight
        layoutParams.width = screenHeight
        buttonLayer.layoutParams = layoutParams
    }

    private fun makeButtonList(): List<ButtonStateData> {
        return mutableListOf(
            ButtonStateData(),
            ButtonStateData(),
            ButtonStateData(),
            ButtonStateData(),
            ButtonStateData(),
            ButtonStateData(),
            ButtonStateData(),
            ButtonStateData(),
            ButtonStateData(),
        )
    }

}