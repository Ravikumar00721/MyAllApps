package msi.crool.myshoppinglist

data class LocationData(
    val latitude:Double,
    val longitude:Double
)

data class GeoCodingResponse(
    val results:List<GeoCodingResult>,
    val status:String
)

data class GeoCodingResult(
    val Formattedadddress:String
)

