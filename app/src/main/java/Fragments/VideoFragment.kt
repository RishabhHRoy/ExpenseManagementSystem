package Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.databinding.FragmentVideoBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class VideoFragment : Fragment() {
    private val REQUEST_VIDEO_PICK = 1001
    private lateinit var binding : FragmentVideoBinding
    private lateinit var videoView : VideoView
    private lateinit var selectVideoBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.isVisible = false

        // Inflate the layout for this fragment
        binding = FragmentVideoBinding.inflate(inflater, container, false)

        selectVideoBtn = binding.selectVideoButton
        videoView = binding.videoView

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectVideoBtn.setOnClickListener {
            PickVideo()
        }

        binding.back.setOnClickListener{
            requireActivity().onBackPressed()
        }
    }

    private fun PickVideo() {
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_VIDEO_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VIDEO_PICK && data != null) {
            val selectedVideoUri = data.data
            selectedVideoUri?.let { playVideo(it) }
        }
    }

    private fun playVideo(videoUri: Uri) {
        videoView.setVideoURI(videoUri)

        // Add media controller to enable play/pause/seek controls
        val mediaController = MediaController(requireContext())
        videoView.setMediaController(mediaController)
        mediaController.setAnchorView(videoView)

        // Start playing the video
        videoView.start()
    }
}