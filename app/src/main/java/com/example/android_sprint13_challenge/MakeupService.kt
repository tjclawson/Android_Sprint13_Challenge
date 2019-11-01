package com.example.android_sprint13_challenge

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MakeupService {

    @GET("products.json")
    fun getMakeupList(@Query("brand") brand: String): Observable<List<Makeup>>
}