package com.cocido.formokaizen.utils

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        
        // Skip auth for login and register endpoints
        val url = request.url.toString()
        if (url.contains("/auth/login/") || url.contains("/auth/register/")) {
            return chain.proceed(request)
        }
        
        // Add auth token to other requests
        val token = runBlocking { tokenManager.getAccessToken() }
        
        return if (token != null) {
            val authenticatedRequest = request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            chain.proceed(authenticatedRequest)
        } else {
            chain.proceed(request)
        }
    }
}