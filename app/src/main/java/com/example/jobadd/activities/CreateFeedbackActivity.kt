package com.example.jobadd.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.jobadd.R
import com.example.jobadd.models.Feedbacks
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateFeedbackActivity : AppCompatActivity() {
    private lateinit var etName : EditText
    private lateinit var etEmail : EditText
    private lateinit var etFeedback : EditText
    private lateinit var etRate : EditText
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_feedback)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etFeedback = findViewById(R.id.etFeedback)
        etRate = findViewById(R.id.etRate)
        database = FirebaseDatabase.getInstance().reference
    }

    fun saveFeedback(view: View){
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val message = etFeedback.text.toString().trim()
        val rating = etRate.text.toString().trim()
        val rate = rating.toInt()

        val feedback = Feedbacks(name, email, message, rate)

        val feedbackId = database.child("Feedbacks").push().key

        database.child("Feedbacks").child(feedbackId!!).setValue(feedback)
            .addOnSuccessListener {
                Toast.makeText(this, "Feedback was submitted", Toast.LENGTH_SHORT).show()
                var intent = Intent(this@CreateFeedbackActivity, FetchFeedbackActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener{
                Toast.makeText(this, "Failed to submit", Toast.LENGTH_SHORT).show()
            }
    }
}