package fr.openium.examplepam.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.openium.examplepam.R;
import fr.openium.examplepam.model.Call;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.CallViewHolder> {
    private List<Call> calls;
    private PrettyTime prettyTime;

    public HistoryAdapter(List<Call> calls) {
        this.calls = calls;
        prettyTime = new PrettyTime(Locale.ENGLISH);
    }

    @NonNull
    @Override
    public CallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call, parent, false);
        return new CallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallViewHolder holder, int position) {
        Call call = calls.get(position);
        holder.textViewItemContact.setText(call.contactName);
        holder.textViewLength.setText(getElapseTime(call.length));
        holder.textViewItemCallStart.setText(prettyTime.format(call.startDate));
    }

    private String getElapseTime(long duration) {
        long hr = TimeUnit.SECONDS.toHours(duration);
        String result;
        if (hr != 0L) {
            long min = TimeUnit.SECONDS.toMinutes(duration - TimeUnit.HOURS.toSeconds(hr));
            result = String.format("%01dh %02dm", hr, min);
        } else {
            long min = TimeUnit.SECONDS.toMinutes(duration);
            if (min != 0L) {
                result = String.format("%2d min", min);
            } else {
                result = String.format("%2d sec", duration);
            }
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return calls.size();
    }

    public void updateCalls(List<Call> calls) {
        this.calls = calls;
        notifyDataSetChanged();
    }

    static class CallViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemContact;
        TextView textViewLength;
        TextView textViewItemCallStart;

        CallViewHolder(View view) {
            super(view);
            textViewItemContact = view.findViewById(R.id.textViewItemCall);
            textViewLength = view.findViewById(R.id.textViewItemCallLength);
            textViewItemCallStart = view.findViewById(R.id.textViewItemCallStart);
        }
    }
}
