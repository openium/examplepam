package fr.openium.examplepam.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fr.openium.examplepam.R;
import fr.openium.examplepam.model.Author;
import fr.openium.examplepam.model.News;
import fr.openium.examplepam.rest.ApiHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {
    private ProgressBar progressBar;
    private TextView textViewReleaseTitle;
    private TextView textViewContent;
    private TextView textViewAuthors;
    private TextView textViewChangelog;
    private ScrollView scrollView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        textViewReleaseTitle = view.findViewById(R.id.textViewReleaseTitle);
        textViewAuthors = view.findViewById(R.id.textViewAuthors);
        textViewContent = view.findViewById(R.id.textViewContent);
        textViewChangelog = view.findViewById(R.id.textViewChangelog);
        scrollView = view.findViewById(R.id.scrollView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ApiHelper.getInstance().getNewsApi().getNews("11pw2g").enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                scrollView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    News news = response.body();
                    textViewReleaseTitle.setText(news.version + " - " + news.releaseDate);
                    textViewContent.setText(news.content);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String s : news.changelog) {
                        stringBuilder.append("- ").append(s).append("\n");
                    }
                    textViewChangelog.setText(stringBuilder);

                    StringBuilder stringBuilderAuthors = new StringBuilder();
                    for (int i = 0; i < news.authors.size(); i++) {
                        Author author = news.authors.get(i);
                        stringBuilderAuthors.append(author.name);
                        if (i + 1 < news.authors.size()) {
                            stringBuilderAuthors.append(" - ");
                        }
                    }
                    textViewAuthors.setText(stringBuilderAuthors);
                } else {
                    //TODO handle failure
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                //TODO handle failure :)
            }
        });
    }
}
