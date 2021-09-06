package com.example.carpool.progressbar

import android.animation.AnimatorInflater
import android.app.Activity
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.carpool.R
import com.example.carpool.databinding.ActivityAnimationBinding

class AnimationCar() : AppCompatActivity() {

    private lateinit var dialog: AlertDialog
    private lateinit var frameObjAnim: ConstraintLayout
    private lateinit var binding: ActivityAnimationBinding

    private lateinit var car: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)

        binding = ActivityAnimationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        frameObjAnim = findViewById(R.id.frameObjAnim)

        car = findViewById(R.id.imageViewCar)


        esquivar()

    }


    private fun esquivar() {
        AnimatorInflater.loadAnimator(this, R.animator.translate).apply {
            setTarget(binding.imageViewCar)
            start()
        }
    }

}