package com.example.rickmortychallenge.ui.viewmodel

import com.example.rickmortychallenge.data.network.CharacterApiService
import com.example.rickmortychallenge.data.repository.NetworkCharacterRepository
import com.example.rickmortychallenge.domain.repository.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

private class FakeApiService : CharacterApiService {
    override suspend fun getCharacters(page: Int?, status: String?, name: String?, species: String?) = throw NotImplementedError()
    override suspend fun getCharacter(id: Int) = throw NotImplementedError()
}

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: CharacterRepository
    private lateinit var viewModel: CharacterViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val apiService = FakeApiService()
        repository = NetworkCharacterRepository(apiService)
        viewModel = CharacterViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() {
        val state = viewModel.state.value
        assertEquals(null, state.filterStatus)
    }

    @Test
    fun `FilterByStatus action updates state`() {
        viewModel.onAction(CharacterAction.FilterByStatus("Alive"))
        assertEquals("Alive", viewModel.state.value.filterStatus)
    }
}
