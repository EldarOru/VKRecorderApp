package com.example.vkrecorderapp.presentation.fragments

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.vkrecorderapp.R
import com.example.vkrecorderapp.databinding.FragmentRecordBinding
import com.example.vkrecorderapp.domain.entities.AudioNote
import com.example.vkrecorderapp.presentation.viewmodels.RecordViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class RecordFragment : Fragment() {

    private var recordFragmentBinding: FragmentRecordBinding? = null
    private val REQUEST_AUDIO_PERMISSION_CODE = 1
    private var recorder: MediaRecorder? = null
    private var isRecording = false
    private var fileName = ""
    private val recordViewModel: RecordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recordFragmentBinding = FragmentRecordBinding.inflate(inflater, container, false)
        return recordFragmentBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_AUDIO_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(context, "Permission Granted", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun checkPermission(): Boolean{
        val resultRecord = context?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.RECORD_AUDIO) }
        val resultStorage = context?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) }
        return resultRecord == PackageManager.PERMISSION_GRANTED
                && resultStorage == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions(){
        val permissions = arrayOf(
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        activity?.let { ActivityCompat.requestPermissions(it, permissions, REQUEST_AUDIO_PERMISSION_CODE) }
    }

    private fun startRecording(){
        val uuid: String = UUID.randomUUID().toString()
        fileName = Environment.getExternalStorageDirectory().absolutePath.toString() + "/" + uuid + ".3gp"
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
                start()
            } catch (e: IOException) {
                Log.e("TAG", e.message.toString())
            }
        }
        recordFragmentBinding?.chronometer?.base = SystemClock.elapsedRealtime()
        recordFragmentBinding?.chronometer?.start()
        isRecording = true
    }

    @SuppressLint("SimpleDateFormat")
    private fun stopRecording(){
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val audioNote = AudioNote(date = currentDate, description = "New Audio", storagePath = fileName)
        recordViewModel.insertNote(audioNote)
        isRecording = false
        recordFragmentBinding?.chronometer?.stop()
    }

    private fun setListeners() {
         val recordButton = recordFragmentBinding?.recordButton as Button

         recordButton.setOnClickListener {
             if (checkPermission()){
                 if (isRecording){
                     recordButton.text = getString(R.string.clicktorecord)
                     stopRecording()
                 }else{
                     recordButton.text = getString(R.string.recording)
                     startRecording()
                 }
             }else{
                 requestPermissions()
             }
         }
    }
}