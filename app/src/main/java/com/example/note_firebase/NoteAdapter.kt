package com.example.note_firebase

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class NoteAdapter(private val context: Context, private val noteList: MutableList<Notes>) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.titleTextHeading)
        val contentText: TextView = itemView.findViewById(R.id.content)
        val up: ImageView = itemView.findViewById(R.id.updateButton)
        val de: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = noteList.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.titleText.text = note.title
        holder.contentText.text = note.content

        holder.up.setOnClickListener {
            val intent = Intent(context, Update::class.java)
            intent.putExtra("note_id", note.id)
            intent.putExtra("note_title", note.title)
            intent.putExtra("note_content", note.content)
            intent.putExtra("note_time", note.time)
            context.startActivity(intent)
        }
        holder.de.setOnClickListener {
            //deleteNoteFromFirebase(note.id?:return@setOnClickListener, position)
            AlertDialog.Builder(context).apply {
                setTitle("Delete Note")
                setMessage("Are You Sure You Want To Delete This Note?")
                setPositiveButton("Yes"){ _, _ -> deleteNoteFromFirebase(note.id?:return@setPositiveButton, position)
                }
                setNeutralButton("No",null)
                show()
            }
        }
    }

    private fun deleteNoteFromFirebase(noteId: String, position: Int) {
        val databaseReference=FirebaseDatabase.getInstance().getReference("NotesInformation")
        databaseReference.child(noteId).removeValue().addOnCompleteListener {task->
            if(task.isSuccessful){
                Toast.makeText(context,"Note Deleted Successfully...!",Toast.LENGTH_LONG).show()
                noteList.removeAt(position)
                notifyItemRemoved(position)
            }
            else{
                Toast.makeText(context,"Erroror....!",Toast.LENGTH_LONG).show()
            }
        }
    }


    fun updateList(newList: List<Notes>) {
        noteList.clear()
        noteList.addAll(newList)
        notifyDataSetChanged()
    }
}
