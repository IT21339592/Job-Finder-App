package com.example.jobadd.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.jobadd.R

class MainActivity : AppCompatActivity() {
    private lateinit var btnProfile : Button
    private lateinit var btnJob : Button
    private lateinit var btnFeedback : Button
    private lateinit var btnTransaction : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnProfile = findViewById(R.id.btn_profile)
        btnFeedback = findViewById(R.id.btn_feedback)
        btnJob = findViewById(R.id.btn_post_job)
        btnTransaction = findViewById(R.id.btn_payment)

        btnJob.setOnClickListener{
            var intent = Intent(this@MainActivity, FetchJobActivity::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener{
            var intent = Intent(this@MainActivity, FetchProfileActivity::class.java)
            startActivity(intent)
        }

        btnFeedback.setOnClickListener{
            var intent = Intent(this@MainActivity, FetchFeedbackActivity::class.java)
            startActivity(intent)
        }

        btnTransaction.setOnClickListener{
            var intent = Intent(this@MainActivity, FetchTransactionActivity::class.java)
            startActivity(intent)
        }
    }
}