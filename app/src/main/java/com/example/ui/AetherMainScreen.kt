package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AIAgent
import com.example.data.AutomationRule
import com.example.data.CallHistory
import com.example.data.Contact
import com.example.ui.theme.MidnightBlue
import com.example.ui.theme.RichNavy
import com.example.ui.theme.ElectricSapphire
import com.example.ui.theme.SoftIceWhite
import com.example.ui.theme.SilverFrost
import com.example.ui.theme.MutedBlueGlow
import com.example.ui.theme.DarkGreyNavy
import com.example.ui.theme.SapphireGlow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AetherAppContent(viewModel: AetherViewModel = viewModel()) {
    val navState by viewModel.navigationState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MidnightBlue
    ) {
        AnimatedContent(
            targetState = navState,
            transitionSpec = {
                (slideInHorizontally { width -> width } + fadeIn() togetherWith
                        slideOutHorizontally { width -> -width } + fadeOut())
                    .using(SizeTransform(clip = false))
            },
            label = "ScreenTransition"
        ) { state ->
            when (state) {
                AppNavigationState.SPLASH -> SplashScreen(viewModel)
                AppNavigationState.ONBOARDING -> OnboardingScreen(viewModel)
                AppNavigationState.LOGIN -> LoginScreen(viewModel)
                AppNavigationState.MAIN -> MainDashboardContainer(viewModel)
            }
        }
    }
}

// ---------------- SPLASH SCREEN ----------------
@Composable
fun SplashScreen(viewModel: AetherViewModel) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alphaScale by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                // Background ambient soft radials
                drawCircle(
                    color = ElectricSapphire.copy(alpha = 0.15f),
                    radius = 400.dp.toPx(),
                    center = Offset(size.width / 2, size.height / 3)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .drawBehind {
                        drawCircle(
                            color = ElectricSapphire.copy(alpha = alphaScale * 0.3f),
                            radius = (45f + (alphaScale * 15f)).dp.toPx(),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.RecordVoiceOver,
                    contentDescription = "Aether Voice Logo",
                    tint = ElectricSapphire,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "ÆTHER CALL",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 6.sp,
                    color = SoftIceWhite
                )
            )

            Text(
                text = "Luxury Intelligence Operator Engine",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = SilverFrost.copy(alpha = 0.8f),
                    letterSpacing = 1.5.sp
                ),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = { viewModel.setNavigationState(AppNavigationState.ONBOARDING) },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricSapphire),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(54.dp)
                    .testTag("enter_button"),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "COMMISSION SUITE",
                        style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Proceed",
                        tint = Color.White
                    )
                }
            }
        }

        Text(
            text = "VERSION 4.2.1 // ENCRYPTED TRUNK LINK",
            style = MaterialTheme.typography.labelSmall.copy(
                color = SilverFrost.copy(alpha = 0.4f),
                letterSpacing = 1.sp
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 24.dp)
        )
    }
}

// ---------------- ONBOARDING SCREEN ----------------
@Composable
fun OnboardingScreen(viewModel: AetherViewModel) {
    var currentPage by remember { mutableStateOf(0) }
    val headers = listOf(
        "Autonomous Dialing Node",
        "Conconcierge Intelligence",
        "Root Trunk Encryption"
    )
    val descriptions = listOf(
        "Empower your workspace with voice-cloned sovereign AI entities operating with zero manual intervention.",
        "On-device neural parameters score and qualify high-intent pipelines instantly.",
        "Leverage low-level SIP integrations and rooted kernel parameters to pipe call channels securely."
    )
    val icons = listOf(
        Icons.Rounded.PhoneInTalk,
        Icons.Rounded.GraphicEq,
        Icons.Rounded.Security
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawCircle(
                    color = ElectricSapphire.copy(alpha = 0.12f),
                    radius = 350.dp.toPx(),
                    center = Offset(if (currentPage == 0) 50f else if (currentPage == 1) size.width / 2 else size.width - 50f, size.height * 0.7f)
                )
            }
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // Header Skip button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { viewModel.setNavigationState(AppNavigationState.LOGIN) },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text(
                        text = "SKIP TO TERMINAL",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SilverFrost,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }

            // Interactive slideshow illustration
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .background(RichNavy, CircleShape)
                        .border(1.dp, MutedBlueGlow.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icons[currentPage],
                        contentDescription = headers[currentPage],
                        tint = ElectricSapphire,
                        modifier = Modifier.size(64.dp)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = headers[currentPage],
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = descriptions[currentPage],
                    style = MaterialTheme.typography.bodyMedium.copy(color = SilverFrost),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Page indicator dots
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(if (index == currentPage) 12.dp else 8.dp)
                                .background(
                                    color = if (index == currentPage) ElectricSapphire else RichNavy,
                                    shape = CircleShape
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (index == currentPage) ElectricSapphire else MutedBlueGlow.copy(
                                        alpha = 0.5f
                                    ),
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }

            // Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentPage > 0) {
                    OutlinedButton(
                        onClick = { currentPage-- },
                        border = BorderStroke(1.dp, MutedBlueGlow.copy(alpha = 0.6f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SilverFrost),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(
                            text = "BACK",
                            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 1.sp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(80.dp))
                }

                Button(
                    onClick = {
                        if (currentPage < 2) {
                            currentPage++
                        } else {
                            viewModel.setNavigationState(AppNavigationState.LOGIN)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricSapphire),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text(
                        text = if (currentPage == 2) "ENTER TERMINAL" else "NEXT NODE",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }
        }
    }
}

// ---------------- LOGIN SCREEN ----------------
@Composable
fun LoginScreen(viewModel: AetherViewModel) {
    var email by remember { mutableStateOf("jini96x@gmail.com") }
    var keyPhrase by remember { mutableStateOf("••••••••") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawCircle(
                    color = ElectricSapphire.copy(alpha = 0.14f),
                    radius = 450.dp.toPx(),
                    center = Offset(size.width * 0.8f, size.height * 0.2f)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Security,
                contentDescription = "Shield Guard",
                tint = ElectricSapphire,
                modifier = Modifier
                    .size(64.dp)
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "ACCESS CONTROL",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 3.sp,
                    color = Color.White
                )
            )

            Text(
                text = "Authorize Aether Operator Credentials",
                style = MaterialTheme.typography.bodyMedium.copy(color = SilverFrost),
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
            )

            // Glassmorphism login card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        Brush.linearGradient(
                            listOf(MutedBlueGlow.copy(alpha = 0.4f), Color.Transparent)
                        ),
                        RoundedCornerShape(24.dp)
                    ),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = RichNavy.copy(alpha = 0.85f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("OPERATOR ID / EMAIL", color = SilverFrost) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricSapphire,
                            unfocusedBorderColor = MutedBlueGlow.copy(alpha = 0.4f),
                            focusedLabelColor = ElectricSapphire,
                            unfocusedLabelColor = SilverFrost,
                            focusedTextColor = SoftIceWhite,
                            unfocusedTextColor = SoftIceWhite
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = keyPhrase,
                        onValueChange = { keyPhrase = it },
                        label = { Text("PASSKEY / CRIPT TOKEN", color = SilverFrost) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricSapphire,
                            unfocusedBorderColor = MutedBlueGlow.copy(alpha = 0.4f),
                            focusedLabelColor = ElectricSapphire,
                            unfocusedLabelColor = SilverFrost,
                            focusedTextColor = SoftIceWhite,
                            unfocusedTextColor = SoftIceWhite
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = { viewModel.setNavigationState(AppNavigationState.MAIN) },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricSapphire),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("login_button")
                    ) {
                        Text(
                            text = "AUTHORIZE LINK",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Color.White,
                                letterSpacing = 1.5.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = { viewModel.setNavigationState(AppNavigationState.MAIN) },
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Text(
                    text = "BYPASS / DEMO MODE",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MutedBlueGlow,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

// ---------------- MAIN APP WRAPPER CONTAINER ----------------
@Composable
fun MainDashboardContainer(viewModel: AetherViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val isRooted by viewModel.isDeviceRooted.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MidnightBlue,
        bottomBar = {
            // Luxurious bottom navigation dock with glowing line indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MidnightBlue)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                        .border(
                            1.dp,
                            MutedBlueGlow.copy(alpha = 0.25f),
                            RoundedCornerShape(24.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = RichNavy.copy(alpha = 0.92f)),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TabNavItem(
                            imageVector = Icons.Default.AutoGraph,
                            label = "Core",
                            selected = currentTab == MainTab.DASHBOARD,
                            onClick = { viewModel.selectTab(MainTab.DASHBOARD) }
                        )
                        TabNavItem(
                            imageVector = Icons.Default.RecordVoiceOver,
                            label = "Agents",
                            selected = currentTab == MainTab.AGENTS,
                            onClick = { viewModel.selectTab(MainTab.AGENTS) }
                        )
                        TabNavItem(
                            imageVector = Icons.Default.Campaign,
                            label = "Campaigns",
                            selected = currentTab == MainTab.CAMPAIGNS,
                            onClick = { viewModel.selectTab(MainTab.CAMPAIGNS) }
                        )
                        TabNavItem(
                            imageVector = Icons.Default.PhoneInTalk,
                            label = "Dispatch",
                            selected = currentTab == MainTab.LIVE_CALL,
                            onClick = { viewModel.selectTab(MainTab.LIVE_CALL) }
                        )
                        TabNavItem(
                            imageVector = Icons.Default.Terminal,
                            label = "Rules",
                            selected = currentTab == MainTab.AUTOMATION,
                            onClick = { viewModel.selectTab(MainTab.AUTOMATION) }
                        )
                        TabNavItem(
                            imageVector = if (isRooted) Icons.Default.Security else Icons.Default.Settings,
                            label = if (isRooted) "Root" else "System",
                            selected = currentTab == MainTab.ACCOUNT,
                            onClick = { viewModel.selectTab(MainTab.ACCOUNT) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
        ) {
            // Elegant brand header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(ElectricSapphire, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "ÆTHER CORE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = 2.sp
                        )
                    )
                }

                // Premium status pills
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(DarkGreyNavy, RoundedCornerShape(12.dp))
                            .border(1.dp, MutedBlueGlow.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (isRooted) "KERNEL LINK ACTIVE-R" else "USERLAND BYPASS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ElectricSapphire,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                }
            }

            Divider(color = MutedBlueGlow.copy(alpha = 0.15f), thickness = 1.dp)

            // Dynamic view selector
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (currentTab) {
                    MainTab.DASHBOARD -> DashboardTab(viewModel)
                    MainTab.AGENTS -> AgentTab(viewModel)
                    MainTab.CAMPAIGNS -> CampaignTab(viewModel)
                    MainTab.LIVE_CALL -> DispatchTab(viewModel)
                    MainTab.AUTOMATION -> AutomationTab(viewModel)
                    MainTab.ACCOUNT -> AccountTab(viewModel)
                }
            }
        }
    }
}

@Composable
fun RowScope.TabNavItem(
    imageVector: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val alpha by animateFloatAsState(if (selected) 1.0f else 0.5f, label = "alpha")
    val scale by animateFloatAsState(if (selected) 1.1f else 1.0f, label = "scale")

    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = label,
            tint = if (selected) ElectricSapphire else SilverFrost,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) SoftIceWhite else SilverFrost,
            letterSpacing = 0.5.sp
        )
    }
}

// ---------------- DASHBOARD TAB ----------------
@Composable
fun DashboardTab(viewModel: AetherViewModel) {
    val callHistory by viewModel.callHistory.collectAsState(initial = emptyList())
    val agentsState by viewModel.aiAgents.collectAsState(initial = emptyList())
    val campaignList by viewModel.campaigns.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        // Welcome Header
        item {
            Column {
                Text(
                    text = "Aether Executive Status",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                )
                Text(
                    text = "Orchestrating intelligent interactions with continuous SIP links.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = SilverFrost)
                )
            }
        }

        // Metrics Grid (Beautiful Floating Cards)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    title = "Today's Calls",
                    value = "${callHistory.size * 2 + 14}",
                    subtitle = "100% autonomous",
                    icon = Icons.Default.Phone
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    title = "Active Agents",
                    value = "${agentsState.size}",
                    subtitle = "All cores healthy",
                    icon = Icons.Default.RecordVoiceOver
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    title = "Conversations",
                    value = "${campaignList.sumOf { it.totalContacts }}",
                    subtitle = "Synced pipeline",
                    icon = Icons.Default.Contacts
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    title = "Success Rate",
                    value = "88.5%",
                    subtitle = "Premium threshold",
                    icon = Icons.Default.TrendingUp
                )
            }
        }

        // Animated Success Trend Line Graphic (Draw in Custom Canvas)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        MutedBlueGlow.copy(alpha = 0.2f),
                        RoundedCornerShape(24.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = RichNavy),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "SOVEREIGN AGENT CONVERSION TREND",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SilverFrost,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                    ) {
                        val points = listOf(20f, 40f, 35f, 75f, 60f, 95f, 85f, 110f)
                        val widthGap = size.width / (points.size - 1)
                        val path = Path()

                        points.forEachIndexed { i, p ->
                            // Map values to Canvas coordinates
                            val x = i * widthGap
                            val y = size.height - (p / 120f) * size.height
                            if (i == 0) {
                                path.moveTo(x, y)
                            } else {
                                path.lineTo(x, y)
                            }
                        }

                        drawPath(
                            path = path,
                            color = ElectricSapphire,
                            style = Stroke(width = 3.dp.toPx())
                        )

                        // Draw glowing gradient underneath
                        val fillPath = Path().apply {
                            addPath(path)
                            lineTo(size.width, size.height)
                            lineTo(0f, size.height)
                            close()
                        }
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(ElectricSapphire.copy(alpha = 0.3f), Color.Transparent)
                            )
                        )

                        // Highlight final bullet points
                        drawCircle(
                            color = ElectricSapphire,
                            radius = 6.dp.toPx(),
                            center = Offset(size.width, size.height - (points[points.size - 1] / 120f) * size.height)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Autonomous pipelines registered 88.5% conversion rating over past 7 cycles.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = SilverFrost, fontSize = 12.sp)
                    )
                }
            }
        }

        // Recent Transaction Activity Logs
        item {
            Text(
                text = "RECENT CONCIERGE TRANSACTIONS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = SilverFrost,
                    letterSpacing = 1.sp
                )
            )
        }

        if (callHistory.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No records compiled yet. Tap Dispatch to dial client.", color = SilverFrost)
                }
            }
        } else {
            items(callHistory) { call ->
                CallHistoryCard(call)
            }
        }
    }
}

@Composable
fun MetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = modifier
            .border(
                1.dp,
                MutedBlueGlow.copy(alpha = 0.2f),
                RoundedCornerShape(20.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = RichNavy),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = SilverFrost,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ElectricSapphire,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = SilverFrost.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
            )
        }
    }
}

@Composable
fun CallHistoryCard(call: CallHistory) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MutedBlueGlow.copy(alpha = 0.15f),
                RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = DarkGreyNavy),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (call.direction == "Inbound") Icons.Default.CallReceived else Icons.Default.CallMade,
                        contentDescription = call.direction,
                        tint = ElectricSapphire,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = call.contactName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .background(ElectricSapphire.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = call.sentiment,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MutedBlueGlow,
                            fontSize = 9.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = call.transcript,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = SilverFrost,
                    fontSize = 13.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = call.phoneNumber,
                    style = MaterialTheme.typography.labelSmall.copy(color = SilverFrost.copy(alpha = 0.6f))
                )
                Text(
                    text = "${call.durationSeconds} seconds",
                    style = MaterialTheme.typography.labelSmall.copy(color = SilverFrost.copy(alpha = 0.6f))
                )
            }
        }
    }
}

// ---------------- VOICE STUDIO / AGENT TAB ----------------
@Composable
fun AgentTab(viewModel: AetherViewModel) {
    val agents by viewModel.aiAgents.collectAsState(initial = emptyList())
    val selected by viewModel.selectedAgent.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newSystemPrompt by remember { mutableStateOf("") }
    var selectedVoice by remember { mutableStateOf("Aether Male (Pro)") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "AI Agent Studio",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "Customize sovereign representative nodes.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = SilverFrost)
                    )
                }

                Button(
                    onClick = { showCreateDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricSapphire),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "New Agent", tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Incept", color = Color.White)
                }
            }
        }

        // Active selection Waveform selector
        selected?.let { agent ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            2.dp,
                            ElectricSapphire.copy(alpha = 0.4f),
                            RoundedCornerShape(24.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = RichNavy),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(ElectricSapphire.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RecordVoiceOver,
                                    contentDescription = null,
                                    tint = ElectricSapphire
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = agent.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "Active Voice Preset: ${agent.voiceType}",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = MutedBlueGlow)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Voice waveform simulation drawing
                        Text(
                            text = "VOICE CONTOUR VISUALIZER [100% QUALITY]",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SilverFrost,
                                letterSpacing = 1.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val bars = listOf(12, 28, 24, 40, 16, 20, 36, 12, 14, 28, 38, 22, 12, 32, 10, 8, 22, 38, 14, 28, 40, 12, 18, 32, 12)
                            bars.forEach { heightVal ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(heightVal.dp)
                                        .background(ElectricSapphire, RoundedCornerShape(2.dp))
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Sliders for dynamic response attributes
                        Text(
                            text = "Speech Pitch Modulation",
                            style = MaterialTheme.typography.labelSmall.copy(color = SilverFrost)
                        )
                        var tempPitch by remember { mutableStateOf(agent.pitch) }
                        Slider(
                            value = tempPitch,
                            onValueChange = { tempPitch = it },
                            valueRange = 0.5f..1.5f,
                            colors = SliderDefaults.colors(
                                thumbColor = ElectricSapphire,
                                activeTrackColor = ElectricSapphire,
                                inactiveTrackColor = MidnightBlue
                            )
                        )

                        Text(
                            text = "Orator Pacing Speed",
                            style = MaterialTheme.typography.labelSmall.copy(color = SilverFrost)
                        )
                        var tempSpeed by remember { mutableStateOf(agent.speed) }
                        Slider(
                            value = tempSpeed,
                            onValueChange = { tempSpeed = it },
                            valueRange = 0.5f..1.5f,
                            colors = SliderDefaults.colors(
                                thumbColor = ElectricSapphire,
                                activeTrackColor = ElectricSapphire,
                                inactiveTrackColor = MidnightBlue
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Core Sovereign Constraints Model Prompt",
                            style = MaterialTheme.typography.labelSmall.copy(color = SilverFrost)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(DarkGreyNavy, RoundedCornerShape(12.dp))
                                .border(1.dp, MutedBlueGlow.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = agent.systemPrompt,
                                style = MaterialTheme.typography.bodyMedium.copy(color = SoftIceWhite, fontSize = 13.sp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "COMMISSIONED SOVEREIGN AGENTS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = SilverFrost,
                    letterSpacing = 1.sp
                )
            )
        }

        // All agents listed
        items(agents) { agent ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.setSelectedAgent(agent) }
                    .border(
                        1.dp,
                        if (selected?.id == agent.id) ElectricSapphire else MutedBlueGlow.copy(alpha = 0.15f),
                        RoundedCornerShape(16.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = DarkGreyNavy),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(ElectricSapphire.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = ElectricSapphire,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = agent.name,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "PSTN Voice: ${agent.voiceType}",
                                style = MaterialTheme.typography.labelSmall.copy(color = SilverFrost)
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Edit Parameters",
                        tint = SilverFrost,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

    // Modal Builder Dialog
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Incept New Voice Rep", color = Color.White) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Agent Name") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    OutlinedTextField(
                        value = newSystemPrompt,
                        onValueChange = { newSystemPrompt = it },
                        label = { Text("Core System Constraint Script") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    // Select voice dropdown simulation
                    Text("Select Voice Engine Preset", color = SilverFrost)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Aether Male (Pro)", "Nova Female (Satin)", "Echo Warm (Natural)").forEach { voice ->
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (selectedVoice == voice) ElectricSapphire else DarkGreyNavy,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedVoice = voice }
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = voice.substringBefore(" "),
                                    color = Color.White,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newName.isNotBlank() && newSystemPrompt.isNotBlank()) {
                            viewModel.addAgent(newName, selectedVoice, newSystemPrompt)
                            newName = ""
                            newSystemPrompt = ""
                            showCreateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricSapphire)
                ) {
                    Text("Commission Node", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancel", color = SilverFrost)
                }
            },
            containerColor = RichNavy,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

// ---------------- CAMPAIGN MANAGER TAB ----------------
@Composable
fun CampaignTab(viewModel: AetherViewModel) {
    val campaigns by viewModel.campaigns.collectAsState(initial = emptyList())
    val contacts by viewModel.contacts.collectAsState(initial = emptyList())

    var showCampaignModal by remember { mutableStateOf(false) }
    var campName by remember { mutableStateOf("") }
    var targetVol by remember { mutableStateOf("100") }

    var showContactModal by remember { mutableStateOf(false) }
    var cName by remember { mutableStateOf("") }
    var cPhone by remember { mutableStateOf("") }
    var cTags by remember { mutableStateOf("VIP Lead, SaaS") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Automated Campaigns",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "Track active outbound voice dialing flows.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = SilverFrost)
                    )
                }

                Button(
                    onClick = { showCampaignModal = true },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricSapphire),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.PlusOne, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Rollout", color = Color.White)
                }
            }
        }

        // List Campaigns
        items(campaigns) { campaign ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        MutedBlueGlow.copy(alpha = 0.15f),
                        RoundedCornerShape(20.dp)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = RichNavy)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = campaign.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Box(
                            modifier = Modifier
                                .background(
                                    if (campaign.status == "Active") ElectricSapphire.copy(alpha = 0.2f) else DarkGreyNavy,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = campaign.status.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (campaign.status == "Active") ElectricSapphire else SilverFrost,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress indicators
                    Text(
                        text = "Dialing Completion Rate: ${campaign.completedContacts} / ${campaign.totalContacts}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = SilverFrost, fontSize = 12.sp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    val progressRatio = if (campaign.totalContacts > 0) {
                        campaign.completedContacts.toFloat() / campaign.totalContacts.toFloat()
                    } else 0f

                    LinearProgressIndicator(
                        progress = progressRatio,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = ElectricSapphire,
                        trackColor = MidnightBlue
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("SOVEREIGN CONVERSIONS", style = MaterialTheme.typography.labelSmall.copy(color = SilverFrost, fontSize = 9.sp))
                            Text("${campaign.successRate}% Success", style = MaterialTheme.typography.titleSmall.copy(color = Color.White, fontWeight = FontWeight.Bold))
                        }

                        Column {
                            Text("TRUNK ROUTED_BY", style = MaterialTheme.typography.labelSmall.copy(color = SilverFrost, fontSize = 9.sp))
                            Text("Aether Core Engine", style = MaterialTheme.typography.titleSmall.copy(color = MutedBlueGlow, fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }

        // Contact Directory Module Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Operator Contacts Database",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )

                TextButton(
                    onClick = { showContactModal = true },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text("+ Add VIP Lead", color = MutedBlueGlow, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Compact list of contacts in current CRM cache
        items(contacts) { contact ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        MutedBlueGlow.copy(alpha = 0.1f),
                        RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkGreyNavy)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = contact.name,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Phone: ${contact.phone} • Tag: ${contact.tags}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = SilverFrost, fontSize = 12.sp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(ElectricSapphire.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = contact.status,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ElectricSapphire,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }

    // Modal Create Campaign
    if (showCampaignModal) {
        AlertDialog(
            onDismissRequest = { showCampaignModal = false },
            title = { Text("Launch Dialing Blast", color = Color.White) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = campName,
                        onValueChange = { campName = it },
                        label = { Text("Campaign Identity ID") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                    OutlinedTextField(
                        value = targetVol,
                        onValueChange = { targetVol = it },
                        label = { Text("Target Batch Volume") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (campName.isNotBlank()) {
                            viewModel.addCampaign(
                                campName,
                                1,
                                targetVol.toIntOrNull() ?: 100
                            )
                            campName = ""
                            showCampaignModal = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricSapphire)
                ) {
                    Text("Provision Campaign", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCampaignModal = false }) {
                    Text("Close", color = SilverFrost)
                }
            },
            containerColor = RichNavy,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Modal Contact Entry
    if (showContactModal) {
        AlertDialog(
            onDismissRequest = { showContactModal = false },
            title = { Text("Add Elite Lead to Vault", color = Color.White) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = cName,
                        onValueChange = { cName = it },
                        label = { Text("Client Full Name") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                    OutlinedTextField(
                        value = cPhone,
                        onValueChange = { cPhone = it },
                        label = { Text("Phone Address (E.164)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                    OutlinedTextField(
                        value = cTags,
                        onValueChange = { cTags = it },
                        label = { Text("Qualification Tags") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (cName.isNotBlank() && cPhone.isNotBlank()) {
                            viewModel.addContact(cName, cPhone, "Lead", cTags)
                            cName = ""
                            cPhone = ""
                            showContactModal = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricSapphire)
                ) {
                    Text("Commit Lead", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showContactModal = false }) {
                    Text("Cancel", color = SilverFrost)
                }
            },
            containerColor = RichNavy,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

// ---------------- LIVE DISPATCH / DIALER TAB ----------------
@Composable
fun DispatchTab(viewModel: AetherViewModel) {
    val contacts by viewModel.contacts.collectAsState(initial = emptyList())
    val callState by viewModel.liveCallState.collectAsState()
    val activeName by viewModel.liveCallName.collectAsState()
    val activePhone by viewModel.liveCallPhone.collectAsState()
    val transcript by viewModel.liveCallTranscript.collectAsState()
    val isSpeaking by viewModel.isAgentSpeaking.collectAsState()

    // Animation to scale speaking wave indicator
    val speechPulse = rememberInfiniteTransition(label = "pulseWave")
    val pulseSize by speechPulse.animateFloat(
        initialValue = 110f,
        targetValue = 145f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        if (callState == CallState.IDLE) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
                modifier = Modifier.weight(1f)
            ) {
                item {
                    Column {
                        Text(
                            text = "Live Operator Dispatch",
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Locate an existing client record below and trigger a simulated live AI outbound dialogue immediately.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = SilverFrost)
                        )
                    }
                }

                item {
                    Text(
                        text = "AVAILABLE TARGET DESTINATIONS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SilverFrost,
                            letterSpacing = 1.sp
                        )
                    )
                }

                items(contacts) { contact ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                MutedBlueGlow.copy(alpha = 0.2f),
                                RoundedCornerShape(20.dp)
                            ),
                        colors = CardDefaults.cardColors(containerColor = RichNavy),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = contact.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = contact.phone,
                                    style = MaterialTheme.typography.bodyMedium.copy(color = SilverFrost)
                                )
                            }

                            Button(
                                onClick = { viewModel.initiateSimulatedCall(contact.name, contact.phone) },
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricSapphire),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("dial_${contact.name.replace(" ", "_")}")
                            ) {
                                Icon(imageVector = Icons.Default.PhoneInTalk, contentDescription = "Dial", tint = Color.White)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Dial AI", color = Color.White)
                            }
                        }
                    }
                }
            }
        } else {
            // Live Call Active Screen! (HUD styling matching OpenAI Advanced Voice / Futuristic HUD)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            ElectricSapphire.copy(alpha = 0.5f),
                            RoundedCornerShape(24.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = RichNavy),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "SOVEREIGN CALL LINK STATUS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SilverFrost,
                                letterSpacing = 2.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = activeName,
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        )

                        Text(
                            text = activePhone,
                            style = MaterialTheme.typography.bodyMedium.copy(color = SilverFrost)
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        // Pulsing glowing orb when active
                        Box(
                            modifier = Modifier
                                .size(160.dp)
                                .drawBehind {
                                    val finalPulse = if (isSpeaking) pulseSize.dp.toPx() else 110.dp.toPx()
                                    drawCircle(
                                        color = ElectricSapphire.copy(alpha = 0.2f),
                                        radius = finalPulse
                                    )
                                    drawCircle(
                                        color = ElectricSapphire.copy(alpha = 0.5f),
                                        radius = 80.dp.toPx()
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.VolumeUp else Icons.Default.Mic,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = if (callState == CallState.CONNECTING) "HANDSHAKING DUPLEX STREAM..." else "SECURE PSTN CHANNEL OPEN // DIALOGUE COMMENCED",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (callState == CallState.CONNECTING) SilverFrost else ElectricSapphire,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                // Scrolling Live transcript characters HUD
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .border(1.dp, MutedBlueGlow.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkGreyNavy),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            Text(
                                text = "TRUNK REAL-TIME TRANSCRIPT // SECURE DISPATCH LOG",
                                style = MaterialTheme.typography.labelSmall.copy(color = MutedBlueGlow, letterSpacing = 0.5.sp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (transcript.isEmpty()) "Awaiting carrier stream connection..." else transcript,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = SoftIceWhite,
                                    lineHeight = 22.sp
                                )
                            )
                        }
                    }
                }

                // Hang Up trigger
                Button(
                    onClick = { viewModel.hangUpCall() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f)),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(64.dp)
                        .testTag("hangup_button")
                ) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = "Hang Up", tint = Color.White, modifier = Modifier.size(28.dp))
                }
            }
        }
    }
}

// ---------------- AUTOMATION & INTEGRATION TAB ----------------
@Composable
fun AutomationTab(viewModel: AetherViewModel) {
    val rules by viewModel.automationRules.collectAsState(initial = emptyList())
    val hsActive by viewModel.hubspotActive.collectAsState()
    val sfActive by viewModel.salesforceActive.collectAsState()
    val zpActive by viewModel.zapierActive.collectAsState()

    var showRuleModal by remember { mutableStateOf(false) }
    var trigText by remember { mutableStateOf("") }
    var actText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Core Automation Rules",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "Orchestrate automated triggers without human latency.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = SilverFrost)
                    )
                }

                Button(
                    onClick = { showRuleModal = true },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricSapphire),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Rule", color = Color.White)
                }
            }
        }

        // List Rules
        items(rules) { rule ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        MutedBlueGlow.copy(alpha = 0.15f),
                        RoundedCornerShape(16.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = RichNavy),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(ElectricSapphire.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = ElectricSapphire)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "TRIGGER: ${rule.triggerEvent}",
                            style = MaterialTheme.typography.labelSmall.copy(color = MutedBlueGlow, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "ACTION: ${rule.actionToTake}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontWeight = FontWeight.SemiBold)
                        )
                    }

                    Switch(
                        checked = rule.isEnabled,
                        onCheckedChange = { /* Simulated toggle on persist */ },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ElectricSapphire,
                            checkedTrackColor = ElectricSapphire.copy(alpha = 0.4f),
                            uncheckedThumbColor = SilverFrost,
                            uncheckedTrackColor = DarkGreyNavy
                        )
                    )
                }
            }
        }

        // CRM Integration Showcase
        item {
            Text(
                text = "ENTERPRISE SYNC INTEGRATIONS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = SilverFrost,
                    letterSpacing = 1.sp
                )
            )
        }

        // HubSpot Sync
        item {
            IntegrationCard(
                title = "HubSpot Core CRM Sync",
                description = "Automatically map qualified Aether leads directly to pipeline deals.",
                isActive = hsActive,
                onToggle = { viewModel.toggleHubspot() }
            )
        }

        // Salesforce Sync
        item {
            IntegrationCard(
                title = "Salesforce Enterprise Cloud",
                description = "Pipe full-dialogue SIP voice tape transcripts directly into contact opportunities.",
                isActive = sfActive,
                onToggle = { viewModel.toggleSalesforce() }
            )
        }

        // Zapier Trigger webhook
        item {
            IntegrationCard(
                title = "Zapier Automation Engine",
                description = "Transmit active call logs to 5,000+ client hooks immediately.",
                isActive = zpActive,
                onToggle = { viewModel.toggleZapier() }
            )
        }
    }

    if (showRuleModal) {
        AlertDialog(
            onDismissRequest = { showRuleModal = false },
            title = { Text("Assemble New Automation Trigger", color = Color.White) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = trigText,
                        onValueChange = { trigText = it },
                        label = { Text("Triggering Event (e.g. On High-Intent Lead)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                    OutlinedTextField(
                        value = actText,
                        onValueChange = { actText = it },
                        label = { Text("Action Sequence to Dispatch") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (trigText.isNotBlank() && actText.isNotBlank()) {
                            viewModel.addAutomationRule(trigText, actText)
                            trigText = ""
                            actText = ""
                            showRuleModal = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricSapphire)
                ) {
                    Text("Provision Rule", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRuleModal = false }) {
                    Text("Cancel", color = SilverFrost)
                }
            },
            containerColor = RichNavy,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun IntegrationCard(
    title: String,
    description: String,
    isActive: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isActive) ElectricSapphire.copy(alpha = 0.5f) else MutedBlueGlow.copy(alpha = 0.15f),
                RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = DarkGreyNavy),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(color = SilverFrost, fontSize = 12.sp)
                )
            }

            Switch(
                checked = isActive,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ElectricSapphire,
                    checkedTrackColor = ElectricSapphire.copy(alpha = 0.4f),
                    uncheckedThumbColor = SilverFrost,
                    uncheckedTrackColor = MidnightBlue
                )
            )
        }
    }
}

// ---------------- ROOT privilegES, DIAGNOSTICS & ACCOUNT TAB ----------------
@Composable
fun AccountTab(viewModel: AetherViewModel) {
    val isRooted by viewModel.isDeviceRooted.collectAsState()
    val lowLevelInject by viewModel.lowLevelAudioInjection.collectAsState()
    val rawBusTap by viewModel.telephonyBusTap.collectAsState()
    val terminalLines by viewModel.terminalLogs.collectAsState()
    val currentBillingTier by viewModel.saasTier.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Aether Systems & Diagnostics",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                )
                Text(
                    text = "Low-level system controls and sovereign license details.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = SilverFrost)
                )
            }
        }

        // Active billing tier select
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        ElectricSapphire.copy(alpha = 0.5f),
                        RoundedCornerShape(20.dp)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = RichNavy)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "COMMISSIONED LICENSE SUBSCRIPTION STATUS",
                        style = MaterialTheme.typography.labelSmall.copy(color = SilverFrost, letterSpacing = 1.sp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentBillingTier,
                        style = MaterialTheme.typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Continuous sovereign PSTN channel dialing, high-fidelity vocoder outputs enabled.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = SilverFrost, fontSize = 12.sp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { viewModel.setSaasTier("Sapphire Sovereign Enterprise") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentBillingTier.contains("Sovereign")) ElectricSapphire else DarkGreyNavy
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Sovereign Tier", color = Color.White, fontSize = 11.sp)
                        }

                        Button(
                            onClick = { viewModel.setSaasTier("Standard Pro License") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentBillingTier.contains("Standard")) ElectricSapphire else DarkGreyNavy
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Standard Pro", color = Color.White, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Root status details (Administrative capability diagnostics)
        item {
            Text(
                text = "LOW-LEVEL OPERATIVE CHASSIS (ROOT FEATURES)",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = SilverFrost,
                    letterSpacing = 1.sp
                )
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (isRooted) ElectricSapphire.copy(alpha = 0.5f) else MutedBlueGlow.copy(alpha = 0.15f),
                        RoundedCornerShape(16.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = RichNavy),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Root Privilege State",
                            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isRooted) ElectricSapphire.copy(alpha = 0.2f) else DarkGreyNavy,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isRooted) "ROOTED" else "UNROOTED / USERLAND",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isRooted) ElectricSapphire else SilverFrost,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isRooted) "Direct kernel SIP taps and audio stream overrides are authorized." else "Root access was not found. System uses pure userland Android API routing layers natively.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = SilverFrost, fontSize = 12.sp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Low-Level Mixer Direct Injection",
                        style = MaterialTheme.typography.labelSmall.copy(color = SilverFrost)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Intercept voice channels in hardware mixer directly.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = SilverFrost, fontSize = 12.sp),
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = lowLevelInject,
                            onCheckedChange = { viewModel.toggleLowLevelAudio() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = ElectricSapphire,
                                checkedTrackColor = ElectricSapphire.copy(alpha = 0.4f),
                                uncheckedThumbColor = SilverFrost,
                                uncheckedTrackColor = MidnightBlue
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Telephony Baseband capture wire",
                        style = MaterialTheme.typography.labelSmall.copy(color = SilverFrost)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Simulate PSTN baseband bypass directly in cellular bus.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = SilverFrost, fontSize = 12.sp),
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = rawBusTap,
                            onCheckedChange = { viewModel.toggleTelephonyBusTap() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = ElectricSapphire,
                                checkedTrackColor = ElectricSapphire.copy(alpha = 0.4f),
                                uncheckedThumbColor = SilverFrost,
                                uncheckedTrackColor = MidnightBlue
                            )
                        )
                    }
                }
            }
        }

        // Root terminal live diagnostics logger
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(1.dp, MutedBlueGlow.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkGreyNavy),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "AETHER PRIVILEGED CLIENT TERMINAL",
                        style = MaterialTheme.typography.labelSmall.copy(color = MutedBlueGlow)
                    )
                    Divider(color = MutedBlueGlow.copy(alpha = 0.15f), modifier = Modifier.padding(vertical = 8.dp))
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            terminalLines.forEach { logLine ->
                                Text(
                                    text = logLine,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = SoftIceWhite,
                                        fontSize = 11.sp,
                                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Administrative tool triggers
        item {
            Button(
                onClick = { viewModel.clearAllLogs() },
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreyNavy),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Truncate Outbound History DB Logs", color = Color.White)
            }
        }
    }
}
