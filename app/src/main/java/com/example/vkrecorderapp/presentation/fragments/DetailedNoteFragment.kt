package com.example.vkrecorderapp.presentation.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.vkrecorderapp.databinding.FragmentDetailedNoteBinding
import com.example.vkrecorderapp.domain.entities.AudioNote
import com.example.vkrecorderapp.presentation.viewmodels.DetailedNoteViewModel
import com.example.vkrecorderapp.utils.TimeConverter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException


@AndroidEntryPoint
class DetailedNoteFragment : Fragment() {

    private var noteId: Int = UNDEFINED_ID
    private var detailedNoteBinding: FragmentDetailedNoteBinding? = null
    private val detailedNoteViewModel: DetailedNoteViewModel by viewModels()
    private var player: MediaPlayer? = null
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            noteId = it.getInt(ARG_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailedNoteBinding = FragmentDetailedNoteBinding.inflate(inflater, container, false)
        return detailedNoteBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailedNoteViewModel.getNote(noteId)
        setData()
        controlAudio()
    }

    override fun onStop() {
        super.onStop()
        if (player != null && isPlaying) {
            player?.apply {
                stop()
                reset()
                release()
            }
            player = null
            isPlaying = false
        }
        val audioNote = AudioNote(noteId,
            detailedNoteBinding?.descriptionEt?.text.toString(),
            detailedNoteViewModel.noteState.value.date,
            detailedNoteViewModel.noteState.value.storagePath,
            detailedNoteViewModel.noteState.value.duration)
        detailedNoteViewModel.updateNote(audioNote)

    }

    private fun setData(){
        lifecycleScope.launch {
            detailedNoteViewModel.noteState.collect {
                it.let {
                    detailedNoteBinding?.descriptionEt?.setText(it.description)
                    detailedNoteBinding?.dateTv?.text = it.date
                    detailedNoteBinding?.duration?.text = it.duration
                }
            }
        }
    }

    private fun controlAudio(){
        detailedNoteBinding?.playImage?.setOnClickListener {
            if (!isPlaying && player == null){
                player = MediaPlayer()
                player?.apply{
                    setDataSource(detailedNoteViewModel.noteState.value.storagePath)
                    prepare()
                    start()
                }
                initSeekBar()
                isPlaying = true
            }else if (!isPlaying && player != null){
                player?.start()
                isPlaying = true
            }
        }
        detailedNoteBinding?.pauseImage?.setOnClickListener {
            if (isPlaying){
                player?.pause()
                isPlaying = false
            }
        }
        detailedNoteBinding?.stopImage?.setOnClickListener {
            if (isPlaying){
                player?.apply {
                    stop()
                    reset()
                    release()
                }
                player = null
                isPlaying = false
            }
        }

        detailedNoteBinding?.seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    player?.seekTo(progress)
                    detailedNoteBinding?.currentDuration?.text =
                    player?.currentPosition?.toLong()?.let { TimeConverter.convertTime(it) }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun initSeekBar(){
        detailedNoteBinding?.seekBar?.max = player?.duration!!

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object: Runnable{
            override fun run() {
                try {
                    detailedNoteBinding?.seekBar?.progress = player?.currentPosition ?: 0
                    detailedNoteBinding?.currentDuration?.text =
                        player?.currentPosition?.toLong()?.let { TimeConverter.convertTime(it) }

                    if ( detailedNoteBinding?.seekBar?.progress == player?.duration){
                        player?.apply {
                            stop()
                            reset()
                            release()
                        }
                        player = null
                        isPlaying = false
                    }
                    handler.postDelayed(this, 1000)
                }catch (e: IOException){
                    detailedNoteBinding?.seekBar?.progress = 0
                }
            }
        }, 0)

    }


    companion object {
        @JvmStatic
        fun newInstance(id: Int) =
            DetailedNoteFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ID, id)
                }
            }
        private const val ARG_ID = "ID"
        private const val UNDEFINED_ID = -1
    }
}