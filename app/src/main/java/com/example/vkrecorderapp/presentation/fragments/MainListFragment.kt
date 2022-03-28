package com.example.vkrecorderapp.presentation.fragments

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vkrecorderapp.R
import com.example.vkrecorderapp.databinding.FragmentMainListBinding
import com.example.vkrecorderapp.domain.entities.AudioNote
import com.example.vkrecorderapp.presentation.adapters.NoteListAdapter
import com.example.vkrecorderapp.presentation.viewmodels.MainListViewModel
import com.example.vkrecorderapp.utils.TimeConverter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.lang.RuntimeException

@AndroidEntryPoint
class MainListFragment : Fragment() {

    private lateinit var onFragmentsInteractionsListener: OnFragmentInteractionListener
    private lateinit var notesListAdapter: NoteListAdapter
    private var mainListFragmentBinding: FragmentMainListBinding? = null
    private var player: MediaPlayer? = null
    private var isPlaying = false
    private var playingNoteId = UNDEFINED_PLAYING_ID
    private val mainListViewModel: MainListViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener){
            onFragmentsInteractionsListener = context
        }else{
            throw RuntimeException("Activity must implement OnFragmentInteractionsListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainListFragmentBinding = FragmentMainListBinding.inflate(inflater, container, false)
        return mainListFragmentBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        setListeners()
        setData()
    }

    override fun onStop() {
        super.onStop()
        if (player != null && isPlaying) {
            finishPlaying()
        }
    }

    private fun setListeners(){
        notesListAdapter.onClickListener = {
            onFragmentsInteractionsListener.onAddBackStack(it.storagePath, DetailedNoteFragment.newInstance(
                it.id!!
            ))
        }

        notesListAdapter.onPlayClickListener = {
            if (player != null && !isPlaying) {
                startPlaying(it)
            }else{
                finishPlaying()
                startPlaying(it)
            }
        }

        mainListFragmentBinding?.playingButton?.setOnClickListener {
            if (player != null && isPlaying) {
                player?.pause()
                isPlaying = false
                mainListFragmentBinding?.playingButton?.setImageResource(R.drawable.ic_baseline_pause_24)
            }else{
                player?.start()
                isPlaying = true
                mainListFragmentBinding?.playingButton?.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
        }
    }

    private fun setRecyclerView(){
        val recyclerView = mainListFragmentBinding?.notesRv
        recyclerView?.layoutManager = LinearLayoutManager(context)
        notesListAdapter = NoteListAdapter()
        recyclerView?.adapter = notesListAdapter
        setupSwipeListener(recyclerView as RecyclerView)
    }

    private fun setData(){
        lifecycleScope.launch {
            mainListViewModel.notesState.collect {
                it.let {
                    notesListAdapter.notesList = it
                }
            }
        }
    }

    private fun startPlaying(audioNote: AudioNote){
        player = MediaPlayer()
        try {
            player?.apply {
                setDataSource(audioNote.storagePath)
                prepare()
                start()
            }
            isPlaying = true
            playingNoteId = audioNote.id ?: -1
        } catch (e: IOException) {
            Log.e("TAG", "player failed")
        }
        initDuration(audioNote)
        mainListFragmentBinding?.playingTv?.text = "Playing now: ${audioNote.description}"
        mainListFragmentBinding?.playingButton?.setImageResource(R.drawable.ic_baseline_play_arrow_24)
    }

    private fun finishPlaying(){
        player?.apply {
            stop()
            reset()
            release()
        }
        player = null
        isPlaying = false
    }

    private fun setupSwipeListener(recyclerView: RecyclerView) {
        val myCallBack = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT){
                    val item = notesListAdapter.notesList[viewHolder.adapterPosition]
                    if (playingNoteId == item.id){
                        finishPlaying()
                        mainListFragmentBinding?.playingTv?.text = getString(R.string.player)
                        mainListFragmentBinding?.playingButton?.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    }
                    val file = File(item.storagePath)
                    if (file.exists()){
                        file.delete()
                    }
                    mainListViewModel.deleteNote(item)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(myCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun initDuration(audioNote: AudioNote){
        mainListFragmentBinding?.duration?.text = audioNote.duration

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object: Runnable{
            override fun run() {
                try {
                    mainListFragmentBinding?.currentDuration?.text =
                        player?.currentPosition?.toLong()?.let { TimeConverter.convertTime(it) }

                    handler.postDelayed(this, 1000)
                }catch (e: IOException){
                    mainListFragmentBinding?.currentDuration?.text = "0"
                }
            }
        }, 0)
    }

    override fun onStart() {
        super.onStart()
        mainListFragmentBinding?.apply {
            duration.text = getString(R.string.nulls)
            separate.text = getString(R.string.separate)
        }
    }

    companion object{
        private const val UNDEFINED_PLAYING_ID = -1
    }
}

