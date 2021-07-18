package com.demo.developer.deraesw.demomoviewes.core.data.model

import com.google.gson.annotations.SerializedName

class NetworkError(
    @SerializedName("status_message") var statusMessage: String,
    @SerializedName("status_code") var statusCode: Int
)

class NetworkException(networkError: NetworkError) : Throwable(networkError.statusMessage)