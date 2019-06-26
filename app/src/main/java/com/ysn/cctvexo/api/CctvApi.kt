package com.ysn.cctvexo.api

import com.ysn.cctvexo.model.CctvData
import retrofit2.Response
import retrofit2.http.GET

interface CctvApi {

    @GET("cctv")
    suspend fun getListCctv(): Response<CctvData>

}