package fr.openium.examplepam.model

import kotlinx.serialization.Serializable

@Serializable
data class Author(
    val name: String? = null,
    val email: String? = null
)