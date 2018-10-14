package com.tjohnn.popularmovieskotlin.data.dto

import com.google.gson.annotations.SerializedName

data class Trailer (
        var name: String? = null,
        var size: String? = null,
        @SerializedName(value = "source", alternate = ["key"]) var source: String? = null,
        var type: String? = null
){



    override fun toString(): String {
        return "Trailer{" +
                "name='" + name + '\''.toString() +
                ", size='" + size + '\''.toString() +
                ", source='" + source + '\''.toString() +
                ", type='" + type + '\''.toString() +
                '}'.toString()
    }
}
