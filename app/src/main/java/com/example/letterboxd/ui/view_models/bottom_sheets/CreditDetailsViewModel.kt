package com.example.letterboxd.ui.view_models.bottom_sheets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxd.domain.model.CreditDetailsItem
import com.example.letterboxd.domain.model.PersonDetailsItem
import com.example.letterboxd.domain.repository.CreditDetailsRepository
import com.example.letterboxd.domain.repository.PersonDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreditDetailsViewModel @Inject constructor(
    private val repository : CreditDetailsRepository,
    private val repository2 : PersonDetailsRepository,
) : ViewModel() {
    val creditDetailsToDisplay = MutableStateFlow<CreditDetailsItem?>(null)
    val personDetailsToDisplay = MutableStateFlow<PersonDetailsItem?>(null)

    fun getCreditDetails(id : String) {
        viewModelScope.launch {
            val response = repository.getCreditDetails(id)
            creditDetailsToDisplay.emit(response)

            val personId = response.personId
            val personResponse = repository2.getPersonDetails(personId)
            personDetailsToDisplay.emit(personResponse)
        }
    }
}