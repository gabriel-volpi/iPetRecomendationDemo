package com.example.ipetrecomendationdemo.recomendation.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PetBasicInfo(
    val name: String,
    val species: String,
    val breed: String,
    val size: String, // porte
) : Parcelable

