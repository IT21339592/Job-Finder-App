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
import com.example.jobportal.models.Profile
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileDetailActivity : AppCompatActivity() {
    private lateinit var edUFullName : EditText
    private lateinit var tvUEmail : TextView
    private lateinit var edUUsername : EditText
    private lateinit var edUAddress : EditText
    private lateinit var edUPhone : EditText
    private lateinit var btnTUpdate : Button
    private lateinit var btnTDelete : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_detail)

        edUFullName = findViewById(R.id.tvUFullName)
        tvUEmail = findViewById(R.id.tvUUserEmail)
        edUUsername = findViewById(R.id.tvUUserName)
        edUAddress = findViewById(R.id.tvUAddress)
        edUPhone = findViewById(R.id.tvUPhoneNumber)

        btnTUpdate = findViewById(R.id.btnTUpdate)
        btnTDelete = findViewById(R.id.btnTDelete)

        edUFullName.setText(intent.getStringExtra("fullName"))
        tvUEmail.text = intent.getStringExtra("userEmail")
        edUUsername.setText(intent.getStringExtra("userName"))
        edUAddress.setText(intent.getStringExtra("address"))
        edUPhone.setText(intent.getStringExtra("phoneNumber"))

        btnTUpdate.setOnClickListener{
            updateProfile()
        }

        btnTDelete.setOnClickListener {
            deleteProfile(tvUEmail.text.toString())
        }
    }

    private fun updateProfile() {
        val newFullName = edUFullName.text.toString().trim()
        val newEmail = tvUEmail.text.toString().trim()
        val newAddress = edUAddress.text.toString().trim()
        val newPhone = edUPhone.text.toString().trim()
        val newUsername = edUUsername.text.toString().trim()

        if (newFullName.isNotEmpty() && newEmail.isNotEmpty() && newAddress.isNotEmpty() && newPhone.isNotEmpty() && newUsername.isNotEmpty()) {
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("Profiles")

            ref.orderByChild("useremail").equalTo(newEmail)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // If the child node exists, update it
                            for (projectSnapshot in dataSnapshot.children) {
                                val profileD = projectSnapshot.getValue(Profile::class.java)
                                if (profileD != null) {
                                    profileD.fullName = newFullName
                                    profileD.useremail = newEmail
                                    profileD.username = newUsername
                                    profileD.address = newAddress
                                    profileD.phoneNumber = newPhone
                                    // Update the record in the database
                                    projectSnapshot.ref.setValue(profileD)

                                    Toast.makeText(applicationContext, "Profile Updated", Toast.LENGTH_LONG).show()
                                    val intent = Intent(applicationContext, FetchProfileActivity::class.java)
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

    private fun deleteProfile(id: String){
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Profiles")

        // Find the child node with the projectName value
        ref.orderByChild("useremail").equalTo(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // If the child node exists, delete it
                    for (projectSnapshot in dataSnapshot.children) {
                        projectSnapshot.ref.removeValue()
                        Toast.makeText(applicationContext, "Profile Data Deleted", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // If the child node doesn't exist, log an error message or show an alert to the user
                    Log.e("DeleteProject", "Profile data not found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
                Log.e("DeleteProject", databaseError.message)
            }
        })

        val intent = Intent(this, FetchProfileActivity::class.java)
        finish()
        startActivity(intent)
    }
}