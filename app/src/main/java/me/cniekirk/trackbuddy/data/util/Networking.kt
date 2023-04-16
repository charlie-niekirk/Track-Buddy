package me.cniekirk.trackbuddy.data.util

import android.util.Log
import retrofit2.Response
import timber.log.Timber

class UnexpectedError : Error() {

    override fun toString(): String {
        return "UnexpectedError"
    }
}


/**
 * Represents an error in which the server could not be reached.
 */
class NetworkError : Error() {

    override fun toString(): String {
        return "NetworkError"
    }
}


/**
 * An error response from the server.
 */
open class RemoteServiceError : Error() {

    override fun toString(): String {
        return "RemoteServiceError(message: $message)"
    }
}

sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val error: Error) : Result<Nothing>()
}

suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T> {
    return try {
        val response = call.invoke()
        if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else {
            if (response.code() in 400..499) {
                Result.Failure(RemoteServiceError())
            } else if (response.code() in 500..599) {
                Result.Failure(RemoteServiceError())
            } else {
                Result.Failure(UnexpectedError())
            }
        }
    } catch (error: Throwable) {
        Timber.e(error)
        Result.Failure(NetworkError())
    }
}