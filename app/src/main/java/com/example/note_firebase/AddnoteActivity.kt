package com.example.note_firebase

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.note_firebase.databinding.ActivityAddnoteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddnoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddnoteBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddnoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("NotesInformation")

        // Save button click listener
        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString().trim()
            val content = binding.contentEditText.text.toString().trim()
            val time = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val noteId = databaseReference.push().key ?: ""  // Generate a unique key
                val note = Notes(noteId, title, content, time)

                // Save the note to Firebase
                if (noteId.isNotEmpty()) {
                    databaseReference.child(noteId).setValue(note).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Note Added Successfully", Toast.LENGTH_LONG).show()
                            finish()  // Close activity on success
                        } else {
                            Toast.makeText(this, "Failed to add note: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please Fill The Details First", Toast.LENGTH_LONG).show()
            }
        }
    }
}
