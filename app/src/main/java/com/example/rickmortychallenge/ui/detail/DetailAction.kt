package com.example.rickmortychallenge.ui.detail

sealed interface DetailAction {
    data class LoadDetail(val id: Int) : DetailAction
    data class Retry(val id: Int) : DetailAction
}
