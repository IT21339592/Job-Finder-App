package com.example.jobadd.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.jobadd.R
import com.example.jobportal.models.Profile
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateProfileActivity : AppCompatActivity() {
    private lateinit var etFullName : EditText
    private lateinit var etUserEmail : EditText
    private lateinit var etUserName : EditText
    private lateinit var etPhoneNumber : EditText
    private lateinit var etAddress : EditText
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)

        etFullName = findViewById(R.id.etFullName)
        etUserEmail = findViewById(R.id.etUserEmail)
        etUserName = findViewById(R.id.etUserName)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etAddress = findViewById(R.id.etAddress)
        database = FirebaseDatabase.getInstance().reference
    }

    fun saveTransaction(view: View){
        val fullName = etFullName.text.toString().trim()
        val userEmail = etUserEmail.text.toString().trim()
        val userName = etUserName.text.toString().trim()
        val phoneNumber = etPhoneNumber.text.toString().trim()
        val address = etAddress.text.toString().trim()

        val profile = Profile(fullName, userEmail, userName, address, phoneNumber)

        val profileId = database.child("Profiles").push().key

        database.child("Profiles").child(profileId!!).setValue(profile)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile creation successful", Toast.LENGTH_SHORT).show()
                var intent = Intent(this@CreateProfileActivity, FetchProfileActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener{
                Toast.makeText(this, "Failed to perform profile action", Toast.LENGTH_SHORT).show()
            }
    }
}