package com.ysn.cctvexo.model

data class CctvData(
    val data: List<Data>
)

data class Data(
    val name: String,
    val url: String
)