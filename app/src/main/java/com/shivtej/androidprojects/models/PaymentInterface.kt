package com.shivtej.androidprojects.models

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentInterface {

    @POST("/getOrderId")
    fun getOrderId(@Body map: HashMap<String, String>): Call<Order>

    @POST("/updateTransactionStatus")
    fun updateTransaction(@Body map: HashMap<String, String>): Call<String>

}