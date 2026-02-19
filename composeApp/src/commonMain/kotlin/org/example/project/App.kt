package org.example.project

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ==========================================
// DESIGN SYSTEM — CLEAN SLATE LIGHT THEME
// ==========================================

// ── Background & Surface ──────────────────
private val BgApp      = Color(0xFFF8FAFC)
private val SurfaceW   = Color(0xFFFFFFFF)
private val Surface2   = Color(0xFFF1F5F9)

// ── Border & Outline ─────────────────────
private val Border     = Color(0xFFE2E8F0)

// ── Warna Aksen (Pop Colors) ──────────────
private val AccentTeal      = Color(0xFF0D9488)
private val AccentTealDim   = Color(0x1A0D9488)
private val AccentBlue      = Color(0xFF0284C7)
private val AccentBlueDim   = Color(0x1A0284C7)
private val AccentViolet    = Color(0xFF7C3AED)
private val AccentVioletDim = Color(0x1A7C3AED)
private val AccentOrange    = Color(0xFFEA580C)
private val AccentOrangeDim = Color(0x1AEA580C)

// ── Text ──────────────────────────────────
private val TextDark   = Color(0xFF0F172A)
private val TextMid    = Color(0xFF475569)
private val TextSoft   = Color(0xFF94A3B8)

private val LightColorScheme = lightColorScheme(
    background       = BgApp,
    surface          = SurfaceW,
    surfaceVariant   = Surface2,
    primary          = AccentBlue,
    onPrimary        = Color.White,
    onBackground     = TextDark,
    onSurface        = TextDark,
    onSurfaceVariant = TextMid,
    secondary        = TextMid,
    outline          = Border
)

// ==========================================
// MODEL DATA & VISUALS
// ==========================================

data class RawNews(val id: Int, val title: String, val category: String, val timestamp: Long)
data class DisplayNews(val id: Int, val title: String, val category: String, val timeInfo: String)

data class CatVisual(
    val label: String,
    val emoji: String,
    val color: Color,
    val dimColor: Color,
    val gradStart: Color,
    val gradEnd: Color
)

private val catVisuals = mapOf(
    "Tech" to CatVisual(
        label     = "Tech",
        emoji     = "💻",
        color     = AccentBlue,
        dimColor  = AccentBlueDim,
        gradStart = Color(0xFF0284C7),
        gradEnd   = Color(0xFF38BDF8)
    ),
    "Campus" to CatVisual(
        label     = "Campus",
        emoji     = "🎓",
        color     = AccentViolet,
        dimColor  = AccentVioletDim,
        gradStart = Color(0xFF7C3AED),
        gradEnd   = Color(0xFFA78BFA)
    ),
    "Sports" to CatVisual(
        label     = "Sports",
        emoji     = "⚽",
        color     = AccentOrange,
        dimColor  = AccentOrangeDim,
        gradStart = Color(0xFFEA580C),
        gradEnd   = Color(0xFFFDBA74)
    )
)

enum class CategoryTab(val label: String, val emoji: String, val color: Color) {
    ALL   ("All",    "🗞️", AccentTeal),
    TECH  ("Tech",   "💻", AccentBlue),
    CAMPUS("Campus", "🎓", AccentViolet),
    SPORTS("Sports", "⚽", AccentOrange)
}

// ==========================================
// MANAGER / REPOSITORY (LOGIKA KMP)
// ==========================================

class NewsFeedManager {
    private val categories = listOf("Tech", "Campus", "Sports")
    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount.asStateFlow()

    private val rawNewsFlow: Flow<RawNews> = flow {
        var count = 1
        while (true) {
            emit(RawNews(
                id        = count,
                title     = sampleTitles.getOrElse((count - 1) % sampleTitles.size) { "Update #$count" },
                category  = categories.random(),
                timestamp = System.currentTimeMillis()
            ))
            count++
            delay(2000)
        }
    }

    fun getProcessedFeed(filter: String): Flow<DisplayNews> =
        rawNewsFlow
            .filter { it.category == filter || filter == "All" }
            .map { DisplayNews(it.id, it.title, it.category, "Baru saja") }

    fun incrementReadCount() { _readCount.value++ }

    suspend fun fetchNewsDetailAsync(newsId: Int): String =
        withContext(Dispatchers.Default) {
            delay(1500)
            "Artikel #$newsId mengupas perkembangan terbaru yang berdampak langsung pada " +
                    "komunitas kampus dan ekosistem teknologi. Data dikumpulkan dari berbagai sumber " +
                    "terpercaya dan disampaikan secara real-time melalui arsitektur Flow Kotlin."
        }

    companion object {
        val sampleTitles = listOf(
            "Peneliti ITERA Kembangkan Sistem Pertanian Cerdas Berbasis IoT",
            "Mahasiswa Raih Emas di Kompetisi Robotika Internasional",
            "Fakultas Teknik Buka Program Studi Kecerdasan Buatan",
            "Tim Basket Putri ITERA Juara Nasional Antar PTN",
            "Workshop Cybersecurity Diikuti 500 Peserta dari 30 Kota",
            "Riset Energi Terbarukan ITERA Diterbitkan di Jurnal Scopus Q1",
            "Liga Futsal Mahasiswa: Semifinal Penuh Drama",
            "Kolaborasi ITERA dan Google Hadirkan Beasiswa Cloud Computing"
        )
    }
}

// ==========================================
// APP ROOT
// ==========================================

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun App() {
    val manager   = remember { NewsFeedManager() }
    val scope     = rememberCoroutineScope()
    val readCount by manager.readCount.collectAsState()

    var selectedCategory by remember { mutableStateOf("All") }
    val newsFeed       = remember { mutableStateListOf<DisplayNews>() }
    val expandedNews   = remember { mutableStateListOf<Int>() }
    val newsDetails    = remember { mutableStateMapOf<Int, String>() }
    val loadingDetails = remember { mutableStateMapOf<Int, Boolean>() }

    LaunchedEffect(selectedCategory) {
        newsFeed.clear(); expandedNews.clear()
        manager.getProcessedFeed(selectedCategory).collect { newsFeed.add(0, it) }
    }

    MaterialTheme(colorScheme = LightColorScheme) {
        Scaffold(
            containerColor = BgApp,
            topBar = {
                TopAppBar(
                    title  = { NewsTopBar() },
                    actions = {
                        ReadCountBadge(readCount)
                        Spacer(Modifier.width(16.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { padding ->
            LazyColumn(
                modifier       = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(bottom = 40.dp)
            ) {
                item { HeroHeader() }

                // Pembatas Aman Anti-Error (Ganti HorizontalDivider)
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 22.dp, vertical = 8.dp)
                            .height(1.dp)
                            .background(Border)
                    )
                }

                item {
                    CategoryTabs(selected = selectedCategory, onSelect = { selectedCategory = it })
                    Spacer(Modifier.height(8.dp))
                }

                if (newsFeed.isEmpty()) {
                    item {
                        Box(Modifier.fillMaxWidth().height(260.dp), contentAlignment = Alignment.Center) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                CircularProgressIndicator(color = AccentBlue, strokeWidth = 2.dp, modifier = Modifier.size(28.dp))
                                Text("Menarik data terbaru…", fontSize = 13.sp, color = TextSoft)
                            }
                        }
                    }
                }

                items(newsFeed, key = { it.id }) { news ->
                    val isExpanded    = expandedNews.contains(news.id)
                    val isLoading     = loadingDetails[news.id] == true
                    val detailContent = newsDetails[news.id]

                    AnimatedVisibility(
                        visible = true,
                        enter   = fadeIn(tween(280)) + slideInVertically(
                            initialOffsetY = { -30 },
                            animationSpec  = tween(280, easing = EaseOutCubic)
                        )
                    ) {
                        LightNewsCard(
                            news          = news,
                            isExpanded    = isExpanded,
                            isLoading     = isLoading,
                            detailContent = detailContent,
                            modifier      = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            onClick       = {
                                if (isExpanded) {
                                    expandedNews.remove(news.id)
                                } else {
                                    manager.incrementReadCount()
                                    expandedNews.add(news.id)
                                    if (detailContent == null && !isLoading) {
                                        loadingDetails[news.id] = true
                                        scope.launch {
                                            newsDetails[news.id] = manager.fetchNewsDetailAsync(news.id)
                                            loadingDetails[news.id] = false
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// KOMPONEN UI TAMBAHAN
// ==========================================

@Composable
private fun NewsTopBar() {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Brush.linearGradient(listOf(AccentBlue, AccentTeal)))
                .border(1.dp, Border, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("IF", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
        }

        Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
            Text(
                text          = "ITERA News",
                fontSize      = 18.sp,
                fontWeight    = FontWeight.ExtraBold,
                letterSpacing = (-0.4).sp,
                color         = TextDark
            )
            Text(
                text          = "Institut Teknologi Sumatera",
                fontSize      = 9.sp,
                fontWeight    = FontWeight.Medium,
                color         = TextSoft
            )
        }
    }
}

@Composable
private fun ReadCountBadge(count: Int) {
    val transition = rememberInfiniteTransition(label = "dot")
    val dotAlpha by transition.animateFloat(
        initialValue  = 1f,
        targetValue   = 0.3f,
        animationSpec = infiniteRepeatable(tween(900, easing = EaseInOutSine), RepeatMode.Reverse),
        label         = "alpha"
    )

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceW)
            .border(1.dp, Border, RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(AccentBlue.copy(alpha = dotAlpha))
        )
        Text("Dibaca: $count", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextDark)
    }
}

@Composable
private fun HeroHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawCircle(
                    brush  = Brush.radialGradient(
                        colors = listOf(AccentBlue.copy(alpha = 0.08f), Color.Transparent),
                        center = Offset(size.width * 0.85f, 0f),
                        radius = size.width * 0.5f
                    ),
                    radius = size.width,
                    center = Offset(size.width * 0.85f, 0f)
                )
            }
            .padding(start = 22.dp, end = 22.dp, top = 8.dp, bottom = 12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text          = "News Feed",
                fontSize      = 38.sp,
                fontWeight    = FontWeight.Black,
                letterSpacing = (-1.5).sp,
                color         = TextDark,
                lineHeight    = 40.sp
            )
            Text(
                text          = "Simulator",
                fontSize      = 38.sp,
                fontWeight    = FontWeight.Black,
                letterSpacing = (-1.5).sp,
                color         = TextSoft.copy(alpha = 0.7f),
                lineHeight    = 40.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text          = "MUHAMMAD FAJRI FIRDAUS  ·  123140050",
                fontSize      = 10.sp,
                fontWeight    = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color         = AccentTeal
            )
        }
    }
}

@Composable
private fun CategoryTabs(selected: String, onSelect: (String) -> Unit) {
    LazyRow(
        contentPadding        = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Menggunakan entries.toTypedArray() agar kompatibel dengan berbagai versi Kotlin
        items(CategoryTab.entries.toTypedArray()) { tab ->
            val active = selected == tab.label
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (active) tab.color else SurfaceW)
                    .border(1.dp, if (active) tab.color else Border, RoundedCornerShape(20.dp))
                    .clickable(MutableInteractionSource(), null) { onSelect(tab.label) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(tab.emoji, fontSize = 14.sp)
                Text(
                    text       = tab.label,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color      = if (active) Color.White else TextMid
                )
            }
        }
    }
}

@Composable
fun LightNewsCard(
    news: DisplayNews,
    isExpanded: Boolean,
    isLoading: Boolean,
    detailContent: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vis = catVisuals[news.category] ?: CatVisual(
        news.category, "🗞️", AccentBlue, AccentBlueDim, AccentBlue, Color(0xFF38BDF8)
    )

    val accentBarGrad = Brush.horizontalGradient(listOf(vis.gradStart, vis.gradEnd))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(MutableInteractionSource(), null, onClick = onClick)
            .animateContentSize(spring(stiffness = Spring.StiffnessMediumLow)),
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = SurfaceW),
        border    = BorderStroke(
            1.dp,
            if (isExpanded) vis.color.copy(alpha = 0.4f) else Border
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isExpanded) 4.dp else 1.dp)
    ) {
        Column(Modifier.fillMaxWidth()) {

            // ── Gambar Cover Card (Diganti Box Emoji Anti-Error) ─────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(Brush.linearGradient(listOf(vis.gradStart, vis.gradEnd))),
                contentAlignment = Alignment.Center
            ) {
                // Ikon besar di tengah kotak
                Text(vis.emoji, fontSize = 56.sp)

                // Efek Gradient Fade ke Putih
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                0.0f to Color.Transparent,
                                0.5f to Color.Transparent,
                                1.0f to SurfaceW
                            )
                        )
                )

                // Kapsul Label Kategori
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.95f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .align(Alignment.TopStart),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(Modifier.size(6.dp).clip(CircleShape).background(vis.color))
                    Text(
                        text          = news.category.uppercase(),
                        fontSize      = 10.sp,
                        fontWeight    = FontWeight.Black,
                        letterSpacing = 0.8.sp,
                        color         = vis.color
                    )
                }

                // Teks Waktu
                Text(
                    text       = news.timeInfo,
                    fontSize   = 10.sp,
                    color      = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            // ── Body Konten Card ─────────────────────────
            Column(Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)) {

                Text(
                    text       = news.title,
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp,
                    color      = TextDark,
                    maxLines   = if (isExpanded) Int.MAX_VALUE else 2,
                    overflow   = TextOverflow.Ellipsis
                )

                val accentFrac by animateFloatAsState(
                    targetValue   = if (isExpanded) 1f else 0.3f,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label         = "bar"
                )
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(accentFrac)
                        .height(3.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(accentBarGrad)
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier              = Modifier.padding(bottom = 16.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text       = if (isExpanded) "Tutup" else "Baca selengkapnya",
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color      = vis.color
                    )
                    Box(
                        modifier         = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(vis.dimColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text     = if (isExpanded) "↑" else "→",
                            fontSize = 10.sp,
                            color    = vis.color,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            // ── Area Expanded Detail ──────────────────────
            AnimatedVisibility(
                visible = isExpanded,
                enter   = fadeIn() + expandVertically(),
                exit    = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(vis.dimColor.copy(alpha = 0.05f))
                ) {
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Border))

                    Box(modifier = Modifier.padding(16.dp)) {
                        when {
                            isLoading -> {
                                Row(
                                    verticalAlignment     = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier    = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color       = vis.color
                                    )
                                    Text(
                                        "Menyiapkan rincian berita...",
                                        fontSize  = 13.sp,
                                        color     = TextMid,
                                        fontStyle = FontStyle.Italic
                                    )
                                }
                            }
                            detailContent != null -> {
                                Text(
                                    text       = detailContent,
                                    fontSize   = 14.sp,
                                    lineHeight = 22.sp,
                                    color      = TextMid
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}