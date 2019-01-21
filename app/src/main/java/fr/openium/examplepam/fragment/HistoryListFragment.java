package fr.openium.examplepam.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.openium.examplepam.R;
import fr.openium.examplepam.adapter.HistoryAdapter;
import fr.openium.examplepam.database.AppDatabase;
import fr.openium.examplepam.model.Call;

public class HistoryListFragment extends Fragment {
    private RecyclerView recyclerViewCall;
    private TextView emptyTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_history, container, false);
        recyclerViewCall = view.findViewById(R.id.recyclerViewContact);
        emptyTextView = view.findViewById(R.id.textViewEmpty);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerViewCall.setLayoutManager(new LinearLayoutManager(getContext()));
        displayCallSync();
    }

    private void displayCall() {
        List<Call> calls = AppDatabase.getInstance(getContext()).callDao().getAll();
        if (!calls.isEmpty()) {
            recyclerViewCall.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewCall.setAdapter(new HistoryAdapter(calls));
        } else {
            recyclerViewCall.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        }
    }

    private void displayCallSync() {
        AppDatabase.getInstance(getContext()).callDao().getAllSync().observe(this, new Observer<List<Call>>() {
            @Override
            public void onChanged(List<Call> calls) {
                if (!calls.isEmpty()) {
                    recyclerViewCall.setVisibility(View.VISIBLE);
                    emptyTextView.setVisibility(View.GONE);
                    if (recyclerViewCall.getAdapter() != null) {
                        ((HistoryAdapter) recyclerViewCall.getAdapter()).updateCalls(calls);
                    } else {
                        recyclerViewCall.setAdapter(new HistoryAdapter(calls));
                    }
                } else {
                    recyclerViewCall.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

//    private void displayContacts() {
//        linearLayout.setVisibility(View.GONE);
//        recyclerViewContact.setLayoutManager(new LinearLayoutManager(getContext()));
//        List<CallContact> contacts = getContactName();
//        recyclerViewContact.setAdapter(new ContactAdapter(contacts, (contact -> {
//
//        })));
//    }
//
//    private List<String> getContactName() {
//        ContentResolver cr = getContext().getContentResolver();
//        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//        List<String> contacts = new ArrayList<>();
//
//        if ((cur != null ? cur.getCount() : 0) > 0) {
//            while (cur.moveToNext()) {
//                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                contacts.add(name);
//            }
//        }
//        if (cur != null) {
//            cur.close();
//        }
//
//        return contacts;
//    }

}
