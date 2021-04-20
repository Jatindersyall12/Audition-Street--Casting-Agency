package com.silo.utils.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InternetInterceptor @Inject constructor(private val networkMonitor: NetworkHelper) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (networkMonitor.isNetworkConnected()) {
            val oldRequest = chain.request()
            chain.proceed(oldRequest)
        } else {
            throw NoNetworkException()
        }
    }
}
