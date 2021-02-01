package fr.openium.examplepam.ui.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import fr.openium.examplepam.R
import fr.openium.examplepam.model.News
import fr.openium.examplepam.rest.ApiHelper
import kotlinx.coroutines.launch

class NewsFragment : Fragment() {
    private var progressBar: ProgressBar? = null
    private var textViewReleaseTitle: TextView? = null
    private var textViewContent: TextView? = null
    private var textViewAuthors: TextView? = null
    private var textViewChangelog: TextView? = null
    private var scrollView: ScrollView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_news, container, false)
        progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        textViewReleaseTitle = view.findViewById<TextView>(R.id.textViewReleaseTitle)
        textViewAuthors = view.findViewById<TextView>(R.id.textViewAuthors)
        textViewContent = view.findViewById<TextView>(R.id.textViewContent)
        textViewChangelog = view.findViewById<TextView>(R.id.textViewChangelog)
        scrollView = view.findViewById<ScrollView>(R.id.scrollView)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val news = ApiHelper.getNews()
                displayNews(news)
            } catch (e: Exception) {
                Log.e("NewsFragment", "network error", e)
                //TODO handle failure
            }

        }
    }

    private fun displayNews(news: News) {
        scrollView?.setVisibility(View.VISIBLE)
        progressBar?.setVisibility(View.GONE)
        textViewReleaseTitle?.setText(news.version + " - " + news.releaseDate)
        textViewContent?.setText(news.content)
        val stringBuilder = buildString {
            news.changelog?.forEach {
                append("- ").append(it).append("\n")
            }
        }
        textViewChangelog?.setText(stringBuilder)
        val stringBuilderAuthors = buildString {
            news.authors?.forEachIndexed { index, author ->
                append(author.name)
                if (index < news.authors.lastIndex) {
                    append(" - ")
                }
            }
        }
        textViewAuthors?.setText(stringBuilderAuthors)
    }
}