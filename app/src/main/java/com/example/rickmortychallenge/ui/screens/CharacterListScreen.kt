package com.example.rickmortychallenge.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.rickmortychallenge.domain.model.Character
import com.example.rickmortychallenge.theme.Primary
import com.example.rickmortychallenge.theme.RickMortyChallengeTheme
import com.example.rickmortychallenge.theme.Secondary
import com.example.rickmortychallenge.theme.SpaceGroteskFontFamily
import com.example.rickmortychallenge.ui.components.CharacterCard
import com.example.rickmortychallenge.ui.components.ErrorScreen
import com.example.rickmortychallenge.ui.components.LoadingScreen
import com.example.rickmortychallenge.ui.components.PortalLoader
import com.example.rickmortychallenge.ui.components.StatusFilterChips
import com.example.rickmortychallenge.ui.viewmodel.CharacterAction
import com.example.rickmortychallenge.ui.viewmodel.CharacterEvent
import com.example.rickmortychallenge.ui.viewmodel.CharacterState
import com.example.rickmortychallenge.ui.viewmodel.CharacterViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun <T> ObserveAsEvents(flow: Flow<T>, onEvent: (T) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(flow, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(onEvent)
        }
    }
}

// Composable Root split: handles dependency injection via koinViewModel() and event observation
@Composable
fun CharacterListRoot(
    onCharacterSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharacterViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CharacterEvent.ShowToast -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
            is CharacterEvent.NavigateToDetail -> {
                onCharacterSelected(event.characterId)
            }
        }
    }

    CharacterListScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

// Pure Composable Screen split: has zero business logic and zero ViewModel dependency
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    state: CharacterState,
    onAction: (CharacterAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagingItems = state.pagingDataFlow.collectAsLazyPagingItems()
    val layoutDirection = LocalLayoutDirection.current
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Rick & Morty Characters",
                        fontFamily = SpaceGroteskFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF101416) // Cosmic void background
                )
            )
        },
        bottomBar = {
            // Pill-shaped Bottom Navigation Bar styled like the HTML mock
            NavigationBar(
                containerColor = Color(0xFF1C2022), // surface-container
                tonalElevation = 8.dp,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Already on characters list */ },
                    icon = { Icon(Icons.Default.Group, contentDescription = "Characters") },
                    label = { Text("Characters", style = MaterialTheme.typography.labelSmall) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF101416),
                        selectedTextColor = Primary,
                        indicatorColor = Primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { Toast.makeText(context, "Locations portal offline", Toast.LENGTH_SHORT).show() },
                    icon = { Icon(Icons.Default.Public, contentDescription = "Locations") },
                    label = { Text("Locations", style = MaterialTheme.typography.labelSmall) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { Toast.makeText(context, "Episodes portal offline", Toast.LENGTH_SHORT).show() },
                    icon = { Icon(Icons.Default.Movie, contentDescription = "Episodes") },
                    label = { Text("Episodes", style = MaterialTheme.typography.labelSmall) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateStartPadding(layoutDirection),
                    end = innerPadding.calculateEndPadding(layoutDirection)
                )
                .background(Color(0xFF101416)) // Level 0 background
        ) {
            // Search Input styled like custom Tailwind mockup in HTML
            TextField(
                value = state.searchQuery,
                onValueChange = { query ->
                    onAction(CharacterAction.SearchCharacters(query))
                },
                placeholder = { 
                    Text(
                        text = "Search characters...", 
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    ) 
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = if (state.searchQuery.isNotEmpty()) Secondary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF272B2D), // surface-container-high
                    unfocusedContainerColor = Color(0xFF1C2022), // surface-container
                    disabledContainerColor = Color(0xFF1C2022),
                    focusedIndicatorColor = Secondary, // focus glow ring
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Secondary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Species Filter Chips Row styled like mockup
            val speciesList = listOf("Human", "Alien", "Poopybutthole")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = state.filterSpecies == null,
                    onClick = { onAction(CharacterAction.FilterBySpecies(null)) },
                    shape = CircleShape, // Pill rounding
                    label = { Text("ALL CHARACTERS", style = MaterialTheme.typography.labelSmall) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Primary,
                        selectedLabelColor = Color(0xFF101416),
                        containerColor = Color(0xFF202329),
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = Color(0x1AFFFFFF),
                        selectedBorderColor = Primary,
                        enabled = true,
                        selected = state.filterSpecies == null
                    )
                )

                speciesList.forEach { species ->
                    val isSelected = state.filterSpecies?.lowercase() == species.lowercase()
                    FilterChip(
                        selected = isSelected,
                        onClick = { onAction(CharacterAction.FilterBySpecies(species)) },
                        shape = CircleShape,
                        label = { Text(species.uppercase(), style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Primary,
                            selectedLabelColor = Color(0xFF101416),
                            containerColor = Color(0xFF202329),
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = Color(0x1AFFFFFF),
                            selectedBorderColor = Primary,
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }

            // Status Filter Chips as secondary filter layout
            StatusFilterChips(
                selectedStatus = state.filterStatus,
                onStatusSelected = { status ->
                    onAction(CharacterAction.FilterByStatus(status))
                }
            )

            val isRefreshing = pagingItems.loadState.refresh is LoadState.Loading

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { pagingItems.refresh() },
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    pagingItems.loadState.refresh is LoadState.Loading && pagingItems.itemCount == 0 -> {
                        LoadingScreen()
                    }
                    pagingItems.loadState.refresh is LoadState.Error && pagingItems.itemCount == 0 -> {
                        val error = (pagingItems.loadState.refresh as LoadState.Error).error
                        ErrorScreen(
                            message = error.message ?: "Transmission failed. Let's fire the portal gun again.",
                            onRetry = { pagingItems.retry() }
                        )
                    }
                    else -> {
                        CharacterPagingList(
                            pagingItems = pagingItems,
                            bottomPadding = innerPadding.calculateBottomPadding(),
                            onItemClick = { character ->
                                onAction(CharacterAction.SelectCharacter(character.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterPagingList(
    pagingItems: LazyPagingItems<Character>,
    bottomPadding: Dp,
    onItemClick: (Character) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 340.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 8.dp,
            bottom = 8.dp + bottomPadding
        )
    ) {
        items(
            count = pagingItems.itemCount,
            key = { index -> pagingItems[index]?.id ?: index }
        ) { index ->
            val character = pagingItems[index]
            if (character != null) {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick(character) }
                    ) {
                        val isFeatured = character.id % 4 == 1
                        CharacterCard(
                            character = character,
                            isFeatured = isFeatured
                        )
                    }
                }
            }
        }

        if (pagingItems.loadState.append is LoadState.Loading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                PortalLoader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    size = 50.dp
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "List Screen - Success State")
@Composable
private fun CharacterListScreenSuccessPreview() {
    val mockCharacters = listOf(
        Character(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            type = "Scientist",
            gender = "Male",
            image = "",
            url = "",
            created = ""
        ),
        Character(
            id = 2,
            name = "Morty Smith",
            status = "Alive",
            species = "Human",
            type = "Student",
            gender = "Male",
            image = "",
            url = "",
            created = ""
        ),
        Character(
            id = 3,
            name = "Summer Smith",
            status = "Alive",
            species = "Human",
            type = "None",
            gender = "Female",
            image = "",
            url = "",
            created = ""
        )
    )
    RickMortyChallengeTheme {
        CharacterListScreen(
            state = CharacterState(
                pagingDataFlow = flowOf(PagingData.from(mockCharacters)),
                filterStatus = null
            ),
            onAction = {}
        )
    }
}
