package com.app.barclaysdemo

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.app.barclaysdemo.ui.stock.StockListActivity

//2LZIR28XCD78DV1F API Key
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val timer=object:CountDownTimer(3000,1000){
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                Intent(this@SplashActivity,StockListActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }

        }
        timer.start()
    }
}