package com.example.acrominerestservice_adamwisecarver.data.api

import com.example.acrominerestservice_adamwisecarver.data.model.AbbreviationList
import com.example.acrominerestservice_adamwisecarver.utils.DICTIONARY_ENDPOINT
import com.example.acrominerestservice_adamwisecarver.utils.SF
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(DICTIONARY_ENDPOINT)
    suspend fun fetchAbbreviation(
        @Query(SF) sf: String
    ): Response<List<AbbreviationList>>
}