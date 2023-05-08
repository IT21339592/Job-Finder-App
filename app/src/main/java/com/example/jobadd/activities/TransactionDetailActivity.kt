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
import com.example.jobadd.models.Transactions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TransactionDetailActivity : AppCompatActivity() {
    private lateinit var edTName : EditText
    private lateinit var tvTransName : TextView
    private lateinit var edTNumber : EditText
    private lateinit var edTBranch : EditText
    private lateinit var edTAmount : EditText
    private lateinit var btnTUpdate : Button
    private lateinit var btnTDelete : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_detail)

        edTName = findViewById(R.id.etTName)
        tvTransName = findViewById(R.id.tvTransName)
        edTNumber = findViewById(R.id.etTNumber)
        edTBranch = findViewById(R.id.etTBranch)
        edTAmount = findViewById(R.id.etTAmount)

        btnTUpdate = findViewById(R.id.btnTUpdate)
        btnTDelete = findViewById(R.id.btnTDelete)

        edTName.setText(intent.getStringExtra("accName"))
        tvTransName.text = intent.getStringExtra("transName")
        edTAmount.setText(intent.getDoubleExtra("accAmount", 0.0).toString())
        edTBranch.setText(intent.getStringExtra("accBranch"))
        edTNumber.setText(intent.getStringExtra("accNumber"))

        btnTUpdate.setOnClickListener{
            updateTransaction()
        }

        btnTDelete.setOnClickListener {
            deleteTransaction(tvTransName.text.toString())
        }
    }

    private fun updateTransaction() {
        val transName = tvTransName.text.toString().trim()
        val accName = edTName.text.toString().trim()
        val accNumber = edTNumber.text.toString().trim()
        val accBranch = edTBranch.text.toString().trim()
        val amount = edTAmount.text.toString().toDoubleOrNull() ?: 0.0

        if (transName.isNotEmpty() && accName.isNotEmpty() && accNumber.isNotEmpty() && accBranch.isNotEmpty() && amount > 0.0) {
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("Transactions")

            ref.orderByChild("transactionName").equalTo(transName)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // If the child node exists, update it
                            for (projectSnapshot in dataSnapshot.children) {
                                val transaction = projectSnapshot.getValue(Transactions::class.java)
                                if (transaction != null) {
                                    transaction.transactionName = transName
                                    transaction.accHolderName = accName
                                    transaction.accNumber = accNumber
                                    transaction.branch = accBranch
                                    transaction.amount = amount
                                    // Update the record in the database
                                    projectSnapshot.ref.setValue(transaction)

                                    Toast.makeText(applicationContext, "Transaction Data Updated", Toast.LENGTH_LONG).show()
                                    val intent = Intent(applicationContext, FetchTransactionActivity::class.java)
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

    private fun deleteTransaction(id: String){
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Transactions")

        // Find the child node with the projectName value
        ref.orderByChild("transactionName").equalTo(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // If the child node exists, delete it
                    for (projectSnapshot in dataSnapshot.children) {
                        projectSnapshot.ref.removeValue()
                        Toast.makeText(applicationContext, "Transaction Data Deleted", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // If the child node doesn't exist, log an error message or show an alert to the user
                    Log.e("DeleteProject", "Transaction data not found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
                Log.e("DeleteProject", databaseError.message)
            }
        })

        val intent = Intent(this, FetchTransactionActivity::class.java)
        finish()
        startActivity(intent)
        }
}