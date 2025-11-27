package com.example.tvapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApiService {
    @GET("breeds/image/random")
    fun getRandomDog(): Call<DogResponse>
    
    @GET("breeds/list/all")
    fun getAllBreeds(): Call<BreedsResponse>
    
    @GET("breed/{breed}/images/random")
    fun getRandomDogByBreed(@Path("breed") breed: String): Call<DogResponse>
    
    @GET("breed/{breed}/images")
    fun getDogImagesByBreed(@Path("breed") breed: String): Call<DogImagesResponse>
    
    @GET("breed/{breed}/{subBreed}/images/random")
    fun getRandomDogBySubBreed(
        @Path("breed") breed: String,
        @Path("subBreed") subBreed: String
    ): Call<DogResponse>
}
