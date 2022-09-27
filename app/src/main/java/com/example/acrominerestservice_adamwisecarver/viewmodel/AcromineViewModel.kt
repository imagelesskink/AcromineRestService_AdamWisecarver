package com.example.acrominerestservice_adamwisecarver.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acrominerestservice_adamwisecarver.data.api.AcromineRepository
import com.example.acrominerestservice_adamwisecarver.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AcromineViewModel @Inject constructor(
    private val repository: AcromineRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _stateLiveData = MutableLiveData<ApiState>()
    val stateLiveData: LiveData<ApiState> get() = _stateLiveData

    fun fetchAbbreviation(sf: String) {
        viewModelScope.launch(dispatcher) {
            repository.fetchAbbreviation(sf).collect {
                _stateLiveData.postValue(it)
            }
        }
    }
}