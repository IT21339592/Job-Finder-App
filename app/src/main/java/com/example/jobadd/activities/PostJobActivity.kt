package com.example.jobadd.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.jobadd.R
import com.example.jobadd.models.Jobs
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PostJobActivity : AppCompatActivity() {
    private lateinit var etJobName: EditText
    private lateinit var etCompany: EditText
    private lateinit var etJobDes: EditText
    private lateinit var etJobStartDate: EditText
    private lateinit var etJobBudget: EditText
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_job)

        etJobName = findViewById(R.id.etJobName)
        etCompany = findViewById(R.id.etCompany)
        etJobDes = findViewById(R.id.etJobDes)
        etJobStartDate = findViewById(R.id.edStartDate)
        etJobBudget = findViewById(R.id.etBudget)
        database = FirebaseDatabase.getInstance().reference
    }

    fun saveJob(view: View) {
        val jobName = etJobName.text.toString().trim()
        val company = etCompany.text.toString().trim()
        val jobDes = etJobDes.text.toString().trim()
        val jobStartDate = etJobStartDate.text.toString().trim()
        val jobBudget = etJobBudget.text.toString().trim()
        val budget = jobBudget.toDouble()

        val job = Jobs(jobName, company, jobDes, jobStartDate, budget)

        val jobId = database.child("Jobs").push().key

        database.child("Jobs").child(jobId!!).setValue(job)
            .addOnSuccessListener {
                Toast.makeText(this, "Job saved successfully", Toast.LENGTH_SHORT).show()
                var intent = Intent(this@PostJobActivity, FetchJobActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to save job", Toast.LENGTH_SHORT).show()
            }
    }
}