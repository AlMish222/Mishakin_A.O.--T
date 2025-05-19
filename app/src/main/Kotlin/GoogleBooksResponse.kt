import com.google.gson.annotations.SerializedName

data class GoogleBooksResponse(
    @SerializedName("items") val items: List<GoogleBookItem>?
)

data class GoogleBookItem(
    @SerializedName("id") val id: String,
    @SerializedName("volumeInfo") val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    @SerializedName("title") val title: String?,
    @SerializedName("authors") val authors: List<String>?,
    @SerializedName("pageCount") val pageCount: Int?,
    @SerializedName("industryIdentifiers") val identifiers: List<IndustryIdentifier>?
)

data class IndustryIdentifier(
    @SerializedName("type") val type: String?,
    @SerializedName("identifier") val identifier: String?
)