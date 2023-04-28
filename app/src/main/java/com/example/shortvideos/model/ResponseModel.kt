package com.example.shortvideos.model

data class ResponseModel(
    val hits: MutableList<Hit>,
    val total: Int,
    val totalHits: Int
)