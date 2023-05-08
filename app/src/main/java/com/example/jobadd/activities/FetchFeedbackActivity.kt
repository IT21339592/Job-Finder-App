package com.example.jobadd.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobadd.R
import com.example.jobadd.adapters.FeedbackAdapter
import com.example.jobadd.models.Feedbacks
import com.google.firebase.database.*

class FetchFeedbackActivity : AppCompatActivity() {
    private lateinit var feedbackRecyclerView : RecyclerView
    private lateinit var btnAddFeedback : Button
    private lateinit var feedbackList :ArrayList<Feedbacks>
    private lateinit var dbRef : DatabaseReference
    private lateinit var back : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_feedback)
        feedbackRecyclerView = findViewById(R.id.rvFeedback)
        feedbackRecyclerView.layoutManager = LinearLayoutManager(this)
        feedbackRecyclerView.setHasFixedSize(true)

        btnAddFeedback = findViewById(R.id.btnAddFeedback)

        btnAddFeedback.setOnClickListener{
            var intent = Intent(this@FetchFeedbackActivity, CreateFeedbackActivity::class.java)
            startActivity(intent)
        }

        feedbackList = arrayListOf<Feedbacks>()

        getFeedbackData()

        back = findViewById(R.id.btnFBack)

        back.setOnClickListener{
            var intent = Intent(this@FetchFeedbackActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getFeedbackData(){
        feedbackRecyclerView.visibility = View.GONE

        dbRef = FirebaseDatabase.getInstance().getReference("Feedbacks")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                feedbackList.clear()

                if(snapshot.exists()){
                    for(feedbackSnap in snapshot.children){
                        val feedbackData = feedbackSnap.getValue(Feedbacks::class.java)
                        feedbackList.add(feedbackData!!)
                    }

                    val mAdapter = FeedbackAdapter(feedbackList)
                    feedbackRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : FeedbackAdapter.OnItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@FetchFeedbackActivity, FeedbackDetailActivity::class.java)
                            intent.putExtra("name", feedbackList[position].name)
                            intent.putExtra("email", feedbackList[position].email)
                            intent.putExtra("feedback", feedbackList[position].feedback)
                            intent.putExtra("rate", feedbackList[position].rate)

                            startActivity(intent)
                        }
                    })

                    feedbackRecyclerView.visibility = View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}
