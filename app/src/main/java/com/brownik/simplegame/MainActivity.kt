package com.brownik.simplegame

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.brownik.simplegame.databinding.ActivityMainBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var buttonStateViewModel: ButtonStateViewModel
    private lateinit var remainTimeAndScoreViewModel: RemainTimeAndScoreViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setGameLayerSideSize()

        buttonStateViewModel = ViewModelProvider(
            this,
            ButtonStateViewModelFactory()
        )[ButtonStateViewModel::class.java]
        setButtonStateLiveData()

        remainTimeAndScoreViewModel = ViewModelProvider(
            this,
            RemainTimeAndScoreViewModelFactory()
        )[RemainTimeAndScoreViewModel::class.java]
        setRemainTimeAndScoreLiveData()

        buttonStateViewModel.initButtonStateData()

        addOnClickListener()
    }

    private fun addOnClickListener() = with(binding) {
        startButton.setOnClickListener {

        }
    }

    private fun setButtonStateLiveData() = with(binding) {
        buttonStateViewModel.buttonStateData.observe(this@MainActivity, Observer {
            buttonState = buttonStateViewModel
        })
    }

    private fun setRemainTimeAndScoreLiveData() = with(binding) {
        remainTimeAndScoreViewModel.remainTime.observe(this@MainActivity, Observer {
            invalidateAll()
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

    private suspend fun startGame() {
        coroutineScope {
            val job = CoroutineScope(Dispatchers.IO).launch {
                buttonStateViewModel.changeImage()
            }
        }

    }
}
