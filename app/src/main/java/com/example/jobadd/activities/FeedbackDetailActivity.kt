package com.example.jobadd.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.jobadd.R
import com.example.jobadd.models.Feedbacks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FeedbackDetailActivity : AppCompatActivity() {
    private lateinit var edName : EditText
    private lateinit var edEmail : TextView
    private lateinit var edFeedback : EditText
    private lateinit var edRate : EditText
    private lateinit var btnTUpdate : Button
    private lateinit var btnTDelete : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_detail)

        edName = findViewById(R.id.etUName)
        edEmail = findViewById(R.id.etUEmail)
        edFeedback = findViewById(R.id.etUFeedback)
        edRate = findViewById(R.id.etURate)

        btnTUpdate = findViewById(R.id.btnTUpdate)
        btnTDelete = findViewById(R.id.btnTDelete)

        edName.setText(intent.getStringExtra("name"))
        edEmail.text = intent.getStringExtra("email")
        edRate.setText(intent.getIntExtra("rate", 0).toString())
        edFeedback.setText(intent.getStringExtra("feedback"))

        btnTUpdate.setOnClickListener{
            updateFeedback()
        }

        btnTDelete.setOnClickListener {
            deleteFeedback(edEmail.text.toString())
        }
    }

    private fun updateFeedback() {
        val name = edName.text.toString().trim()
        val email = edEmail.text.toString().trim()
        val feedback = edFeedback.text.toString().trim()
        val rate = edRate.text.toString().toIntOrNull() ?: 0

        if (name.isNotEmpty() && email.isNotEmpty() && feedback.isNotEmpty() && rate > 0) {
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("Feedbacks")

            ref.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // If the child node exists, update it
                            for (projectSnapshot in dataSnapshot.children) {
                                val newFeedback = projectSnapshot.getValue(Feedbacks::class.java)
                                if (newFeedback != null) {
                                    newFeedback.name = name
                                    newFeedback.email = email
                                    newFeedback.feedback = feedback
                                    newFeedback.rate = rate
                                    // Update the record in the database
                                    projectSnapshot.ref.setValue(newFeedback)

                                    Toast.makeText(applicationContext, "Feedback Data Updated", Toast.LENGTH_LONG).show()
                                    val intent = Intent(applicationContext, FetchFeedbackActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        } else {
                            // If the child node doesn't exist, log an error message or show an alert to the user
                            Log.e("UpdateProject", "Feedback not found")
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle errors here
                        Log.e("UpdateProject", databaseError.message)
                    }
                })
        } else {
            Toast.makeText(applicationContext, "Please enter valid details", Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteFeedback(id: String){
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Feedbacks")

        // Find the child node with the projectName value
        ref.orderByChild("email").equalTo(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // If the child node exists, delete it
                    for (projectSnapshot in dataSnapshot.children) {
                        projectSnapshot.ref.removeValue()
                        Toast.makeText(applicationContext, "Feedback Data Deleted", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // If the child node doesn't exist, log an error message or show an alert to the user
                    Log.e("DeleteProject", "Feedback data not found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
                Log.e("DeleteProject", databaseError.message)
            }
        })

        val intent = Intent(this, FetchFeedbackActivity::class.java)
        finish()
        startActivity(intent)
    }
}