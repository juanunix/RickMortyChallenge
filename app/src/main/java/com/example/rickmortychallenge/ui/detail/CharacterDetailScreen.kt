package com.example.rickmortychallenge.ui.detail

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.rickmortychallenge.domain.model.Character
import com.example.rickmortychallenge.theme.Primary
import com.example.rickmortychallenge.theme.RickMortyChallengeTheme
import com.example.rickmortychallenge.theme.Secondary
import com.example.rickmortychallenge.theme.SpaceGroteskFontFamily
import com.example.rickmortychallenge.theme.StatusAliveBg
import com.example.rickmortychallenge.theme.StatusAliveDot
import com.example.rickmortychallenge.theme.StatusDeadBg
import com.example.rickmortychallenge.theme.StatusDeadDot
import com.example.rickmortychallenge.theme.StatusUnknownBg
import com.example.rickmortychallenge.theme.StatusUnknownDot
import com.example.rickmortychallenge.ui.components.ErrorScreen
import com.example.rickmortychallenge.ui.components.LoadingScreen
import com.example.rickmortychallenge.ui.components.portalGlow
import org.koin.androidx.compose.koinViewModel

// Composable Root split: handles dependency injection via koinViewModel() and load trigger
@Composable
fun CharacterDetailRoot(
    characterId: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(characterId) {
        viewModel.onAction(DetailAction.LoadDetail(characterId))
    }

    CharacterDetailScreen(
        state = state,
        onAction = viewModel::onAction,
        onBackClick = onBackClick,
        characterId = characterId,
        modifier = modifier
    )
}

// Pure Composable Screen split: has zero business logic and zero ViewModel dependency
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    state: DetailState,
    onAction: (DetailAction) -> Unit,
    onBackClick: () -> Unit,
    characterId: Int,
    modifier: Modifier = Modifier
) {
    val layoutDirection = LocalLayoutDirection.current
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Character Details", 
                        fontFamily = SpaceGroteskFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Back",
                            tint = Primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { Toast.makeText(context, "More analysis specs classified", Toast.LENGTH_SHORT).show() }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options",
                            tint = Primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xEC101416) // surface background with glass opacity
                )
            )
        },
        bottomBar = {
            // Pill-shaped Bottom Navigation Bar matching the list screen pro
            NavigationBar(
                containerColor = Color(0xFF1C2022), // surface-container
                tonalElevation = 8.dp,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { onBackClick() }, // Return back to list
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateStartPadding(layoutDirection),
                    end = innerPadding.calculateEndPadding(layoutDirection)
                )
                .background(Color(0xFF101416)) // Level 0 Void background
        ) {
            when (state) {
                is DetailState.Loading -> LoadingScreen()
                is DetailState.Success -> DetailContent(
                    character = state.character,
                    bottomPadding = innerPadding.calculateBottomPadding()
                )
                is DetailState.Error -> ErrorScreen(
                    message = state.message,
                    onRetry = { onAction(DetailAction.Retry(characterId)) }
                )
            }
        }
    }
}

@Composable
fun DetailContent(
    character: Character,
    bottomPadding: Dp,
    modifier: Modifier = Modifier
) {
    val (statusDotColor, statusBgColor) = when (character.status.lowercase()) {
        "alive" -> StatusAliveDot to StatusAliveBg
        "dead" -> StatusDeadDot to StatusDeadBg
        else -> StatusUnknownDot to StatusUnknownBg
    }

    // Dynamic mock fields customized based on character details for ultra fidelity
    val originPlanet = when (character.id) {
        1 -> "Earth (C-137)"
        2 -> "Earth (C-137)"
        3 -> "Earth (Replacement Dimension)"
        4 -> "Earth (Replacement Dimension)"
        else -> "Classified Space Quadrant"
    }

    val characterBio = when (character.id) {
        1 -> "The smartest man in the universe. Rick is an alcoholic genius who drags his grandson Morty on dangerous adventures across the multiverse."
        2 -> "Rick's good-natured but easily distressed 14-year-old grandson. He is frequently dragged into chaotic interdimensional expeditions."
        3 -> "Morty's 17-year-old sister, Summer is a typical high school teenager who occasionally joins her grandfather on sci-fi escapades."
        4 -> "Rick's daughter, Morty's mother, and a horse heart surgeon. Beth is ambitious, strong-willed, and deals with complex family dynamics."
        else -> "Specimen analysis profile. Drags their companions on dangerous adventures across the multiverse."
    }

    val lastKnownLocation = when (character.id) {
        1, 2 -> "Citadel of Ricks"
        3, 4 -> "Earth (Replacement Dimension)"
        else -> "Unknown Federation Planet"
    }

    val firstSeenEpisode = when (character.id) {
        1, 2, 3, 4 -> "Pilot (S01E01)"
        else -> "Anatomy Park (S01E03)"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 24.dp + bottomPadding
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Simplified Header Section: Circular Avatar Image frame with soft border and Portal Glow
        Box(
            modifier = Modifier
                .size(192.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            // Soft Radial Glow
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .portalGlow(
                        color = Primary,
                        alpha = 0.20f,
                        borderRadius = 96.dp,
                        glowRadius = 16.dp
                    )
            )

            // Circular Image with surface container border
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color(0xFF1C2022)) // surface-container
                    .border(4.dp, Color(0xFF1C2022), CircleShape), // border-4 border-surface-container
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Name in Space Grotesk
        Text(
            text = character.name,
            fontFamily = SpaceGroteskFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        // Subtitle Origin Planet (Inter, Primary tint)
        Text(
            text = originPlanet,
            style = MaterialTheme.typography.bodyLarge,
            color = Primary.copy(alpha = 0.8f),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )

        // Row of small Status/Species chips
        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status Chip
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.1f))
                    .border(BorderStroke(1.dp, Primary.copy(alpha = 0.2f)), CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(statusDotColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = character.status.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
            }

            // Species Chip
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
                    .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = character.species.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Brief Bio description
        Text(
            text = characterBio,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Information Rows Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoRowItem(
                icon = Icons.Default.LocationOn,
                iconColor = Primary.copy(alpha = 0.6f),
                label = "Last Location",
                value = lastKnownLocation
            )
            InfoRowItem(
                icon = Icons.Default.Movie,
                iconColor = Secondary.copy(alpha = 0.6f),
                label = "First Seen In",
                value = firstSeenEpisode
            )
            InfoRowItem(
                icon = Icons.Default.Science,
                iconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                label = "Species",
                value = character.species
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Recent Episodes Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Episodes",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "View All",
                style = MaterialTheme.typography.bodySmall,
                color = Primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { /* View all click */ }
            )
        }

        // Episodes List Container
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF181C1E) // surface-container-low
            ),
            border = BorderStroke(1.dp, Color(0x1AFFFFFF)) // border border-white/5
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                EpisodeListItem(title = "Fear No Mort", code = "S07E10 • Dec 17, 2023")
                EpisodeListItem(title = "Mort: Dinner Rick-and-Morty", code = "S07E09 • Dec 10, 2023", showDivider = true)
                EpisodeListItem(title = "Rise of the Numbericons", code = "S07E08 • Dec 3, 2023", showDivider = true)
            }
        }
    }
}

@Composable
fun InfoRowItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1C2022).copy(alpha = 0.4f)) // bg-surface-container/40
            .border(BorderStroke(1.dp, Color(0x0DFFFFFF)), RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun EpisodeListItem(
    title: String,
    code: String,
    showDivider: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (showDivider) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0x0DFFFFFF)) // divide-white/5
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Episode detail */ }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = code,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "Detail Screen - Loading State")
@Composable
private fun CharacterDetailScreenLoadingPreview() {
    RickMortyChallengeTheme {
        CharacterDetailScreen(
            state = DetailState.Loading,
            onAction = {},
            onBackClick = {},
            characterId = 1
        )
    }
}

@Preview(showBackground = true, name = "Detail Screen - Success State")
@Composable
private fun CharacterDetailScreenSuccessPreview() {
    val mockCharacter = Character(
        id = 1,
        name = "Rick Sanchez",
        status = "Alive",
        species = "Human",
        type = "Scientist",
        gender = "Male",
        image = "",
        url = "https://rickandmortyapi.com/api/character/1",
        created = "2017-11-04T18:48:46.250Z"
    )
    RickMortyChallengeTheme {
        CharacterDetailScreen(
            state = DetailState.Success(mockCharacter),
            onAction = {},
            onBackClick = {},
            characterId = 1
        )
    }
}

@Preview(showBackground = true, name = "Detail Screen - Error State")
@Composable
private fun CharacterDetailScreenErrorPreview() {
    RickMortyChallengeTheme {
        CharacterDetailScreen(
            state = DetailState.Error("Unable to load character details. Please check your internet connection."),
            onAction = {},
            onBackClick = {},
            characterId = 1
        )
    }
}
