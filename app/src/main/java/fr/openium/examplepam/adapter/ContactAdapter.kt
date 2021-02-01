package fr.openium.examplepam.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.openium.examplepam.R
import fr.openium.examplepam.adapter.ContactAdapter.ContactViewHolder
import fr.openium.examplepam.model.CallContact

class ContactAdapter(
    private val contacts: List<CallContact>,
    private val listener: OnContactClickedListener
) : RecyclerView.Adapter<ContactViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.textViewItemContact.text = contact.name
        holder.itemView.setOnClickListener {
            listener.onContactClicked(contact)
        }
        holder.imageButtonCall.setOnClickListener {
            listener.onCallClicked(contact)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewItemContact: TextView
        var imageButtonCall: ImageButton

        init {
            textViewItemContact = view.findViewById(R.id.textViewItemContact)
            imageButtonCall = view.findViewById(R.id.call)
        }
    }

    interface OnContactClickedListener {
        fun onContactClicked(contact: CallContact)
        fun onCallClicked(contact: CallContact)
    }
}