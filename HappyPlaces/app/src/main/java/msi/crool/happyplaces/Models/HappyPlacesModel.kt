package msi.crool.happyplaces.Models

import java.io.Serializable

data class HappyPlacesModel(
    val id:Int,
    val title:String,
    val Image:String,
    val Description:String,
    val Date:String,
    val Location: String,
    val Lattitude: Double?,
    val Longitude: Double?
):Serializable
