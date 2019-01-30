package fr.openium.examplepam.rest;

import fr.openium.examplepam.model.News;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsApi {

    /**
     * Call a webservice with dynamic path
     * Exemple :
     * "https://api.myjson.com/bins/31245"
     * base url : https://api.myjson.com/
     * path : bins/{id}
     * where id = 31245
     * So the method will be called using getNews("31235");
     */

    @GET("bins/{id}")
    Call<News> getNews(@Path("id") String id);

    /**
     * Call a webservice with a static path
     */
    @GET("https://custom.server.com/file.json")
    Call<String> getFile();
}
