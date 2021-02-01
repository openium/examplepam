package fr.openium.examplepam.rest

import fr.openium.examplepam.model.News
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*

object ApiHelper {
    private val client = HttpClient(Android) {
        engine {

        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    suspend fun getNews(): News = client.get<News>("https://jsonkeeper.com/b/BVVR")
}