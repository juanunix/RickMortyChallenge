package com.example.rickmortychallenge.ui.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "CHARACTER ANALYSIS //", 
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF101416)
                )
            )
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 16.dp,
                bottom = 24.dp + bottomPadding
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // High quality photo frame with portal glow
        Box(
            modifier = Modifier
                .padding(8.dp)
                .portalGlow(
                    color = Primary,
                    alpha = 0.25f,
                    borderRadius = 24.dp,
                    glowRadius = 16.dp
                )
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .size(220.dp)
                    .clip(RoundedCornerShape(24.dp)), // Cards & modals corner radius (1rem / 16dp / 24dp)
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Character Name in Space Grotesk
        Text(
            text = character.name.uppercase(),
            fontFamily = SpaceGroteskFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.onBackground,
            letterSpacing = (-0.01).sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Status tag (Pill shape rounding)
        Row(
            modifier = Modifier
                .clip(CircleShape)
                .background(statusBgColor)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(statusDotColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${character.status} - ${character.species}",
                style = MaterialTheme.typography.labelSmall, // Scientific readout
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Analysis specs card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .portalGlow(
                    color = Secondary,
                    alpha = 0.10f,
                    borderRadius = 16.dp,
                    glowRadius = 8.dp
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF202329) // Level 1 surface card
            ),
            border = BorderStroke(1.dp, Color(0x1AFFFFFF)) // 1px white border at 10% opacity
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Header of readout
                Text(
                    text = "SPECIMEN DATA READOUT //",
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Secondary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                DetailItem(label = "CLASSIFIED GENDER", value = character.gender.uppercase())
                DetailItem(label = "MUTATION TYPE", value = if (character.type.isEmpty()) "STANDARD" else character.type.uppercase())
                DetailItem(label = "TEMPORAL CREATION", value = character.created)
                
                val context = androidx.compose.ui.platform.LocalContext.current
                DetailItem(
                    label = "COSMIC INTERNET PROFILE URL", 
                    value = character.url,
                    onClick = {
                        try {
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(character.url))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Safe fallback
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DetailItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .let {
                if (onClick != null) {
                    it.clickable { onClick() }
                } else {
                    it
                }
            }
    ) {
        // Label using monospaced technical font
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            letterSpacing = 0.05.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        val baseStyle = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp)
        val textStyle = if (onClick != null) {
            baseStyle.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline)
        } else {
            baseStyle
        }

        // Value using monospaced technical font
        Text(
            text = value,
            style = textStyle,
            fontWeight = FontWeight.Normal,
            color = if (onClick != null) Secondary else MaterialTheme.colorScheme.onSurfaceVariant
        )
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
