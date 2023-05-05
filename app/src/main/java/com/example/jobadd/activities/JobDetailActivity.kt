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
import com.example.jobadd.models.Jobs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class JobDetailActivity : AppCompatActivity() {
    private lateinit var edJobDescription : EditText
    private lateinit var tvJobName : TextView
    private lateinit var edJobBudget : EditText
    private lateinit var edJobStartDate : EditText
    private lateinit var edJobCompany : EditText
    private lateinit var btnUpdate : Button
    private lateinit var btnDelete : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)

        edJobDescription = findViewById(R.id.etJobDescription)
        tvJobName = findViewById(R.id.tvJobName)
        edJobBudget = findViewById(R.id.etJobBudget)
        edJobStartDate = findViewById(R.id.etJobStartDate)
        edJobCompany = findViewById(R.id.etJobCompany)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)

        edJobDescription.setText(intent.getStringExtra("jobDes"))
        tvJobName.text = intent.getStringExtra("jobName")
        edJobBudget.setText(intent.getDoubleExtra("budget", 0.0).toString())
        edJobStartDate.setText(intent.getStringExtra("startDate"))
        edJobCompany.setText(intent.getStringExtra("company"))

        btnUpdate.setOnClickListener{
            updateJob()
        }

        btnDelete.setOnClickListener {
            deleteJob(tvJobName.text.toString())
        }
    }

    private fun updateJob() {
        val jobName = tvJobName.text.toString().trim()
        val jobDes = edJobDescription.text.toString().trim()
        val jobStartDate = edJobStartDate.text.toString().trim()
        val jobCompany = edJobCompany.text.toString().trim()
        val jobBudget = edJobBudget.text.toString().toDoubleOrNull() ?: 0.0

        if (jobName.isNotEmpty() && jobDes.isNotEmpty() && jobStartDate.isNotEmpty() && jobCompany.isNotEmpty() && jobBudget > 0.0) {
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("Jobs")

            ref.orderByChild("jobName").equalTo(jobName)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // If the child node exists, update it
                            for (projectSnapshot in dataSnapshot.children) {
                                val job = projectSnapshot.getValue(Jobs::class.java)
                                if (job != null) {
                                    job.jobName = jobName
                                    job.jobDes = jobDes
                                    job.budget = jobBudget
                                    job.startDate = jobStartDate
                                    job.company = jobCompany
                                    // Update the record in the database
                                    projectSnapshot.ref.setValue(job)

                                    Toast.makeText(applicationContext, "Job Data Updated", Toast.LENGTH_LONG).show()
                                    val intent = Intent(applicationContext, FetchJobActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        } else {
                            // If the child node doesn't exist, log an error message or show an alert to the user
                            Log.e("UpdateProject", "Project not found")
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

    private fun deleteJob(id: String){
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Jobs")

        // Find the child node with the projectName value
        ref.orderByChild("jobName").equalTo(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // If the child node exists, delete it
                    for (projectSnapshot in dataSnapshot.children) {
                        projectSnapshot.ref.removeValue()
                        Toast.makeText(applicationContext, "Job Data Deleted", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // If the child node doesn't exist, log an error message or show an alert to the user
                    Log.e("DeleteProject", "Project not found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
                Log.e("DeleteProject", databaseError.message)
            }
        })

        val intent = Intent(this, FetchJobActivity::class.java)
        finish()
        startActivity(intent)
        }
}