package com.meqat.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Cream = Color(0xFFFAF9F6)
private val Sage = Color(0xFF8A9A5B)
private val Dark = Color(0xFF333333)
private val Gold = Color(0xFFC6A96B)
private val SoftGreen = Color(0xFFECEFDF)
private val Muted = Color(0xFF88877F)
private val White = Color.White

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                MeqatApp()
            }
        }
    }
}

@Composable
fun MeqatApp() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = Cream,
        bottomBar = {
            NavigationBar(
                containerColor = White,
                contentColor = Sage
            ) {
                val tabs = listOf(
                    "الرئيسية",
                    "القبلة",
                    "القرآن",
                    "الأذكار",
                    "الإعدادات"
                )

                val symbols = listOf("⌂", "◉", "▤", "۞", "⚙")

                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Text(
                                text = symbols[index],
                                fontSize = 21.sp
                            )
                        },
                        label = {
                            Text(
                                text = title,
                                fontSize = 10.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Sage,
                            selectedTextColor = Sage,
                            indicatorColor = SoftGreen,
                            unselectedIconColor = Muted,
                            unselectedTextColor = Muted
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedTab) {
                0 -> HomeScreen()
                1 -> SimpleScreen(
                    "القبلة",
                    "اعرف اتجاه القبلة من موقعك"
                )
                2 -> SimpleScreen(
                    "القرآن الكريم",
                    "كتاب الله بين يديك"
                )
                3 -> SimpleScreen(
                    "الأذكار",
                    "اذكر الله واطمئن قلبك"
                )
                4 -> SettingsScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 24.dp,
            bottom = 28.dp
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "مِيقات",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Sage
                    )
                    Text(
                        text = "رفيقك إلى الصلاة",
                        fontSize = 13.sp,
                        color = Muted
                    )
                }

                Text(
                    text = "☾",
                    color = Sage,
                    fontSize = 36.sp
                )
            }
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = White,
                border = BorderStroke(1.dp, Color(0xFFEAE8E0))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⌖", fontSize = 25.sp, color = Sage)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "موقعك الحالي",
                            color = Dark,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "لم يتم تحديد الموقع بعد",
                            color = Muted,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = Sage
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "الصلاة القادمة",
                        color = White.copy(alpha = 0.85f),
                        fontSize = 14.sp
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "--:--",
                        color = White,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "بانتظار تحديد الموقع وحساب المواقيت",
                        color = White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        item {
            Column {
                Text(
                    "مواقيت الصلاة",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Dark
                )

                Spacer(Modifier.height(12.dp))

                val prayers = listOf(
                    "الفجر",
                    "الشروق",
                    "الظهر",
                    "العصر",
                    "المغرب",
                    "العشاء"
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    prayers.chunked(2).forEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(9.dp)
                        ) {
                            pair.forEach { prayer ->
                                PrayerCard(
                                    name = prayer,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text(
                    "الوصول السريع",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Dark
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickCard("القرآن الكريم", "▤", Modifier.weight(1f))
                    QuickCard("الأذكار", "۞", Modifier.weight(1f))
                    QuickCard("القبلة", "◉", Modifier.weight(1f))
                }
            }
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = SoftGreen
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "﴿ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ ﴾",
                        color = Dark,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(7.dp))
                    Text(
                        "سورة الرعد - 28",
                        color = Muted,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PrayerCard(
    name: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        color = White,
        border = BorderStroke(1.dp, Color(0xFFEAE8E0))
    ) {
        Column(
            modifier = Modifier.padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                name,
                color = Dark,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(7.dp))
            Text(
                "--:--",
                color = Sage,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun QuickCard(
    title: String,
    symbol: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = White,
        border = BorderStroke(1.dp, Color(0xFFEAE8E0))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 17.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                symbol,
                color = Sage,
                fontSize = 25.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                title,
                color = Dark,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SimpleScreen(
    title: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            title,
            color = Sage,
            fontSize = 29.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))
        Text(
            description,
            color = Muted,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(24.dp)
    ) {
        Text(
            "الإعدادات",
            color = Dark,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = White
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Text("إعدادات الموقع", color = Dark)
                HorizontalDivider(color = Cream)
                Text("إعدادات الصلاة", color = Dark)
                HorizontalDivider(color = Cream)
                Text("إعدادات الأذان", color = Dark)
                HorizontalDivider(color = Cream)
                Text("المظهر والخط", color = Dark)
                HorizontalDivider(color = Cream)
                Text("عن مِيقات", color = Dark)
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            "مِيقات | MEQAT",
            color = Sage,
            fontWeight = FontWeight.Bold,
            fontSize = 19.sp
        )

        Text(
            "إعداد وتطوير: عبدالرحمن أحمد",
            color = Muted,
            fontSize = 13.sp
        )
    }
}
