package api.json

import kotlinx.serialization.Serializable

@Serializable
data class ResourcePolicyResponse(val content: List<String>) {
    override fun toString(): String {
        return content.toString()
    }
}

@Serializable
data class WebResourceResponse(val content: List<WebResource>)

@Serializable
data class WebResource(val pageName: String, val speedUrl: String, val webResourceId: String) {
    override fun toString(): String {
        return "$pageName ($speedUrl)"
    }
}