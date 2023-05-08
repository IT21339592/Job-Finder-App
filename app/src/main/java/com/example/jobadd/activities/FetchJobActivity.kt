package com.example.jobadd.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobadd.R
import com.example.jobadd.adapters.JobAdapter
import com.example.jobadd.models.Jobs
import com.google.firebase.database.*

class FetchJobActivity : AppCompatActivity() {
    private lateinit var jobsRecyclerView : RecyclerView
    private lateinit var btnAddJob : Button
    private lateinit var jobList :ArrayList<Jobs>
    private lateinit var dbRef : DatabaseReference
    private lateinit var back : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_job)

        jobsRecyclerView = findViewById(R.id.rvJobs)
        jobsRecyclerView.layoutManager = LinearLayoutManager(this)
        jobsRecyclerView.setHasFixedSize(true)

        btnAddJob = findViewById(R.id.btnAddJob)

        btnAddJob.setOnClickListener{
            var intent = Intent(this@FetchJobActivity, PostJobActivity::class.java)
            startActivity(intent)
        }

        jobList = arrayListOf<Jobs>()

        getJobData()
        back = findViewById(R.id.btnJBack)

        back.setOnClickListener{
            var intent = Intent(this@FetchJobActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getJobData(){
        jobsRecyclerView.visibility = View.GONE

        dbRef = FirebaseDatabase.getInstance().getReference("Jobs")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                jobList.clear()

                if(snapshot.exists()){
                    for(jobSnap in snapshot.children){
                        val jobData = jobSnap.getValue(Jobs::class.java)
                        jobList.add(jobData!!)
                    }

                    val mAdapter = JobAdapter(jobList)
                    jobsRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : JobAdapter.OnItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@FetchJobActivity, JobDetailActivity::class.java)
                            intent.putExtra("jobName", jobList[position].jobName)
                            intent.putExtra("company", jobList[position].company)
                            intent.putExtra("jobDes", jobList[position].jobDes)
                            intent.putExtra("budget", jobList[position].budget)
                            intent.putExtra("startDate", jobList[position].startDate)

                            startActivity(intent)
                        }
                    })

                    jobsRecyclerView.visibility = View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            })
}
}