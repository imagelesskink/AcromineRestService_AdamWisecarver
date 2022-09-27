package com.example.acrominerestservice_adamwisecarver.data.api

import com.example.acrominerestservice_adamwisecarver.utils.ApiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface AcromineRepository {
    fun fetchAbbreviation(sf: String): Flow<ApiState>
}
class AcromineRepositoryImpl @Inject constructor(private val service: ApiService) :
    AcromineRepository {
    override fun fetchAbbreviation(sf: String): Flow<ApiState> =
        flow {
            emit(ApiState.Loading)
            try {
                val response = service.fetchAbbreviation(sf)

                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    emit(response.body()?.let {
                        ApiState.Success(it[0])
                    } ?: throw Exception ("Null Response"))
                } else {
                    throw Exception("Could not find any matches")
                }
            } catch (e:Exception) {
                emit(ApiState.Error(e))
            }
        }
}