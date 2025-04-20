package com.example.note_firebase

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_firebase.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    val noteList= mutableListOf<Notes>()
    private val allnotes = mutableListOf<Notes>()
    val adapter=NoteAdapter(this,noteList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addbutton.setOnClickListener {
            val intent=Intent(this@MainActivity,AddnoteActivity::class.java)
            startActivity(intent)
        }
        binding.s1.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //no actionf
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filternotes(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                //no action
            }

        })
        binding.l1.layoutManager=LinearLayoutManager(this@MainActivity)
        binding.l1.adapter=adapter
        FirebaseDatabase.getInstance().getReference("NotesInformation")
            .orderByChild("time")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tempList= mutableListOf<Notes>()
                    for(noteSnap in snapshot.children) {
                        val note = noteSnap.getValue(Notes::class.java)
                        if (note != null) {
                            tempList.add(note)
                        }
                    }
                        tempList.reverse()
                        allnotes.clear()
                        allnotes.addAll(tempList)
                        adapter.updateList(tempList)
                    }


                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MainActivity,"Erroror!!!!",Toast.LENGTH_LONG).show()
                }
            })
    }
    private fun filternotes(query: String) {
        val filteredList=allnotes.filter {
            it.title?.contains(query, ignoreCase = true) == true ||
            it.content?.contains(query, ignoreCase = true) == true
        }
        adapter.updateList(filteredList)
    }
}