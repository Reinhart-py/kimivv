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
import com.example.ui.theme.KimiCream
import com.example.ui.theme.KimiBurgundy
import com.example.ui.theme.KimiPeach
import com.example.ui.theme.KimiDarkPeach
import com.example.ui.theme.KimiWhite
import com.example.ui.theme.KimiCharcoal
import com.example.ui.theme.KimiWarmGray
import com.example.ui.theme.KimiLightRose

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun KimiAppContent(viewModel: KimiViewModel = viewModel()) {
    val navState by viewModel.navigationState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = KimiCream
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
                AppNavigationState.SPLASH -> KimiSplashScreen(viewModel)
                AppNavigationState.ONBOARDING -> KimiOnboardingScreen(viewModel)
                AppNavigationState.LOGIN -> KimiLoginScreen(viewModel)
                AppNavigationState.MAIN -> KimiMainDashboardContainer(viewModel)
            }
        }
    }
}

// ---------------- SPLASH SCREEN ----------------
@Composable
fun KimiSplashScreen(viewModel: KimiViewModel) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alphaScale by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KimiCream)
            .drawBehind {
                // Background warm ambient soft radials
                drawCircle(
                    color = KimiPeach.copy(alpha = 0.4f),
                    radius = 350.dp.toPx(),
                    center = Offset(size.width * 0.2f, size.height * 0.2f)
                )
                drawCircle(
                    color = KimiPeach.copy(alpha = 0.5f),
                    radius = 450.dp.toPx(),
                    center = Offset(size.width * 0.8f, size.height * 0.8f)
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
                    .size(110.dp)
                    .drawBehind {
                        drawCircle(
                            color = KimiBurgundy.copy(alpha = alphaScale * 0.15f),
                            radius = (50f + (alphaScale * 12f)).dp.toPx()
                        )
                    }
                    .background(KimiWhite, CircleShape)
                    .border(1.5.dp, KimiDarkPeach, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.RecordVoiceOver,
                    contentDescription = "Kimi Voice Logo",
                    tint = KimiBurgundy,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "KIMI",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp,
                    color = KimiBurgundy
                )
            )

            Text(
                text = "Client Communication Dialer",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = KimiWarmGray,
                    letterSpacing = 1.sp
                ),
                modifier = Modifier.padding(top = 6.dp)
            )

            Spacer(modifier = Modifier.height(54.dp))

            Button(
                onClick = { viewModel.setNavigationState(AppNavigationState.ONBOARDING) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = KimiBurgundy,
                    contentColor = KimiPeach
                ),
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(56.dp)
                    .testTag("enter_button"),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "GET STARTED",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = KimiPeach,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Proceed",
                        tint = KimiPeach
                    )
                }
            }
        }

        Text(
            text = "KIMI DISPATCH SYSTEM",
            style = MaterialTheme.typography.labelSmall.copy(
                color = KimiWarmGray.copy(alpha = 0.6f),
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
fun KimiOnboardingScreen(viewModel: KimiViewModel) {
    var currentPage by remember { mutableStateOf(0) }
    val headers = listOf(
        "Kimi Call Assistant",
        "Client Response Manager",
        "Direct Call Settings"
    )
    val descriptions = listOf(
        "Deploy warm, helpful caller profiles to manage client followups and onboarding calls smoothly.",
        "Automatic summaries keep track of client calls, tags, and booking requirements in real time.",
        "Integrated communication settings and direct CRM linkages to keep your workflow fully synced."
    )
    val icons = listOf(
        Icons.Rounded.PhoneInTalk,
        Icons.Rounded.GraphicEq,
        Icons.Rounded.Settings
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KimiCream)
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
                        text = "SKIP TO LOGIN",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = KimiBurgundy,
                            fontWeight = FontWeight.Bold,
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
                        .size(150.dp)
                        .background(KimiWhite, CircleShape)
                        .border(1.dp, KimiDarkPeach, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icons[currentPage],
                        contentDescription = headers[currentPage],
                        tint = KimiBurgundy,
                        modifier = Modifier.size(70.dp)
                    )
                }

                Spacer(modifier = Modifier.height(44.dp))

                Text(
                    text = headers[currentPage],
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = KimiCharcoal,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = descriptions[currentPage],
                    style = MaterialTheme.typography.bodyMedium.copy(color = KimiWarmGray),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(36.dp))

                // Page indicator dots
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .size(if (index == currentPage) 12.dp else 8.dp)
                                .background(
                                    color = if (index == currentPage) KimiBurgundy else KimiWhite,
                                    shape = CircleShape
                                )
                                .border(
                                    width = 1.dp,
                                    color = KimiDarkPeach,
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
                        border = BorderStroke(1.dp, KimiDarkPeach),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = KimiBurgundy),
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
                    colors = ButtonDefaults.buttonColors(containerColor = KimiBurgundy),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text(
                        text = if (currentPage == 2) "ENTER APP" else "NEXT",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = KimiPeach,
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
fun KimiLoginScreen(viewModel: KimiViewModel) {
    var email by remember { mutableStateOf("client@kimi.com") }
    var password by remember { mutableStateOf("••••••••") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KimiCream)
            .drawBehind {
                drawCircle(
                    color = KimiPeach.copy(alpha = 0.5f),
                    radius = 400.dp.toPx(),
                    center = Offset(size.width * 0.9f, size.height * 0.1f)
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
                imageVector = Icons.Rounded.Settings,
                contentDescription = "Gear",
                tint = KimiBurgundy,
                modifier = Modifier
                    .size(64.dp)
                    .padding(bottom = 12.dp)
            )

            Text(
                text = "SIGN IN",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp,
                    color = KimiBurgundy
                )
            )

            Text(
                text = "Enter Kimi Operator Credentials",
                style = MaterialTheme.typography.bodyMedium.copy(color = KimiWarmGray),
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        KimiDarkPeach,
                        RoundedCornerShape(28.dp)
                    ),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = KimiWhite)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("OPERATOR EMAIL", color = KimiWarmGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KimiBurgundy,
                            unfocusedBorderColor = KimiDarkPeach,
                            focusedLabelColor = KimiBurgundy,
                            unfocusedLabelColor = KimiWarmGray,
                            focusedTextColor = KimiCharcoal,
                            unfocusedTextColor = KimiCharcoal
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("PASSWORD", color = KimiWarmGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KimiBurgundy,
                            unfocusedBorderColor = KimiDarkPeach,
                            focusedLabelColor = KimiBurgundy,
                            unfocusedLabelColor = KimiWarmGray,
                            focusedTextColor = KimiCharcoal,
                            unfocusedTextColor = KimiCharcoal
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = { viewModel.setNavigationState(AppNavigationState.MAIN) },
                        colors = ButtonDefaults.buttonColors(containerColor = KimiBurgundy),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("login_button")
                    ) {
                        Text(
                            text = "SIGN IN",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = KimiPeach,
                                letterSpacing = 1.5.sp,
                                fontWeight = FontWeight.Bold
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
                        color = KimiBurgundy,
                        letterSpacing = 1.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

// ---------------- MAIN APP WRAPPER CONTAINER ----------------
@Composable
fun KimiMainDashboardContainer(viewModel: KimiViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val isRooted by viewModel.isDeviceRooted.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = KimiCream,
        bottomBar = {
            // Elegant burgundy bottom navigation dock matching Image 1
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(KimiCream)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .border(
                            1.dp,
                            KimiDarkPeach.copy(alpha = 0.5f),
                            RoundedCornerShape(32.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = KimiBurgundy),
                    shape = RoundedCornerShape(32.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        KimiTabNavItem(
                            imageVector = Icons.Default.AutoGraph,
                            label = "Dashboard",
                            selected = currentTab == MainTab.DASHBOARD,
                            onClick = { viewModel.selectTab(MainTab.DASHBOARD) }
                        )
                        KimiTabNavItem(
                            imageVector = Icons.Default.RecordVoiceOver,
                            label = "Agents",
                            selected = currentTab == MainTab.AGENTS,
                            onClick = { viewModel.selectTab(MainTab.AGENTS) }
                        )
                        KimiTabNavItem(
                            imageVector = Icons.Default.Campaign,
                            label = "Campaigns",
                            selected = currentTab == MainTab.CAMPAIGNS,
                            onClick = { viewModel.selectTab(MainTab.CAMPAIGNS) }
                        )
                        KimiTabNavItem(
                            imageVector = Icons.Default.PhoneInTalk,
                            label = "Dialer",
                            selected = currentTab == MainTab.LIVE_CALL,
                            onClick = { viewModel.selectTab(MainTab.LIVE_CALL) }
                        )
                        KimiTabNavItem(
                            imageVector = Icons.Default.Bolt,
                            label = "Rules",
                            selected = currentTab == MainTab.AUTOMATION,
                            onClick = { viewModel.selectTab(MainTab.AUTOMATION) }
                        )
                        KimiTabNavItem(
                            imageVector = Icons.Default.Settings,
                            label = "Settings",
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
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(KimiBurgundy, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "KIMI WORKSPACE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = KimiBurgundy,
                            letterSpacing = 1.5.sp
                        )
                    )
                }

                // Premium status pills
                Box(
                    modifier = Modifier
                        .background(KimiWhite, RoundedCornerShape(12.dp))
                        .border(1.dp, KimiDarkPeach, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isRooted) "DIRECT LINK" else "STANDARD CHANNEL",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = KimiBurgundy,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }

            Divider(color = KimiDarkPeach.copy(alpha = 0.4f), thickness = 1.dp)

            // Dynamic view selector
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (currentTab) {
                    MainTab.DASHBOARD -> KimiDashboardTab(viewModel)
                    MainTab.AGENTS -> KimiAgentTab(viewModel)
                    MainTab.CAMPAIGNS -> KimiCampaignTab(viewModel)
                    MainTab.LIVE_CALL -> KimiDispatchTab(viewModel)
                    MainTab.AUTOMATION -> KimiAutomationTab(viewModel)
                    MainTab.ACCOUNT -> KimiAccountTab(viewModel)
                }
            }
        }
    }
}

@Composable
fun RowScope.KimiTabNavItem(
    imageVector: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val alpha by animateFloatAsState(if (selected) 1.0f else 0.5f, label = "alpha")
    val scale by animateFloatAsState(if (selected) 1.08f else 1.0f, label = "scale")

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
            tint = if (selected) KimiPeach else KimiPeach.copy(alpha = 0.5f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) KimiPeach else KimiPeach.copy(alpha = 0.5f),
            letterSpacing = 0.5.sp
        )
    }
}

// ---------------- DASHBOARD TAB ----------------
@Composable
fun KimiDashboardTab(viewModel: KimiViewModel) {
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
                    text = "System Status",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = KimiBurgundy
                    )
                )
                Text(
                    text = "Overview of dialer volume, campaigns, and call assistants.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = KimiWarmGray)
                )
            }
        }

        // Metrics Grid (Beautiful Rounded White Cards)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                KimiMetricCard(
                    modifier = Modifier.weight(1f),
                    title = "Today's Calls",
                    value = "${callHistory.size * 2 + 14}",
                    subtitle = "Dialed calls",
                    icon = Icons.Default.Phone
                )
                KimiMetricCard(
                    modifier = Modifier.weight(1f),
                    title = "Active Agents",
                    value = "${agentsState.size}",
                    subtitle = "Profiles active",
                    icon = Icons.Default.RecordVoiceOver
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                KimiMetricCard(
                    modifier = Modifier.weight(1f),
                    title = "Contacts",
                    value = "${campaignList.sumOf { it.totalContacts }}",
                    subtitle = "In database",
                    icon = Icons.Default.Contacts
                )
                KimiMetricCard(
                    modifier = Modifier.weight(1f),
                    title = "Success Rate",
                    value = "88.5%",
                    subtitle = "Average success",
                    icon = Icons.Default.TrendingUp
                )
            }
        }

        // Animated Success Trend Line Graphic (Soft, warm line chart)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        KimiDarkPeach.copy(alpha = 0.6f),
                        RoundedCornerShape(28.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = KimiWhite),
                shape = RoundedCornerShape(28.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "CALL SUCCESS TREND",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = KimiBurgundy,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
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
                            color = KimiBurgundy,
                            style = Stroke(width = 3.dp.toPx())
                        )

                        // Smooth gradient fill below
                        val fillPath = Path().apply {
                            addPath(path)
                            lineTo(size.width, size.height)
                            lineTo(0f, size.height)
                            close()
                        }
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(KimiPeach, Color.Transparent)
                            )
                        )

                        // Highlight final coordinate point
                        drawCircle(
                            color = KimiBurgundy,
                            radius = 6.dp.toPx(),
                            center = Offset(size.width, size.height - (points[points.size - 1] / 120f) * size.height)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Average dialer performance registered at 88.5% over this week's cycles.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = KimiWarmGray, fontSize = 12.sp)
                    )
                }
            }
        }

        // Recent Transaction Activity Logs
        item {
            Text(
                text = "RECENT OUTBOUND CALL ACTIVITY",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = KimiBurgundy,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
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
                    Text("No records compiled yet. Tap Dialer to start a call.", color = KimiWarmGray)
                }
            }
        } else {
            items(callHistory) { call ->
                KimiCallHistoryCard(call)
            }
        }
    }
}

@Composable
fun KimiMetricCard(
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
                KimiDarkPeach.copy(alpha = 0.5f),
                RoundedCornerShape(24.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = KimiWhite),
        shape = RoundedCornerShape(24.dp)
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
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = KimiWarmGray,
                        letterSpacing = 0.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(KimiPeach, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = KimiBurgundy,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = KimiBurgundy
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = KimiWarmGray,
                    fontSize = 12.sp
                )
            )
        }
    }
}

@Composable
fun KimiCallHistoryCard(call: CallHistory) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
            .border(
                1.dp,
                KimiDarkPeach.copy(alpha = 0.4f),
                RoundedCornerShape(20.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = KimiWhite),
        shape = RoundedCornerShape(20.dp)
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
                        tint = KimiBurgundy,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = call.contactName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = KimiCharcoal,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .background(KimiPeach, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = call.sentiment,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = KimiBurgundy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = call.transcript,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = KimiWarmGray,
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
                    style = MaterialTheme.typography.labelSmall.copy(color = KimiWarmGray)
                )
                Text(
                    text = "${call.durationSeconds} seconds",
                    style = MaterialTheme.typography.labelSmall.copy(color = KimiWarmGray)
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Call Transcript: ${call.contactName}", color = KimiBurgundy, fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(text = call.transcript, color = KimiCharcoal, style = MaterialTheme.typography.bodyMedium)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = KimiBurgundy)
                ) {
                    Text("Close", color = KimiPeach)
                }
            },
            containerColor = KimiWhite,
            shape = RoundedCornerShape(24.dp)
        )
    }
}

// ---------------- AGENT STUDIO TAB ----------------
@Composable
fun KimiAgentTab(viewModel: KimiViewModel) {
    val agents by viewModel.aiAgents.collectAsState(initial = emptyList())
    val selected by viewModel.selectedAgent.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newSystemPrompt by remember { mutableStateOf("") }
    var selectedVoice by remember { mutableStateOf("Kimi Male (Natural)") }

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
                        text = "Call Agent Studio",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = KimiBurgundy
                        )
                    )
                    Text(
                        text = "Configure custom profiles for client communication.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = KimiWarmGray)
                    )
                }

                Button(
                    onClick = { showCreateDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = KimiBurgundy),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "New Agent", tint = KimiPeach)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New", color = KimiPeach)
                }
            }
        }

        // Active selection slider controls
        selected?.let { agent ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            KimiDarkPeach,
                            RoundedCornerShape(28.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = KimiWhite),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(KimiPeach, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RecordVoiceOver,
                                    contentDescription = null,
                                    tint = KimiBurgundy
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = agent.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = KimiCharcoal,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "Voice Preset: ${agent.voiceType}",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = KimiBurgundy, fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "VOICE WAVEFORM",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = KimiBurgundy,
                                letterSpacing = 1.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Waveform visualization
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
                                        .background(KimiBurgundy, RoundedCornerShape(2.dp))
                                    )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Speech Pitch Modulation",
                            style = MaterialTheme.typography.labelSmall.copy(color = KimiWarmGray)
                        )
                        var tempPitch by remember { mutableStateOf(agent.pitch) }
                        Slider(
                            value = tempPitch,
                            onValueChange = { tempPitch = it },
                            valueRange = 0.5f..1.5f,
                            colors = SliderDefaults.colors(
                                thumbColor = KimiBurgundy,
                                activeTrackColor = KimiBurgundy,
                                inactiveTrackColor = KimiCream
                            )
                        )

                        Text(
                            text = "Orator Pacing Speed",
                            style = MaterialTheme.typography.labelSmall.copy(color = KimiWarmGray)
                        )
                        var tempSpeed by remember { mutableStateOf(agent.speed) }
                        Slider(
                            value = tempSpeed,
                            onValueChange = { tempSpeed = it },
                            valueRange = 0.5f..1.5f,
                            colors = SliderDefaults.colors(
                                thumbColor = KimiBurgundy,
                                activeTrackColor = KimiBurgundy,
                                inactiveTrackColor = KimiCream
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Agent System Prompt / Instructions",
                            style = MaterialTheme.typography.labelSmall.copy(color = KimiWarmGray)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(KimiLightRose, RoundedCornerShape(14.dp))
                                .border(1.dp, KimiDarkPeach, RoundedCornerShape(14.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = agent.systemPrompt,
                                style = MaterialTheme.typography.bodyMedium.copy(color = KimiCharcoal, fontSize = 13.sp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "COMMISSIONED AGENTS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = KimiBurgundy,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
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
                        if (selected?.id == agent.id) KimiBurgundy else KimiDarkPeach.copy(alpha = 0.4f),
                        RoundedCornerShape(20.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = KimiWhite),
                shape = RoundedCornerShape(20.dp)
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
                                .background(KimiPeach, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = KimiBurgundy,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = agent.name,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = KimiCharcoal,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "Voice Preset: ${agent.voiceType}",
                                style = MaterialTheme.typography.labelSmall.copy(color = KimiWarmGray)
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Edit Details",
                        tint = KimiBurgundy,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

    // Modal Create Agent
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Create Call Agent", color = KimiBurgundy, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Agent Name") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KimiBurgundy,
                            unfocusedBorderColor = KimiDarkPeach,
                            focusedTextColor = KimiCharcoal,
                            unfocusedTextColor = KimiCharcoal
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = newSystemPrompt,
                        onValueChange = { newSystemPrompt = it },
                        label = { Text("Core Prompt Script") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KimiBurgundy,
                            unfocusedBorderColor = KimiDarkPeach,
                            focusedTextColor = KimiCharcoal,
                            unfocusedTextColor = KimiCharcoal
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Text("Select Voice Engine Preset", color = KimiWarmGray, style = MaterialTheme.typography.labelSmall)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Kimi Male (Natural)", "Kimi Female (Warm)", "Kimi Outbound (Standard)").forEach { voice ->
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (selectedVoice == voice) KimiBurgundy else KimiCream,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .border(1.dp, KimiDarkPeach, RoundedCornerShape(10.dp))
                                    .clickable { selectedVoice = voice }
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = voice.substringBefore(" "),
                                    color = if (selectedVoice == voice) KimiPeach else KimiCharcoal,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
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
                    colors = ButtonDefaults.buttonColors(containerColor = KimiBurgundy)
                ) {
                    Text("Save Agent", color = KimiPeach)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancel", color = KimiBurgundy)
                }
            },
            containerColor = KimiWhite,
            shape = RoundedCornerShape(24.dp)
        )
    }
}

// ---------------- CAMPAIGN MANAGER TAB ----------------
@Composable
fun KimiCampaignTab(viewModel: KimiViewModel) {
    val campaigns by viewModel.campaigns.collectAsState(initial = emptyList())
    val contacts by viewModel.contacts.collectAsState(initial = emptyList())

    var showCampaignModal by remember { mutableStateOf(false) }
    var campName by remember { mutableStateOf("") }
    var targetVol by remember { mutableStateOf("100") }

    var showContactModal by remember { mutableStateOf(false) }
    var cName by remember { mutableStateOf("") }
    var cPhone by remember { mutableStateOf("") }
    var cTags by remember { mutableStateOf("VIP Lead, Standard") }

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
                        text = "Outbound Campaigns",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = KimiBurgundy
                        )
                    )
                    Text(
                        text = "Track and deploy automated calling flows.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = KimiWarmGray)
                    )
                }

                Button(
                    onClick = { showCampaignModal = true },
                    colors = ButtonDefaults.buttonColors(containerColor = KimiBurgundy),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Default.PlusOne, contentDescription = null, tint = KimiPeach)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Rollout", color = KimiPeach)
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
                        KimiDarkPeach.copy(alpha = 0.5f),
                        RoundedCornerShape(24.dp)
                    ),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = KimiWhite)
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
                                color = KimiCharcoal,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Box(
                            modifier = Modifier
                                .background(
                                    if (campaign.status == "Active") KimiPeach else KimiCream,
                                    RoundedCornerShape(10.dp)
                                )
                                .border(1.dp, KimiDarkPeach, RoundedCornerShape(10.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = campaign.status.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = KimiBurgundy,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Dialer Completed: ${campaign.completedContacts} / ${campaign.totalContacts}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = KimiWarmGray, fontSize = 13.sp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val progressRatio = if (campaign.totalContacts > 0) {
                        campaign.completedContacts.toFloat() / campaign.totalContacts.toFloat()
                    } else 0f

                    LinearProgressIndicator(
                        progress = progressRatio,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = KimiBurgundy,
                        trackColor = KimiCream
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("CALL CONVERSIONS", style = MaterialTheme.typography.labelSmall.copy(color = KimiWarmGray, fontSize = 9.sp))
                            Text("${campaign.successRate}% Success", style = MaterialTheme.typography.titleSmall.copy(color = KimiBurgundy, fontWeight = FontWeight.Bold))
                        }

                        Column {
                            Text("ROUTING ENGINE", style = MaterialTheme.typography.labelSmall.copy(color = KimiWarmGray, fontSize = 9.sp))
                            Text("Kimi System Engine", style = MaterialTheme.typography.titleSmall.copy(color = KimiCharcoal, fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }

        // Contact Directory
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Client Database",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = KimiBurgundy,
                        fontWeight = FontWeight.Bold
                    )
                )

                TextButton(
                    onClick = { showContactModal = true },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text("+ Add VIP Lead", color = KimiBurgundy, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Compact list of contacts
        items(contacts) { contact ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        KimiDarkPeach.copy(alpha = 0.4f),
                        RoundedCornerShape(20.dp)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = KimiWhite)
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
                                color = KimiCharcoal,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Phone: ${contact.phone} • Tag: ${contact.tags}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = KimiWarmGray, fontSize = 12.sp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(KimiPeach, RoundedCornerShape(10.dp))
                            .border(1.dp, KimiDarkPeach, RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = contact.status,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = KimiBurgundy,
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
            title = { Text("Launch Dialing Campaign", color = KimiBurgundy, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = campName,
                        onValueChange = { campName = it },
                        label = { Text("Campaign Identity Name") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KimiBurgundy,
                            unfocusedBorderColor = KimiDarkPeach,
                            focusedTextColor = KimiCharcoal,
                            unfocusedTextColor = KimiCharcoal
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = targetVol,
                        onValueChange = { targetVol = it },
                        label = { Text("Target Volume") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KimiBurgundy,
                            unfocusedBorderColor = KimiDarkPeach,
                            focusedTextColor = KimiCharcoal,
                            unfocusedTextColor = KimiCharcoal
                        ),
                        shape = RoundedCornerShape(12.dp)
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
                    colors = ButtonDefaults.buttonColors(containerColor = KimiBurgundy)
                ) {
                    Text("Provision Campaign", color = KimiPeach)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCampaignModal = false }) {
                    Text("Cancel", color = KimiBurgundy)
                }
            },
            containerColor = KimiWhite,
            shape = RoundedCornerShape(24.dp)
        )
    }

    // Modal Contact Entry
    if (showContactModal) {
        AlertDialog(
            onDismissRequest = { showContactModal = false },
            title = { Text("Add Contact Lead", color = KimiBurgundy, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = cName,
                        onValueChange = { cName = it },
                        label = { Text("Client Full Name") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KimiBurgundy,
                            unfocusedBorderColor = KimiDarkPeach,
                            focusedTextColor = KimiCharcoal,
                            unfocusedTextColor = KimiCharcoal
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = cPhone,
                        onValueChange = { cPhone = it },
                        label = { Text("Phone Address (E.164)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KimiBurgundy,
                            unfocusedBorderColor = KimiDarkPeach,
                            focusedTextColor = KimiCharcoal,
                            unfocusedTextColor = KimiCharcoal
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = cTags,
                        onValueChange = { cTags = it },
                        label = { Text("Tags") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KimiBurgundy,
                            unfocusedBorderColor = KimiDarkPeach,
                            focusedTextColor = KimiCharcoal,
                            unfocusedTextColor = KimiCharcoal
                        ),
                        shape = RoundedCornerShape(12.dp)
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
                    colors = ButtonDefaults.buttonColors(containerColor = KimiBurgundy)
                ) {
                    Text("Commit Lead", color = KimiPeach)
                }
            },
            dismissButton = {
                TextButton(onClick = { showContactModal = false }) {
                    Text("Cancel", color = KimiBurgundy)
                }
            },
            containerColor = KimiWhite,
            shape = RoundedCornerShape(24.dp)
        )
    }
}

// ---------------- LIVE DISPATCH / DIALER TAB ----------------
@Composable
fun KimiDispatchTab(viewModel: KimiViewModel) {
    val contacts by viewModel.contacts.collectAsState(initial = emptyList())
    val callState by viewModel.liveCallState.collectAsState()
    val activeName by viewModel.liveCallName.collectAsState()
    val activePhone by viewModel.liveCallPhone.collectAsState()
    val transcript by viewModel.liveCallTranscript.collectAsState()
    val isSpeaking by viewModel.isAgentSpeaking.collectAsState()

    // Breathing soft peach wave indicator
    val speechPulse = rememberInfiniteTransition(label = "pulseWave")
    val pulseSize by speechPulse.animateFloat(
        initialValue = 100f,
        targetValue = 130f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
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
                            text = "Live Dialer Dispatch",
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = KimiBurgundy
                            )
                        )
                        Text(
                            text = "Select an existing client contact below and trigger a simulated dial immediately.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = KimiWarmGray)
                        )
                    }
                }

                item {
                    Text(
                        text = "AVAILABLE CLIENT CONTACTS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = KimiBurgundy,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                items(contacts) { contact ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                KimiDarkPeach.copy(alpha = 0.5f),
                                RoundedCornerShape(24.dp)
                            ),
                        colors = CardDefaults.cardColors(containerColor = KimiWhite),
                        shape = RoundedCornerShape(24.dp)
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
                                        color = KimiCharcoal,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = contact.phone,
                                    style = MaterialTheme.typography.bodyMedium.copy(color = KimiWarmGray)
                                )
                            }

                            Button(
                                onClick = { viewModel.initiateSimulatedCall(contact.name, contact.phone) },
                                colors = ButtonDefaults.buttonColors(containerColor = KimiBurgundy),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.testTag("dial_${contact.name.replace(" ", "_")}")
                            ) {
                                Icon(imageVector = Icons.Default.PhoneInTalk, contentDescription = "Dial", tint = KimiPeach)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Call", color = KimiPeach)
                            }
                        }
                    }
                }
            }
        } else {
            // Live Call Screen styled like Screen 2 of Image 1
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
                            KimiDarkPeach,
                            RoundedCornerShape(28.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = KimiWhite),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "CALL STATUS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = KimiWarmGray,
                                letterSpacing = 2.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = activeName,
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = KimiCharcoal,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        )

                        Text(
                            text = activePhone,
                            style = MaterialTheme.typography.bodyMedium.copy(color = KimiWarmGray)
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        // Soft breathing radial backplate for voice representation
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .drawBehind {
                                    val finalPulse = if (isSpeaking) pulseSize.dp.toPx() else 100.dp.toPx()
                                    drawCircle(
                                        color = KimiPeach,
                                        radius = finalPulse
                                    )
                                    drawCircle(
                                        color = KimiDarkPeach,
                                        radius = 70.dp.toPx()
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.VolumeUp else Icons.Default.Mic,
                                contentDescription = null,
                                tint = KimiBurgundy,
                                modifier = Modifier.size(44.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = if (callState == CallState.CONNECTING) "CONNECTING CALL CHANNELS..." else "CALL IN PROGRESS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = KimiBurgundy,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                // Call transcript log scrolling box styled with custom bubbles
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .border(1.dp, KimiDarkPeach.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                    colors = CardDefaults.cardColors(containerColor = KimiWhite),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            Text(
                                text = "REAL-TIME TRANSCRIPT LOG",
                                style = MaterialTheme.typography.labelSmall.copy(color = KimiBurgundy, fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (transcript.isEmpty()) "Connecting to client line..." else transcript,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = KimiCharcoal,
                                    lineHeight = 22.sp
                                )
                            )
                        }
                    }
                }

                // Hang Up Button (Burgundy round button)
                Button(
                    onClick = { viewModel.hangUpCall() },
                    colors = ButtonDefaults.buttonColors(containerColor = KimiBurgundy),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(68.dp)
                        .testTag("hangup_button"),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = "Hang Up", tint = KimiPeach, modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}

// ---------------- AUTOMATION TAB ----------------
@Composable
fun KimiAutomationTab(viewModel: KimiViewModel) {
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
                        text = "Automation Rules",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = KimiBurgundy
                        )
                    )
                    Text(
                        text = "Deploy automatic triggers to synchronize call events.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = KimiWarmGray)
                    )
                }

                Button(
                    onClick = { showRuleModal = true },
                    colors = ButtonDefaults.buttonColors(containerColor = KimiBurgundy),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = KimiPeach)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add", color = KimiPeach)
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
                        KimiDarkPeach.copy(alpha = 0.5f),
                        RoundedCornerShape(20.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = KimiWhite),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(KimiPeach, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = KimiBurgundy)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "TRIGGER: ${rule.triggerEvent}",
                            style = MaterialTheme.typography.labelSmall.copy(color = KimiBurgundy, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "ACTION: ${rule.actionToTake}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = KimiCharcoal, fontWeight = FontWeight.SemiBold)
                        )
                    }

                    Switch(
                        checked = rule.isEnabled,
                        onCheckedChange = { /* Persistent Switch callback placeholder */ },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = KimiPeach,
                            checkedTrackColor = KimiBurgundy,
                            uncheckedThumbColor = KimiWarmGray,
                            uncheckedTrackColor = KimiCream
                        )
                    )
                }
            }
        }

        // CRM Integration
        item {
            Text(
                text = "CRM INTEGRATIONS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = KimiBurgundy,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        item {
            KimiIntegrationCard(
                title = "HubSpot CRM Sync",
                description = "Automatically map call results to pipeline contacts.",
                isActive = hsActive,
                onToggle = { viewModel.toggleHubspot() }
            )
        }

        item {
            KimiIntegrationCard(
                title = "Salesforce Integration",
                description = "Sync call details directly into client opportunities.",
                isActive = sfActive,
                onToggle = { viewModel.toggleSalesforce() }
            )
        }

        item {
            KimiIntegrationCard(
                title = "Zapier Connect Engine",
                description = "Trigger workflows and notifications immediately.",
                isActive = zpActive,
                onToggle = { viewModel.toggleZapier() }
            )
        }
    }

    if (showRuleModal) {
        AlertDialog(
            onDismissRequest = { showRuleModal = false },
            title = { Text("Create Automation Rule", color = KimiBurgundy, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = trigText,
                        onValueChange = { trigText = it },
                        label = { Text("Triggering Event (e.g. On Lead)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KimiBurgundy,
                            unfocusedBorderColor = KimiDarkPeach,
                            focusedTextColor = KimiCharcoal,
                            unfocusedTextColor = KimiCharcoal
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = actText,
                        onValueChange = { actText = it },
                        label = { Text("Action to Perform") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KimiBurgundy,
                            unfocusedBorderColor = KimiDarkPeach,
                            focusedTextColor = KimiCharcoal,
                            unfocusedTextColor = KimiCharcoal
                        ),
                        shape = RoundedCornerShape(12.dp)
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
                    colors = ButtonDefaults.buttonColors(containerColor = KimiBurgundy)
                ) {
                    Text("Provision Rule", color = KimiPeach)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRuleModal = false }) {
                    Text("Cancel", color = KimiBurgundy)
                }
            },
            containerColor = KimiWhite,
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
fun KimiIntegrationCard(
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
                if (isActive) KimiBurgundy else KimiDarkPeach.copy(alpha = 0.4f),
                RoundedCornerShape(20.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = KimiWhite),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(color = KimiCharcoal, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(color = KimiWarmGray, fontSize = 12.sp)
                )
            }

            Switch(
                checked = isActive,
                onToggle = onToggle
            )
        }
    }
}

@Composable
fun Switch(
    checked: Boolean,
    onToggle: () -> Unit
) {
    Switch(
        checked = checked,
        onCheckedChange = { onToggle() },
        colors = SwitchDefaults.colors(
            checkedThumbColor = KimiPeach,
            checkedTrackColor = KimiBurgundy,
            uncheckedThumbColor = KimiWarmGray,
            uncheckedTrackColor = KimiCream
        )
    )
}

// ---------------- SETTINGS & SYSTEM DETAILS TAB ----------------
@Composable
fun KimiAccountTab(viewModel: KimiViewModel) {
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
                    text = "Kimi Systems & Diagnostics",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = KimiBurgundy
                    )
                )
                Text(
                    text = "System parameters, custom audio routing, and direct console logs.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = KimiWarmGray)
                )
            }
        }

        // Active billing tier select
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        KimiDarkPeach,
                        RoundedCornerShape(24.dp)
                    ),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = KimiWhite)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "LICENSE SUBSCRIPTION STATUS",
                        style = MaterialTheme.typography.labelSmall.copy(color = KimiWarmGray, letterSpacing = 1.sp, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentBillingTier,
                        style = MaterialTheme.typography.titleMedium.copy(color = KimiBurgundy, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Allows continuous dialing, call history archiving, and custom caller assistants.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = KimiWarmGray, fontSize = 12.sp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { viewModel.setSaasTier("Kimi Pro Enterprise") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentBillingTier.contains("Enterprise")) KimiBurgundy else KimiCream
                            ),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, KimiDarkPeach)
                        ) {
                            Text(
                                text = "Enterprise Tier",
                                color = if (currentBillingTier.contains("Enterprise")) KimiPeach else KimiCharcoal,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { viewModel.setSaasTier("Kimi Standard Pro") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentBillingTier.contains("Standard")) KimiBurgundy else KimiCream
                            ),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, KimiDarkPeach)
                        ) {
                            Text(
                                text = "Standard Pro",
                                color = if (currentBillingTier.contains("Standard")) KimiPeach else KimiCharcoal,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Toggles for Direct Audio Links
        item {
            Text(
                text = "DIRECT AUDIO SETTINGS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = KimiBurgundy,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        KimiDarkPeach.copy(alpha = 0.5f),
                        RoundedCornerShape(20.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = KimiWhite),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Direct Audio Link Mode",
                            style = MaterialTheme.typography.bodyLarge.copy(color = KimiCharcoal, fontWeight = FontWeight.Bold)
                        )
                        Box(
                            modifier = Modifier
                                .background(KimiPeach, RoundedCornerShape(8.dp))
                                .border(1.dp, KimiDarkPeach, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isRooted) "AVAILABLE" else "STANDARD LAYER",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = KimiBurgundy,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isRooted) "Direct dialer audio channels and voice synthesis modules are active." else "Standard system audio is in use. Running standard voice channels.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = KimiWarmGray, fontSize = 12.sp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Hardware Mixer Direct Injection",
                        style = MaterialTheme.typography.labelSmall.copy(color = KimiWarmGray)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Inject audio signals directly to hardware mixer channel.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = KimiWarmGray, fontSize = 12.sp),
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = lowLevelInject,
                            onToggle = { viewModel.toggleLowLevelAudio() }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Baseband Phone Call Tape",
                        style = MaterialTheme.typography.labelSmall.copy(color = KimiWarmGray)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Link simulated call tape directly into cellular bus.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = KimiWarmGray, fontSize = 12.sp),
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = rawBusTap,
                            onToggle = { viewModel.toggleTelephonyBusTap() }
                        )
                    }
                }
            }
        }

        // Kimi Console (Premium burgundy console with peach text)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(1.dp, KimiDarkPeach, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2E0417)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "KIMI CONSOLE DIAGNOSTICS",
                        style = MaterialTheme.typography.labelSmall.copy(color = KimiPeach, fontWeight = FontWeight.Bold)
                    )
                    Divider(color = KimiPeach.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 8.dp))
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            terminalLines.forEach { logLine ->
                                Text(
                                    text = logLine,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = KimiPeach,
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

        // Clear logs button
        item {
            Button(
                onClick = { viewModel.clearAllLogs() },
                colors = ButtonDefaults.buttonColors(containerColor = KimiBurgundy),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Truncate Outbound History Logs", color = KimiPeach)
            }
        }
    }
}
