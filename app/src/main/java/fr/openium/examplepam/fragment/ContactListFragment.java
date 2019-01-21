package fr.openium.examplepam.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.openium.examplepam.R;
import fr.openium.examplepam.adapter.ContactAdapter;
import fr.openium.examplepam.model.CallContact;
import fr.openium.examplepam.service.CallService;

public class ContactListFragment extends Fragment implements ContactAdapter.OnContactClickedListener {
    private Button buttonRequirePermission;
    private RecyclerView recyclerViewContact;
    private LinearLayout linearLayout;
    private TextView emptyTextView;
    private static final int REQUEST_CONTACT_PERMISSION = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_contact, container, false);
        buttonRequirePermission = view.findViewById(R.id.buttonRequirePermission);
        recyclerViewContact = view.findViewById(R.id.recyclerViewContact);
        linearLayout = view.findViewById(R.id.requirePermissionBlock);
        emptyTextView = view.findViewById(R.id.textViewEmpty);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        buttonRequirePermission.setOnClickListener(button -> {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT_PERMISSION);
        });

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            displayContacts();
        }
    }

    private void displayContacts() {
        linearLayout.setVisibility(View.GONE);
        recyclerViewContact.setLayoutManager(new LinearLayoutManager(getContext()));
        List<CallContact> contacts = getContacts();

        if (!contacts.isEmpty()) {
            recyclerViewContact.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
            recyclerViewContact.setAdapter(new ContactAdapter(contacts, this));
        } else {
            recyclerViewContact.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        }
    }

    private List<CallContact> getContacts() {
        ContentResolver cr = getContext().getContentResolver();
        //We recover the contact list  from the content uri
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        List<CallContact> contacts = new ArrayList<>();

        if ((cur != null ? cur.getCount() : 0) > 0) {
            // We get only the id and the name
            while (cur.moveToNext()) {
                long id = cur.getLong(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contacts.add(new CallContact(id, name));
            }
        }
        if (cur != null) {
            cur.close();
        }

        return contacts;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If the permission has been granted by the user
        if (requestCode == REQUEST_CONTACT_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayContacts();
            } else {
                Toast.makeText(getContext(), "We need the contact permission to display the list", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onContactClicked(CallContact contact) {
// We want an app that display content
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // That type of content (a contact)
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contact.id));
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onCallClicked(CallContact contact) {
        Intent intent = CallService.getStartIntent(getContext(), contact.name);
        ContextCompat.startForegroundService(getContext(), intent);
    }
}
