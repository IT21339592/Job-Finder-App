package com.example.jobadd.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobadd.R
import com.example.jobadd.adapters.ProfileAdapter
import com.example.jobadd.models.Profile
import com.google.firebase.database.*

class FetchProfileActivity : AppCompatActivity() {
    private lateinit var profileRecyclerView : RecyclerView
    private lateinit var btnAddProfile : Button
    private lateinit var profileList :ArrayList<Profile>
    private lateinit var dbRef : DatabaseReference
    private lateinit var back : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_profile)
        profileRecyclerView = findViewById(R.id.rvProfile)
        profileRecyclerView.layoutManager = LinearLayoutManager(this)
        profileRecyclerView.setHasFixedSize(true)

        btnAddProfile = findViewById(R.id.btnProfileAdd)

        btnAddProfile.setOnClickListener{
            var intent = Intent(this@FetchProfileActivity, CreateProfileActivity::class.java)
            startActivity(intent)
        }

        profileList = arrayListOf<Profile>()

        getProfileData()

        back = findViewById(R.id.btnPBack)

        back.setOnClickListener{
            var intent = Intent(this@FetchProfileActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getProfileData(){
        profileRecyclerView.visibility = View.GONE

        dbRef = FirebaseDatabase.getInstance().getReference("Profiles")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                profileList.clear()

                if(snapshot.exists()){
                    for(profileSnap in snapshot.children){
                        val profileData = profileSnap.getValue(Profile::class.java)
                        profileList.add(profileData!!)
                    }

                    val mAdapter = ProfileAdapter(profileList)
                    profileRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : ProfileAdapter.OnItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@FetchProfileActivity, ProfileDetailActivity::class.java)
                            intent.putExtra("fullName", profileList[position].fullName)
                            intent.putExtra("userEmail", profileList[position].useremail)
                            intent.putExtra("userName", profileList[position].username)
                            intent.putExtra("phoneNumber", profileList[position].phoneNumber)
                            intent.putExtra("address", profileList[position].address)

                            startActivity(intent)
                        }
                    })

                    profileRecyclerView.visibility = View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}