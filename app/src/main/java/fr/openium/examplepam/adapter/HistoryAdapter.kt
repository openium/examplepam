package fr.openium.examplepam.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.openium.examplepam.R
import fr.openium.examplepam.adapter.HistoryAdapter.CallViewHolder
import fr.openium.examplepam.model.Call
import org.ocpsoft.prettytime.PrettyTime
import java.util.*
import java.util.concurrent.TimeUnit

class HistoryAdapter(private var calls: List<Call>) : RecyclerView.Adapter<CallViewHolder>() {
    private val prettyTime: PrettyTime
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_call, parent, false)
        return CallViewHolder(view)
    }

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        val (_, contactName, length, startDate) = calls[position]
        holder.textViewItemContact.text = contactName
        holder.textViewLength.text = getElapseTime(length)
        holder.textViewItemCallStart.text = prettyTime.format(startDate)
    }

    private fun getElapseTime(duration: Long): String {
        val hr = TimeUnit.SECONDS.toHours(duration)
        val result: String
        result = if (hr != 0L) {
            val min = TimeUnit.SECONDS.toMinutes(duration - TimeUnit.HOURS.toSeconds(hr))
            String.format("%01dh %02dm", hr, min)
        } else {
            val min = TimeUnit.SECONDS.toMinutes(duration)
            if (min != 0L) {
                String.format("%2d min", min)
            } else {
                String.format("%2d sec", duration)
            }
        }
        return result
    }

    override fun getItemCount(): Int {
        return calls.size
    }

    fun updateCalls(calls: List<Call>) {
        this.calls = calls
        notifyDataSetChanged()
    }

    class CallViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewItemContact: TextView
        var textViewLength: TextView
        var textViewItemCallStart: TextView

        init {
            textViewItemContact = view.findViewById(R.id.textViewItemCall)
            textViewLength = view.findViewById(R.id.textViewItemCallLength)
            textViewItemCallStart = view.findViewById(R.id.textViewItemCallStart)
        }
    }

    init {
        prettyTime = PrettyTime(Locale.ENGLISH)
    }
}