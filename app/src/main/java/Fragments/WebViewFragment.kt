package Fragments

import Webview.Webview
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.expensemanagementsystem.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment() {
    private lateinit var binding: FragmentWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebViewBinding.inflate(layoutInflater)

        binding.webview.settings.javaScriptEnabled = true;
        binding.webview.settings.loadWithOverviewMode = true;
        binding.webview.settings.useWideViewPort = true;

        val wv = Webview(requireActivity())
        binding.webview.webViewClient = wv
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.webview.loadUrl("https://www.ais.ac.nz/")
    }
}