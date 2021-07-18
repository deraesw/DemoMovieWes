package com.demo.developer.deraesw.demomoviewes.core.data.model

sealed class NetworkResults
class NetworkSuccess<out T>(val data: T) : NetworkResults()
class NetworkFailed(val errors: NetworkError) : NetworkResults()