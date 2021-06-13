package com.demo.developer.deraesw.demomoviewes.data.model

sealed class NetworkResults
class NetworkSuccess<out T>(val data: T) : NetworkResults()
class NetworkFailed(val errors: NetworkError) : NetworkResults()