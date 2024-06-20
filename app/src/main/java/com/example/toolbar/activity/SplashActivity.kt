package com.example.toolbar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.toolbar.Login
import com.example.toolbar.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val splashTime: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        //Gerando um atraso com Coroutine
        CoroutineScope(Dispatchers.Main).launch {
            delay(splashTime)

            val intent = Intent(this@SplashActivity,
                Login::class.java)
            startActivity(intent)
            finish()
        }


    /*    //Splash - primeira forma
        Handler().postDelayed({
            val intent = intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }, 2000)*/
    }
}