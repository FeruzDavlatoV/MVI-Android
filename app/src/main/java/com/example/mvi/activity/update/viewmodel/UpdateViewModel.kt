package com.example.mvi.activity.update.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvi.activity.update.intentstate.UpdateIntent
import com.example.mvi.activity.update.intentstate.UpdateState
import com.example.mvi.model.Post
import com.example.mvi.repository.UpdateRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class UpdateViewModel(private val updateRepository: UpdateRepository) : ViewModel() {

    val updateIntent = Channel<UpdateIntent> { Channel.UNLIMITED }

    private val _state = MutableStateFlow<UpdateState>(UpdateState.Init)
    val state: StateFlow<UpdateState> get() = _state


    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            updateIntent.consumeAsFlow().collect {
                when (it) {
                    is UpdateIntent.UpdatePost -> updatePost(it.id, it.post)
                }
            }
        }
    }

    private fun updatePost(id: Int, post: Post) {
        viewModelScope.launch {
            _state.value = UpdateState.Loading

            _state.value = try {
                UpdateState.UpdatePost(id, updateRepository.updatePost(id, post))
            } catch (e: Exception) {
                UpdateState.Error(e.localizedMessage)
            }
        }
    }

}