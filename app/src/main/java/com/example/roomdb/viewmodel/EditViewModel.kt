package com.example.roomdb.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdb.repositori.RepositoriSiswa
import com.example.roomdb.view.route.DestinasiEditSiswa
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoriSiswa: RepositoriSiswa
) : ViewModel() {
    private val siswaId: Int =
        checkNotNull(savedStateHandle[DestinasiEditSiswa.itemIdArg])

    var uiStateSiswa by mutableStateOf(UIStateSiswa())
        private set

    init {
        viewModelScope.launch {
            val siswa = repositoriSiswa.getSiswaStream(siswaId).filterNotNull().first()
            uiStateSiswa = siswa.toUiStateSiswa(isEntryValid = true)
        }
    }

    fun updateUiState(detailsSiswa: DetailsSiswa) {
        uiStateSiswa = UIStateSiswa(
            detailsSiswa = detailsSiswa,
            isEntryValid = detailsSiswa.nama.isNotBlank() &&
                detailsSiswa.alamat.isNotBlank() &&
                detailsSiswa.telpon.isNotBlank()
        )
    }

    suspend fun updateSiswa() {
        if (uiStateSiswa.isEntryValid) {
            repositoriSiswa.updateSiswa(uiStateSiswa.detailsSiswa.toSiswa())
        }
    }
}