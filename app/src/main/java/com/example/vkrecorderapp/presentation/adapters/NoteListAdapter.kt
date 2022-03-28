package com.example.vkrecorderapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.vkrecorderapp.databinding.NoteItemBinding
import com.example.vkrecorderapp.domain.entities.AudioNote

class NoteListAdapter: RecyclerView.Adapter<NoteListAdapter.NoteListHolder>() {

    var onPlayClickListener: ((AudioNote) -> (Unit))? = null
    var onClickListener: ((AudioNote) -> (Unit))? = null

    var notesList = listOf<AudioNote>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListHolder {
        return NoteListHolder(
            NoteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false))
    }

    override fun onBindViewHolder(holder: NoteListHolder, position: Int) {
        val note = notesList[position]
        holder.noteView.apply {
            descriptionTv.text = note.description
            dateTv.text = note.date
            timeTv.text = note.duration
        }

        val playButton: ImageView = holder.noteView.playButton
        playButton.setOnClickListener {
            onPlayClickListener?.invoke(note)
        }

        holder.noteView.root.setOnClickListener {
            onClickListener?.invoke(note)
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    class NoteListHolder(val noteView: NoteItemBinding):
        RecyclerView.ViewHolder(noteView.root)
}