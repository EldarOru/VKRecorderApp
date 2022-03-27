package com.example.vkrecorderapp.presentation.fragments

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkrecorderapp.databinding.FragmentMainListBinding
import com.example.vkrecorderapp.presentation.adapters.NoteListAdapter
import com.example.vkrecorderapp.presentation.viewmodels.MainListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.RuntimeException

@AndroidEntryPoint
class MainListFragment : Fragment() {

    private lateinit var onFragmentsInteractionsListener: OnFragmentInteractionListener
    private lateinit var notesListAdapter: NoteListAdapter
    private var mainListFragmentBinding: FragmentMainListBinding? = null
    private var player: MediaPlayer? = null
    private var isPlaying = false
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

    private fun setListeners(){
        notesListAdapter.onLongClickListener = {
            onFragmentsInteractionsListener.onAddBackStack(it.storagePath, DetailedNoteFragment.newInstance(
                it.id!!
            ))
        }

        notesListAdapter.onPlayClickListener = {
            if(isPlaying){
                player?.apply {
                    stop()
                    reset()
                    release()
                }
                isPlaying = false
            }else{
                player = MediaPlayer()

                try {
                    player?.apply {
                        setDataSource(it.storagePath)
                        prepare()
                        start()
                    }
                isPlaying = true
                }catch (e: IOException){
                    Log.e("TAG", "player failed")
                }
            }
        }
    }

    private fun setRecyclerView(){
        val recyclerView = mainListFragmentBinding?.notesRv
        recyclerView?.layoutManager = LinearLayoutManager(context)
        notesListAdapter = NoteListAdapter()
        recyclerView?.adapter = notesListAdapter
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
}

