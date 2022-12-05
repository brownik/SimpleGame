package com.brownik.simplegame

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brownik.simplegame.data.viewmodel.ButtonScoreViewModel
import com.brownik.simplegame.data.viewmodel.RemainTimeViewModel
import com.brownik.simplegame.data.viewmodel.ScoreViewModel
import com.brownik.simplegame.data.viewmodel.TimerState
import com.brownik.simplegame.data.viewmodelfactory.ViewModelFactory
import com.brownik.simplegame.databinding.ActivityGameBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class GameActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityGameBinding
    private val buttonScoreViewModel: ButtonScoreViewModel by viewModels()
    private val remainTimeViewModel: RemainTimeViewModel by viewModels()
    private val scoreViewModel: ScoreViewModel by viewModels()
    private var isGaming = false

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setViewModel()
        collectViewModel()
        setGameLayerSideSize()
        setButtonOnClickListener()
        addOnClickListener()
    }

    private fun setViewModel() {
        binding.lifecycleOwner = this
        binding.buttonStateDataBinding = buttonScoreViewModel
        binding.remainTimeDataBinding = remainTimeViewModel
        binding.scoreDataBinding = scoreViewModel
    }

    private fun collectViewModel() {
        CoroutineScope(Dispatchers.IO).launch {
            remainTimeViewModel.timerState.collect {
                when (it) {
                    is TimerState.Start -> {

                    }
                    is TimerState.End -> stopGame()
                }
            }
        }
    }

    // 시작 버튼 이벤트 세팅 함수
    private fun addOnClickListener() = with(binding) {
        gameButton.setOnClickListener {
            when (isGaming) {
                true -> stopGame()
                false -> startGame()
            }
        }
    }

    // 버튼에 clickListener 세팅 함수
    private fun setButtonOnClickListener() = with(binding) {
        button0.setOnClickListener(this@GameActivity)
        button1.setOnClickListener(this@GameActivity)
        button2.setOnClickListener(this@GameActivity)
        button3.setOnClickListener(this@GameActivity)
        button4.setOnClickListener(this@GameActivity)
        button5.setOnClickListener(this@GameActivity)
        button6.setOnClickListener(this@GameActivity)
        button7.setOnClickListener(this@GameActivity)
        button8.setOnClickListener(this@GameActivity)
    }

    // 숫자 버튼 이벤트 세팅 함수
    override fun onClick(view: View) = with(binding) {
        val position = when (view) {
            button0 -> 0
            button1 -> 1
            button2 -> 2
            button3 -> 3
            button4 -> 4
            button5 -> 5
            button6 -> 6
            button7 -> 7
            button8 -> 8
            else -> null
        }
        var score = 0
        if (position != null) {
            score = buttonScoreViewModel.onButtonClick(position)
        }
        scoreViewModel.totalScore(score, totalScore)
        addScoreAnimator()
    }

    // scoreChannel에 쌓인 데이터를 받아 Animator를 start 해주는 함수
    @SuppressLint("Recycle")
    private fun addScoreAnimator() {
        CoroutineScope(Dispatchers.Main).launch {
            launch {
                scoreViewModel.scoreValueAnimatorChannel.receive().start()
            }
            launch {
                scoreViewModel.scoreObjectAnimatorChannel.receive().start()
            }
        }
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

    // 게임을 시작하는 함수
    private fun startGame() = with(binding) {
        MyObject.makeLog("startGame function")
        changeGameState(true)
        gameButton.text = "STOP"
        scoreViewModel.initScore()
//            remainTimeViewModel.startTimerUseChannel()
        buttonScoreViewModel.startGame()
        remainTimeViewModel.startTimer()

        /*// 채널을 통해 true 값이 들어오면 타이머 및 게임 초기화
        CoroutineScope(Dispatchers.IO).launch {
            if (remainTimeViewModel.timerChannel.receive()) {
                MyObject.makeLog("startGame.stopState receive coroutine")
                job.cancel()
                stopGame()
                gamingJob?.cancel()
            }
        }*/
    }

    // 게임을 종료하는 함수
    private fun stopGame() = with(binding) {
//        remainTimeViewModel.stopTimerUseChannel()
        changeGameState(false)
        buttonScoreViewModel.stopGame()
        remainTimeViewModel.initTimer()
        gameButton.text = "START"
    }

    private fun changeGameState(isStartGame: Boolean) {
        isGaming = isStartGame
        buttonScoreViewModel.changeGameState(isStartGame)
        remainTimeViewModel.changeGameState(isStartGame)
    }

    // 게임이 종료될 때까지 버튼을 바꾸는 함수
    private suspend fun makeRandomButton() = coroutineScope {
        MyObject.makeLog("makeRandomButton")
        launch {
            while (isGaming) {
                buttonScoreViewModel.changeButtonScore()
                delay(makeRandomTime())
            }
        }
    }

    // 버튼을 바꾸기 위한 시간을 랜덤으로 정하는 함수
    private fun makeRandomTime(): Long = (0..500).random().toLong()
}