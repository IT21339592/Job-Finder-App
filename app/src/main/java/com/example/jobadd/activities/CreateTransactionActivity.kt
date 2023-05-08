package com.example.jobadd.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.jobadd.R
import com.example.jobadd.models.Transactions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateTransactionActivity : AppCompatActivity() {
    private lateinit var etAccName: EditText
    private lateinit var etTransName: EditText
    private lateinit var etAccNumber: EditText
    private lateinit var etBranch: EditText
    private lateinit var etAmount: EditText
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_transaction)

        etTransName = findViewById(R.id.etTransName)
        etAccName = findViewById(R.id.etAccName)
        etAccNumber = findViewById(R.id.etAccNo)
        etBranch = findViewById(R.id.etBranch)
        etAmount = findViewById(R.id.etAmount)
        database = FirebaseDatabase.getInstance().reference
    }

    fun saveTransaction(view: View) {
        val transName = etTransName.text.toString().trim()
        val accName = etAccName.text.toString().trim()
        val accNumber = etAccNumber.text.toString().trim()
        val accBranch = etBranch.text.toString().trim()
        val paymentAmount = etAmount.text.toString().trim()
        val payment = paymentAmount.toDouble()

        val transaction = Transactions(transName, accName, accNumber, accBranch, payment)

        val transactionId = database.child("Transactions").push().key

        database.child("Transactions").child(transactionId!!).setValue(transaction)
            .addOnSuccessListener {
                Toast.makeText(this, "Transaction successful", Toast.LENGTH_SHORT).show()
                var intent =
                    Intent(this@CreateTransactionActivity, FetchTransactionActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to perform transaction", Toast.LENGTH_SHORT).show()
            }
    }
}