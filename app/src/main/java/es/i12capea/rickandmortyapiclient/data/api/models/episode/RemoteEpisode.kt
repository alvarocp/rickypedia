package es.i12capea.rickandmortyapiclient.data.api.models.episode

data class RemoteEpisode(
    val id: Int,
    val name: String,
    val air_date: String,
    val episode: String,
    val characters: ArrayList<String>,
    val url: String,
    val created: String
)