package msi.crool.simpleapidemo

data class ResponseData(
    val id: String,
    val type: String,
    val name: String,
    val ppu: Double,
    val batters: Batters
)

data class Batters(
    val type: String
)

