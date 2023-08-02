package Fragments

import Webview.Webview
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.databinding.FragmentSocialNetworkBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class SocialNetworkFragment : Fragment() {
   private lateinit var binding : FragmentSocialNetworkBinding

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
        binding = FragmentSocialNetworkBinding.inflate(inflater, container, false)

        binding.webview.settings.javaScriptEnabled = true;
        binding.webview.settings.loadWithOverviewMode = true;
        binding.webview.settings.useWideViewPort = true;

        val wv = Webview(requireActivity())
        binding.webview.webViewClient = wv
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener{
            requireActivity().onBackPressed()
        }

        binding.twitterBtn.setOnClickListener{
            openTwitter()
        }

        binding.facebookBtn.setOnClickListener{
            openFacebook()
        }

        binding.instagramBtn.setOnClickListener{
            openInstagram()
        }

        binding.linkedlnBtn.setOnClickListener{
            openLinkedln()
        }
    }

    private fun openTwitter() {
        binding.webview.loadUrl("https://twitter.com/intent/tweet?text=Hello%20Twitter!")
        //val twitterUrl = "https://twitter.com/intent/tweet?text=Hello%20Twitter!"
        //val intent = Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl))

        // If you want to open the Twitter app (if installed) instead of the website, you can use the following:
        // intent.setPackage("com.twitter.android");
       /* try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Handle the case where the Twitter app is not installed on the device.
            // You can provide a fallback or show a message to the user to install the app.
        }*/
    }

    private fun openFacebook() {
        binding.webview.loadUrl("https://www.facebook.com/")
    }
    private fun openInstagram() {
        binding.webview.loadUrl("https://www.instagram.com/")
    }
    private fun openLinkedln() {
        binding.webview.loadUrl("https://nz.linkedin.com/")
    }
}