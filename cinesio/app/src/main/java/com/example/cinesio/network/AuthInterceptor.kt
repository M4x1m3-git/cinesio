package com.example.cinesio.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhOTJkNGMwMmNlZWQ3MTNlMGMxYmRhZmJjZGMzMGM0NSIsIm5iZiI6MTc3MzE0ODU2Ni45MjYsInN1YiI6IjY5YjAxOTk2OTZlNzExMDdkZWMxZDIyZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.kIJoWCgHHb5oSSobbRncW-lYIgdtmMQHwvGhtm0sjU8")
            .addHeader("accept", "application/json")
            .build()

        return chain.proceed(request)
    }
}