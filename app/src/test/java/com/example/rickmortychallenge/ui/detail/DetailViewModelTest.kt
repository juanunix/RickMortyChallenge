package com.example.rickmortychallenge.ui.detail

import com.example.rickmortychallenge.data.network.CharacterApiService
import com.example.rickmortychallenge.data.repository.NetworkCharacterRepository
import com.example.rickmortychallenge.domain.model.Character
import com.example.rickmortychallenge.domain.repository.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private class FakeDetailApiService(
    private val shouldThrow: Boolean = false
) : CharacterApiService {
    override suspend fun getCharacters(page: Int?, status: String?) = throw NotImplementedError()
    
    override suspend fun getCharacter(id: Int): Character {
        if (shouldThrow) {
            throw RuntimeException("Network Error")
        }
        return Character(
            id = id,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            image = "",
            url = "",
            created = ""
        )
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() {
        val repository = NetworkCharacterRepository(FakeDetailApiService())
        val viewModel = DetailViewModel(repository)
        assertTrue(viewModel.state.value is DetailState.Loading)
    }

    @Test
    fun `LoadDetail action updates state to Success on success`() {
        val repository = NetworkCharacterRepository(FakeDetailApiService(shouldThrow = false))
        val viewModel = DetailViewModel(repository)
        viewModel.onAction(DetailAction.LoadDetail(1))
        val state = viewModel.state.value
        assertTrue(state is DetailState.Success)
        assertEquals("Rick Sanchez", (state as DetailState.Success).character.name)
    }

    @Test
    fun `LoadDetail action updates state to Error on failure`() {
        val repository = NetworkCharacterRepository(FakeDetailApiService(shouldThrow = true))
        val viewModel = DetailViewModel(repository)
        viewModel.onAction(DetailAction.LoadDetail(1))
        val state = viewModel.state.value
        assertTrue(state is DetailState.Error)
        assertEquals("Network Error", (state as DetailState.Error).message)
    }
}
