package com.example.roomdb.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdb.repositori.RepositoriSiswa
import com.example.roomdb.view.route.DestinasiDetailSiswa
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class DetailSiswaUiState(
    val detailSiswa: DetailsSiswa = DetailsSiswa()
)

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoriSiswa: RepositoriSiswa
) : ViewModel() {
    private val siswaId: Int =
        checkNotNull(savedStateHandle[DestinasiDetailSiswa.itemIdArg])

    val uiDetailState: StateFlow<DetailSiswaUiState> =
        repositoriSiswa.getSiswaStream(siswaId)
            .map { siswa ->
                DetailSiswaUiState(
                    detailSiswa = siswa?.toDetailsSiswa() ?: DetailsSiswa()
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DetailSiswaUiState()
            )

    suspend fun deleteSiswa() {
        val siswa = uiDetailState.value.detailSiswa.toSiswa()
        if (siswa.id != 0) {
            repositoriSiswa.deleteSiswa(siswa)
        }
    }
}
