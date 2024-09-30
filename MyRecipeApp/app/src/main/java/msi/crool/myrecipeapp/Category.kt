package msi.crool.myrecipeapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val idCategory: String,
    val strCategory:String,
    val strCategoryThumb: String,
    val strCategoryDescription:String
):Parcelable

data class CategoriesResponse(val categories: List<Category>)
