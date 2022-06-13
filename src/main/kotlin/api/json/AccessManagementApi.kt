package api.json

import kotlinx.serialization.Serializable

@Serializable
data class ResourcePolicyResponse(val content: List<String>)

@Serializable
data class WebResourceResponse(val content: List<WebResource>)

@Serializable
data class WebResource(val pageName: String, val speedUrl: String, val webResourceId: String)