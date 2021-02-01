package fr.openium.examplepam.ui.contactlist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.openium.examplepam.R
import fr.openium.examplepam.adapter.ContactAdapter
import fr.openium.examplepam.adapter.ContactAdapter.OnContactClickedListener
import fr.openium.examplepam.model.CallContact
import fr.openium.examplepam.service.CallService.Companion.getStartIntent
import java.util.*

class ContactListFragment : Fragment(), OnContactClickedListener {
    private var buttonRequirePermission: Button? = null
    private var recyclerViewContact: RecyclerView? = null
    private var linearLayout: LinearLayout? = null
    private var emptyTextView: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_contact, container, false)
        buttonRequirePermission = view.findViewById(R.id.buttonRequirePermission)
        recyclerViewContact = view.findViewById(R.id.recyclerViewContact)
        linearLayout = view.findViewById(R.id.requirePermissionBlock)
        emptyTextView = view.findViewById(R.id.textViewEmpty)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        buttonRequirePermission!!.setOnClickListener { button: View? ->
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_CONTACTS
                ), REQUEST_CONTACT_PERMISSION
            )
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            displayContacts()
        }
    }

    private fun displayContacts() {
        linearLayout!!.visibility = View.GONE
        recyclerViewContact!!.layoutManager = LinearLayoutManager(context)
        val contacts = contacts
        if (!contacts.isEmpty()) {
            recyclerViewContact!!.visibility = View.VISIBLE
            emptyTextView!!.visibility = View.GONE
            recyclerViewContact!!.adapter = ContactAdapter(contacts, this)
        } else {
            recyclerViewContact!!.visibility = View.GONE
            emptyTextView!!.visibility = View.VISIBLE
        }
    }// We get only the id and the name

    //We recover the contact list  from the content uri
    private val contacts: List<CallContact>
        get() {
            val cr = requireContext().contentResolver
            //We recover the contact list  from the content uri
            val cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
            val contacts: MutableList<CallContact> = ArrayList()
            if (cur?.count ?: 0 > 0) {
                // We get only the id and the name
                while (cur!!.moveToNext()) {
                    val id = cur.getLong(cur.getColumnIndex(ContactsContract.Contacts._ID))
                    val name =
                        cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    contacts.add(CallContact(id, name))
                }
            }
            cur?.close()
            return contacts
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        // If the permission has been granted by the user
        if (requestCode == REQUEST_CONTACT_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayContacts()
            } else {
                Toast.makeText(context, R.string.contact_permission_required, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onContactClicked(contact: CallContact) {
// We want an app that display content
        val intent = Intent(Intent.ACTION_VIEW)
        // That type of content (a contact)
        val uri =
            Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contact.id.toString())
        intent.data = uri
        startActivity(intent)
    }

    override fun onCallClicked(contact: CallContact) {
        val intent = getStartIntent(context, contact.name)
        ContextCompat.startForegroundService(requireContext(), intent)
    }

    companion object {
        private const val REQUEST_CONTACT_PERMISSION = 1
    }
}