package com.brownik.simplegame

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brownik.simplegame.data.viewmodel.ButtonStateViewModel
import com.brownik.simplegame.data.viewmodel.RemainTimeViewModel
import com.brownik.simplegame.data.viewmodel.ScoreViewModel
import com.brownik.simplegame.data.viewmodelfactory.ViewModelFactory
import com.brownik.simplegame.databinding.ActivityGameBinding
import kotlinx.coroutines.*

class GameActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityGameBinding
    private var buttonStateViewModel = ButtonStateViewModel()
    private var remainTimeViewModel = RemainTimeViewModel()
    private var scoreViewModel = ScoreViewModel()
    private var isGaming = false

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game)
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

        // scoreViewModel 뷰 모델 설정
        scoreViewModel = ViewModelProvider(
            this,
            ViewModelFactory(scoreViewModel)
        )[ScoreViewModel::class.java]

        // LiveData 등록
        setButtonStateLiveData()
        setRemainTimeLiveData()
        setScoreLiveData()

        // liveData 초기화
        buttonStateViewModel.initButtonStateData()
        remainTimeViewModel.initTimer()

        setButtonOnClickListener()
        addOnClickListener()
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
            score = buttonStateViewModel.onButtonClick(position)
        }
        scoreViewModel.totalScore(score, totalScore)
        addScoreAnimator()
    }

    // scoreChannel에 쌓인 데이터를 받아 Animator를 start 해주는 함수
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

    // 버튼 상태 라이브데이터
    private fun setButtonStateLiveData() = with(binding) {
        buttonStateViewModel.buttonStateData.observe(this@GameActivity, Observer {
            buttonStateDataBinding = buttonStateViewModel
        })
    }

    // 타이머 라이브데이터
    private fun setRemainTimeLiveData() = with(binding) {
        remainTimeViewModel.remainTime.observe(this@GameActivity, Observer {
            remainTimeDataBinding = remainTimeViewModel
        })
    }

    // 점수 라이브데이터
    private fun setScoreLiveData() = with(binding) {
        scoreViewModel.score.observe(this@GameActivity, Observer {
            scoreDataBinding = scoreViewModel
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

    // 게임을 시작하는 함수
    private fun startGame() = with(binding) {
        MyObject.makeLog("startGame function")
        isGaming = true
        var gamingJob: Job? = null
        gameButton.text = "STOP"
        scoreViewModel.initScore()

        // 타이머 및 게임 시작 코루틴
        CoroutineScope(Dispatchers.IO).launch {
            MyObject.makeLog("startGame.game coroutine")
            remainTimeViewModel.startTimer()
            gamingJob = makeRandomButton()
        }

        // 채널을 통해 true 값이 들어오면 타이머 및 게임 리셋
        CoroutineScope(Dispatchers.IO).launch {
            if (remainTimeViewModel.timerChannel.receive()) {
                MyObject.makeLog("startGame.stopState receive coroutine")
                stopGame()
                gamingJob?.cancel()
            }
        }
    }

    // 게임을 종료하는 함수
    private fun stopGame() = with(binding) {
        remainTimeViewModel.stopTimer()
        buttonStateViewModel.initButtonBackground()
        isGaming = false
        gameButton.text = "START"
    }

    // 게임이 종료될 때까지 버튼을 바꾸는 함수
    private suspend fun makeRandomButton() = coroutineScope {
        MyObject.makeLog("makeRandomButton")
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