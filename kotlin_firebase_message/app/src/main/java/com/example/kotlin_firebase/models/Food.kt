package com.example.kotlin_firebase.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Food(val foodName: String, val foodImageUrl: String, val price: String, var amount: Int): Parcelable{
    constructor() : this( "", "","", 0)
}