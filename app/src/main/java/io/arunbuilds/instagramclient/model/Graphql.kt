package io.arunbuilds.instagramclient.model

import java.util.ArrayList

data class Graphql (var user: User? = null)

data class User(
    var biography: String? = null,
    var blocked_by_viewer: Boolean = false,
    var country_block: Boolean = false,
    var external_url: String? = null,
    var external_url_linkshimmed: String? = null,
    var edge_followed_by: Edgefollowedby? = null,
    var followed_by_viewer: Boolean = false,
    var edgefollow: Edgefollow? = null,
    var follows_viewer: Boolean = false,
    var full_name: String? = null,
    var has_channel: Boolean = false,
    var has_blocked_viewer: Boolean = false,
    var highlight_reel_count: Float = 0.toFloat(),
    var has_requested_viewer: Boolean = false,
    var id: String? = null,
    var is_business_account: Boolean = false,
    var is_joined_recently: Boolean = false,
    var business_category_name: String? = null,
    var is_private: Boolean = false,
    var is_verified: Boolean = false,
    var edgemutualfollowedby: Edgemutualfollowedby? = null,
    var profile_pic_url: String? = null,
    var profile_pic_url_hd: String? = null,
    var requested_by_viewer: Boolean = false,
    var username: String? = null
)

data class Edgefollowedby(var count: Float = 0.toFloat())

data class Edgefollow( var count: Float = 0.toFloat())

data class Edgemutualfollowedby(
    var count: Float = 0.toFloat(),
    var edges: ArrayList<Any> = ArrayList()
)