package es.i12capea.rickypedia.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.lifecycleScope
import es.i12capea.rickypedia.R
import kotlinx.android.synthetic.main.activity_splash_screen.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        motion_splash.addTransitionListener(transitionListener)

    }

    override fun onDestroy() {
        super.onDestroy()
        motion_splash.removeTransitionListener(transitionListener)
    }

    private val transitionListener = object : MotionLayout.TransitionListener{
        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            Log.d("Transition", "Transition: started")
        }

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            Log.d("Transition", "Transition: change")

        }

        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
            Log.d("Transition", "Transition: completed")
            lifecycleScope.launch(Dispatchers.IO){
                delay(500)
                withContext(Dispatchers.Main){
                    val intent = Intent(this@SplashScreen, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            Log.d("Transition", "Transition: triggered")
        }
    }
}