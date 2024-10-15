package com.dicoding.restaurantreview.data.retrofit
import com.dicoding.restaurantreview.data.response.DetailEventResponse
import com.dicoding.restaurantreview.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active") active: Int
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id: String
    ): Call<DetailEventResponse>

}

//    @FormUrlEncoded
//    @Headers("Authorization: token 12345")
//    @POST("review")
//    fun postReview(
//        @Field("id") id: String,
//        @Field("name") name: String,
//        @Field("review") review: String
//    ): Call<PostReviewResponse>
