package com.example.rickmortychallenge.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rickmortychallenge.domain.model.Character
import com.example.rickmortychallenge.theme.Primary
import com.example.rickmortychallenge.theme.SpaceGroteskFontFamily
import com.example.rickmortychallenge.theme.StatusAliveBg
import com.example.rickmortychallenge.theme.StatusAliveDot
import com.example.rickmortychallenge.theme.StatusDeadBg
import com.example.rickmortychallenge.theme.StatusDeadDot
import com.example.rickmortychallenge.theme.StatusUnknownBg
import com.example.rickmortychallenge.theme.StatusUnknownDot

@Composable
fun CharacterCard(
    character: Character,
    modifier: Modifier = Modifier,
    isFeatured: Boolean = false // If true, apply Portal Glow effect
) {
    // Character status specific styling mapped to C-137 rules
    val (statusDotColor, statusBgColor) = when (character.status.lowercase()) {
        "alive" -> StatusAliveDot to StatusAliveBg
        "dead" -> StatusDeadDot to StatusDeadBg
        else -> StatusUnknownDot to StatusUnknownBg
    }

    // Border changes to Portal Green with soft outer glow on active/featured elements
    val cardBorder = if (isFeatured) {
        BorderStroke(1.5.dp, Primary)
    } else {
        BorderStroke(1.dp, Color(0x1AFFFFFF)) // 1px border of #ffffff10
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .let { 
                if (isFeatured) {
                    it.portalGlow(
                        color = Primary,
                        alpha = 0.20f,
                        borderRadius = 16.dp,
                        glowRadius = 12.dp
                    )
                } else {
                    it
                }
            },
        shape = RoundedCornerShape(16.dp), // 1rem corner radius for cards
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF202329) // Level 1 surface card background
        ),
        border = cardBorder
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp)), // Level 2 shapes
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Name (Space Grotesk - bold, spaceship vibe headlines)
                Text(
                    text = character.name,
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Status Tag / Chip (Pill-shape with translucency)
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(statusBgColor)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(statusDotColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${character.status} - ${character.species}",
                        style = MaterialTheme.typography.labelSmall, // Monospaced scientific readout
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Gender (JetBrains Mono Monospaced Readout)
                Text(
                    text = "GENDER // ${character.gender.uppercase()}",
                    style = MaterialTheme.typography.labelSmall, // Monospaced scientific readout style
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}
