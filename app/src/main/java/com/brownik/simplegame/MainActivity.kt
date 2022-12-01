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
    private var buttonStateViewModel = ButtonStateViewModel()
    private var remainTimeViewModel = RemainTimeViewModel()
    private var isGaming = false

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setGameLayerSideSize()

        // buttonState 뷰 모델 설정
        buttonStateViewModel = ViewModelProvider(
            this,
            ViewModelFactory(buttonStateViewModel)
        )[ButtonStateViewModel::class.java]

        // remainTime 뷰 모델 설정
        remainTimeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(remainTimeViewModel)
        )[RemainTimeViewModel::class.java]

        // LiveData 등록
        setButtonStateLiveData()
        setRemainTimeLiveData()

        // liveData 초기화
        buttonStateViewModel.initButtonStateData()
        remainTimeViewModel.initRemainTime()

        addOnClickListener()
    }

    private fun addOnClickListener() = with(binding) {
        gameButton.setOnClickListener {
            when (isGaming) {
                true -> stopGame()
                false -> startGame()
            }
        }
    }

    // 버튼 상태 라이브데이터
    private fun setButtonStateLiveData() = with(binding) {
        buttonStateViewModel.buttonStateData.observe(this@MainActivity, Observer {
            buttonStateDataBinding = buttonStateViewModel
        })
    }

    // 타이머 라이브데이터
    private fun setRemainTimeLiveData() = with(binding) {
        remainTimeViewModel.remainTime.observe(this@MainActivity, Observer {
            remainTimeDataBinding = remainTimeViewModel
        })
    }

    // 화면 비율에 맞춰 게임 Layer 크기를 맞추는 함수
    private fun setGameLayerSideSize() = with(binding) {
        val metrics = resources.displayMetrics
        val screenHeight = metrics.widthPixels
        val layoutParams = buttonLayer.layoutParams
        layoutParams.height = screenHeight
        layoutParams.width = screenHeight
        buttonLayer.layoutParams = layoutParams
    }

    // Log 만드는 함수
    private fun makeLog(text: String) = Log.d("qwe123", text)

    // 게임을 시작하는 함수
    private fun startGame() = with(binding) {
        isGaming = true
        var gamingJob: Job? = null
        gameButton.text = "STOP"

        // 타이머 및 게임 시작 코루틴
        CoroutineScope(Dispatchers.IO).launch {
            remainTimeViewModel.startTimer()
            gamingJob = makeRandomButton()
        }

        // 채널을 통해 true 값이 들어오면 타이머 및 게임 리셋
        CoroutineScope(Dispatchers.IO).launch {
            if (remainTimeViewModel.timerChannel.receive()) {
                makeLog("startGame.receive")
                stopGame()
                gamingJob?.cancel()
            }
        }
    }

    // 게임을 멈추는 함수
    private fun stopGame() = with(binding) {
        remainTimeViewModel.stopTimer()
        buttonStateViewModel.initButtonBackground()
        isGaming = false
        gameButton.text = "START"
    }

    // 버튼을 계속해서 바꾸는 함수
    private suspend fun makeRandomButton() = coroutineScope {
        launch {
            while (isGaming) {
                buttonStateViewModel.changeImage()
                delay(makeRandomTime())
            }
        }
    }

    // 버튼을 바꾸기 위한 시간을 랜덤으로 정하는 함수
    private fun makeRandomTime(): Long = (200..500).random().toLong()
}