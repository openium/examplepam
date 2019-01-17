package fr.openium.examplepam.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

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

public class ContactListFragment extends Fragment {
    private Button buttonRequirePermission;
    private RecyclerView recyclerViewContact;
    private LinearLayout linearLayout;
    private static final int REQUEST_CONTACT_PERMISSION = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_contact, container, false);
        buttonRequirePermission = view.findViewById(R.id.buttonRequirePermission);
        recyclerViewContact = view.findViewById(R.id.recyclerViewContact);
        linearLayout = view.findViewById(R.id.requirePermissionBlock);
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
        List<String> contacts = getContactName();
        recyclerViewContact.setAdapter(new ContactAdapter(contacts));
    }

    private List<String> getContactName() {
        ContentResolver cr = getContext().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        List<String> contacts = new ArrayList<>();

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contacts.add(name);
            }
        }
        if (cur != null) {
            cur.close();
        }

        return contacts;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CONTACT_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            displayContacts();
        }
    }
}
