package msi.crool.musicapp

import androidx.annotation.DrawableRes

data class Lib(
    @DrawableRes val icon:Int,val name:String
)

val libraries= listOf(
    Lib(R.drawable.r_music,"Playlist"),
    Lib(R.drawable.ic_microphone,"Artist"),
    Lib(R.drawable.ic_baseline_album_24,"Album"),
    Lib(R.drawable.ic_baseline_music_note_24,"Songs"),
    Lib(R.drawable.ic_genre,"Genre")
)