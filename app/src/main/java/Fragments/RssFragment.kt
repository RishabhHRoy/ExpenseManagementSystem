package Fragments

import Adapters.RssAdapter
import Adapters.RssItem
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanagementsystem.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL

class RssFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RssAdapter
    private lateinit var webView: WebView
    private lateinit var backButton: Button
    private val rssUrl = "https://www.rnz.co.nz/rss/media-technology.xml"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_rss, container, false)

        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = RssAdapter(emptyList()) { url -> showWebPage(url) }
        recyclerView.adapter = adapter

        webView = rootView.findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()

        fetchData()

        return rootView
    }

    private fun fetchData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL(rssUrl)
                val parser = XmlPullParserFactory.newInstance().newPullParser()
                parser.setInput(url.openStream(), null)

                val items = mutableListOf<RssItem>()
                var eventType = parser.eventType
                var currentItem: RssItem? = null

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            if (parser.name == "item") {
                                currentItem = RssItem("", "", "", "")
                            } else if (currentItem != null) {
                                when (parser.name) {
                                    "title" -> currentItem.title = parser.nextText()
                                    "description" -> currentItem.description = parser.nextText()
                                    "pubDate" -> currentItem.pubDate = parser.nextText()
                                    "link" -> currentItem.link = parser.nextText()
                                    "enclosure" -> {
                                    }
                                }
                            }
                        }
                        XmlPullParser.END_TAG -> {
                            if (parser.name == "item" && currentItem != null) {
                                items.add(currentItem)
                            }
                        }
                    }
                    eventType = parser.next()
                }

                launch(Dispatchers.Main) {
                    adapter.updateData(items)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showWebPage(url: String) {
        recyclerView.visibility = View.GONE
        webView.visibility = View.VISIBLE
        webView.loadUrl(url)
    }


}
