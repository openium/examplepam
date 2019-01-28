package fr.openium.examplepam.rest;

import fr.openium.examplepam.model.News;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsApi {
    @GET("bins/{id}")
    Call<News> getNews(@Path("id") String id);
}
