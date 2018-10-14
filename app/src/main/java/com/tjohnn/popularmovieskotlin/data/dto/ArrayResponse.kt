package com.tjohnn.popularmovieskotlin.data.dto

data class ArrayResponse<T> (
        var page: Int = 0,
        var totalResults: Int = 0,
        var totalPages: Int = 0,
        var results: List<T>? = null
){
    override fun toString(): String {
        return "ArrayReponse{" +
                "page=" + page +
                ", totalResults=" + totalResults +
                ", totalPages=" + totalPages +
                ", results=" + results +
                '}'.toString()
    }
}
