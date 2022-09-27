package com.example.acrominerestservice_adamwisecarver.utils
import com.example.acrominerestservice_adamwisecarver.data.model.AbbreviationList

sealed class ApiState {
    object Loading: ApiState()
    class Error(val exception: Exception): ApiState()
    class Success(val response: AbbreviationList): ApiState()
}
