package com.example.flixyConnect.data.model

import kotlinx.serialization.Serializable

@Serializable
class ProductionCompany(
    val id: Int,
    val logo_path: String? = null,
    val name: String,
    val origin_contry: String? = null
) {
}