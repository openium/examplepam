package fr.openium.examplepam.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.openium.examplepam.R;
import fr.openium.examplepam.model.CallContact;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<CallContact> contacts;
    private OnContactClickedListener listener;

    public ContactAdapter(List<CallContact> contacts, OnContactClickedListener onContactClickedListener) {
        this.contacts = contacts;
        this.listener = onContactClickedListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        CallContact contact = contacts.get(position);
        holder.textViewItemContact.setText(contact.name);
        holder.itemView.setOnClickListener(v -> listener.onContactClicked(contact));
        holder.imageButtonCall.setOnClickListener(v -> listener.onCallClicked(contact));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemContact;
        ImageButton imageButtonCall;

        ContactViewHolder(View view) {
            super(view);
            textViewItemContact = view.findViewById(R.id.textViewItemContact);
            imageButtonCall = view.findViewById(R.id.call);
        }
    }

    public interface OnContactClickedListener {
        void onContactClicked(CallContact contact);

        void onCallClicked(CallContact contact);
    }
}
