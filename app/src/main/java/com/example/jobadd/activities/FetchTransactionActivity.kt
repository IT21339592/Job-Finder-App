package com.example.jobadd.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobadd.R
import com.example.jobadd.adapters.TransactionAdapter
import com.example.jobadd.models.Transactions
import com.google.firebase.database.*

class FetchTransactionActivity : AppCompatActivity() {
    private lateinit var transactionsRecyclerView: RecyclerView
    private lateinit var btnAddTransaction: Button
    private lateinit var transactionList: ArrayList<Transactions>
    private lateinit var dbRef: DatabaseReference
    private lateinit var back : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_transaction)

        transactionsRecyclerView = findViewById(R.id.rvTransactions)
        transactionsRecyclerView.layoutManager = LinearLayoutManager(this)
        transactionsRecyclerView.setHasFixedSize(true)

        btnAddTransaction = findViewById(R.id.btnAddTransaction)

        btnAddTransaction.setOnClickListener {
            var intent =
                Intent(this@FetchTransactionActivity, CreateTransactionActivity::class.java)
            startActivity(intent)
        }

        transactionList = arrayListOf<Transactions>()

        getTransactionData()
        back = findViewById(R.id.btnTBack)

        back.setOnClickListener{
            var intent = Intent(this@FetchTransactionActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getTransactionData() {
        transactionsRecyclerView.visibility = View.GONE

        dbRef = FirebaseDatabase.getInstance().getReference("Transactions")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                transactionList.clear()

                if (snapshot.exists()) {
                    for (transactionSnap in snapshot.children) {
                        val transactionData = transactionSnap.getValue(Transactions::class.java)
                        transactionList.add(transactionData!!)
                    }

                    val mAdapter = TransactionAdapter(transactionList)
                    transactionsRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object :
                        TransactionAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(
                                this@FetchTransactionActivity,
                                TransactionDetailActivity::class.java
                            )
                            intent.putExtra("transName", transactionList[position].transactionName)
                            intent.putExtra("accName", transactionList[position].accHolderName)
                            intent.putExtra("accNumber", transactionList[position].accNumber)
                            intent.putExtra("accBranch", transactionList[position].branch)
                            intent.putExtra("accAmount", transactionList[position].amount)

                            startActivity(intent)
                        }
                    })

                    transactionsRecyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}
