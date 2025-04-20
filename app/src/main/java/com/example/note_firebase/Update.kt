package com.example.note_firebase

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.note_firebase.databinding.ActivityUpdateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Update : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve data passed from the previous screen
        val noteId = intent.getStringExtra("note_id") ?: "" // This should be passed properly
        if (noteId.isEmpty()) {
            Toast.makeText(this, "Error: Note ID is missing.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val noteTitle = intent.getStringExtra("note_title")
        val noteContent = intent.getStringExtra("note_content")
        val noteTime = intent.getStringExtra("note_time")

        // Set the existing note details in the EditText fields
        binding.updatetitleEditText.setText(noteTitle)
        binding.updatecontentEditText.setText(noteContent)

        // Initialize the database reference
        database = FirebaseDatabase.getInstance().getReference("NotesInformation")

        // Handle Save button click to update the note
        binding.updatesaveButton.setOnClickListener {
            val updatedTitle = binding.updatetitleEditText.text.toString().trim()
            val updatedContent = binding.updatecontentEditText.text.toString().trim()
            val updatedTime = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())

            if (updatedTitle.isNotEmpty() && updatedContent.isNotEmpty()) {
                updateData(noteId, updatedTitle, updatedContent, updatedTime)
            } else {
                Toast.makeText(this, "Please Fill The Info First...!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateData(noteId: String, updatedTitle: String, updatedContent: String, updatedTime: String) {
        // Create a map of updated values
        val updatedNote = mapOf<String, Any>(
            "title" to updatedTitle,
            "content" to updatedContent,
            "time" to updatedTime
        )

        // Update the existing note with the same note_id in Firebase
        database.child(noteId).updateChildren(updatedNote).addOnSuccessListener {
            Toast.makeText(this, "Note Updated Successfully...!", Toast.LENGTH_LONG).show()
            finish()  // Close activity after success
        }.addOnFailureListener {
            Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
        }
    }
}
