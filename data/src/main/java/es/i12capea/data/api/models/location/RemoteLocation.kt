package es.i12capea.data.api.models.location

data class RemoteLocation(
    val id : Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: ArrayList<String>,
    val url: String,
    val created: String
)