package com.example.chatwithai.domain.use_case

import com.example.chatwithai.common.Resource
import com.example.chatwithai.data.remote.dto.toResponse
import com.example.chatwithai.domain.model.Response
import com.example.chatwithai.domain.repository.ResponseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class RequestUseCase @Inject constructor(
    private val repository: ResponseRepository
) {
    operator fun invoke(prompt: String): Flow<Resource<Response>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.request(prompt).toResponse()
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection: ${e.message}"))
        } catch (e: SocketTimeoutException) {
            emit(Resource.Error("Couldn't reach server: ${e.message}"))
        }
    }
}