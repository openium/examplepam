package fr.openium.examplepam.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.openium.examplepam.R
import fr.openium.examplepam.adapter.HistoryAdapter
import fr.openium.examplepam.model.Call

class HistoryListFragment : Fragment() {
    private var recyclerViewCall: RecyclerView? = null
    private var emptyTextView: TextView? = null
    private val viewModel: HistoryViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_call_history, container, false)
        recyclerViewCall = view.findViewById(R.id.recyclerViewContact)
        emptyTextView = view.findViewById(R.id.textViewEmpty)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerViewCall?.layoutManager = LinearLayoutManager(context)
        viewModel.calls.observe(viewLifecycleOwner) {
            displayCalls(it)
        }
    }

    private fun displayCalls(calls: List<Call>) {
        if (!calls.isEmpty()) {
            emptyTextView?.visibility = View.GONE

            recyclerViewCall?.apply {
                //Here this is the recyclerViewCall
                visibility = View.VISIBLE
                if (adapter != null) {
                    (adapter as HistoryAdapter?)?.updateCalls(calls)
                } else {
                    adapter = HistoryAdapter(calls)
                }
            }

        } else {
            emptyTextView?.visibility = View.VISIBLE
            recyclerViewCall?.visibility = View.GONE
        }
    }
}