package fr.openium.examplepam.model

import kotlinx.serialization.Serializable

@Serializable
data class News(
    val version: String? = null,
    val releaseDate: String? = null,
    val content: String? = null,
    val changelog: List<String>? = null,
    val authors: List<Author>? = null
)