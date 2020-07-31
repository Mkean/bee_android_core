package com.bee.launch.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *@Description:
 *
 */

data class AddressNoticeBean(
        @SerializedName("is_need") val is_need: Boolean,

        @SerializedName("top") val top: String,

        @SerializedName("center") val center: String,

        @SerializedName("footer") val footer: String

) : Serializable {

    override fun toString(): String {

        return "AddressNoticeBean(" +
                "is_need=$is_need," +
                " top='$top'," +
                " center='$center'," +
                " footer='$footer')"
    }
}