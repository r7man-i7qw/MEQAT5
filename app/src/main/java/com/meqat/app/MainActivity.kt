package com.meqat.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Cream = Color(0xFFFAF9F6)
private val Sage = Color(0xFF8A9A5B)
private val Dark = Color(0xFF333333)
private val Muted = Color(0xFF88877F)
private val White = Color.White
private val SoftGreen = Color(0xFFECEFDF)

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
            NavigationBar(containerColor = White) {
                val tabs = listOf(
                    "الرئيسية", "القبلة", "القرآن",
                    "الأذكار", "الإعدادات"
                )
                val icons = listOf("⌂", "◉", "▤", "۞", "⚙")

                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Text(icons[index], fontSize = 21.sp) },
                        label = { Text(title, fontSize = 10.sp) },
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
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedTab) {
                0 -> HomeScreen()
                1 -> SimpleScreen("القبلة", "تحديد اتجاه القبلة")
                2 -> SimpleScreen("القرآن الكريم", "كتاب الله بين يديك")
                3 -> SimpleScreen("الأذكار", "اذكر الله واطمئن قلبك")
                4 -> SettingsScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val context = androidx.compose.ui.platform.LocalContext.current

    var prayerTimes by remember { mutableStateOf<PrayerTimes?>(null) }
    var loading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("حدد موقعك لحساب المواقيت") }
    var locationGranted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        locationGranted =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (locationGranted) {
            loadPrayerTimes(
                context,
                { result ->
                    prayerTimes = result
                    loading = false
                    message = "تم تحديث مواقيت الصلاة"
                },
                { error ->
                    loading = false
                    message = error
                },
                { loading = true }
            )
        } else {
            message = "لم يتم السماح بالوصول إلى الموقع"
        }
    }

    fun requestLocation() {
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fine || coarse) {
            locationGranted = true
            loadPrayerTimes(
                context,
                { prayerTimes = it; loading = false; message = "تم تحديث المواقيت" },
                { message = it; loading = false },
                { loading = true }
            )
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        requestLocation()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Column {
                Text(
                    "مِيقات",
                    color = Sage,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "رفيقك إلى الصلاة",
                    color = Muted,
                    fontSize = 13.sp
                )
            }
        }

        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = White,
                border = BorderStroke(1.dp, Color(0xFFEAE8E0))
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("مواقيت الصلاة حسب موقعك", color = Dark)
                    Spacer(Modifier.height(6.dp))
                    Text(message, color = Muted, fontSize = 12.sp)

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = { requestLocation() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Sage
                        )
                    ) {
                        Text(
                            if (loading) "جارٍ التحديث..." else "تحديد الموقع وتحديث المواقيت"
                        )
                    }
                }
            }
        }

        item {
            val times = prayerTimes
            val nextPrayer = times?.let { getNextPrayer(it) }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = Sage
            ) {
                Column(
                    Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("الصلاة القادمة", color = White)
                    Spacer(Modifier.height(10.dp))
                    Text(
                        nextPrayer?.first ?: "--",
                        color = White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        nextPrayer?.second ?: "--:--",
                        color = White,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {
            Text(
                "مواقيت الصلاة",
                color = Dark,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            val times = prayerTimes

            val prayers = listOf(
                "الفجر" to times?.fajr,
                "الشروق" to times?.sunrise,
                "الظهر" to times?.dhuhr,
                "العصر" to times?.asr,
                "المغرب" to times?.maghrib,
                "العشاء" to times?.isha
            )

            Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                prayers.chunked(2).forEach { pair ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        pair.forEach { prayer ->
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(15.dp),
                                color = White,
                                border = BorderStroke(
                                    1.dp,
                                    Color(0xFFEAE8E0)
                                )
                            ) {
                                Column(
                                    Modifier.padding(15.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(prayer.first, color = Dark)
                                    Spacer(Modifier.height(7.dp))
                                    Text(
                                        prayer.second ?: "--:--",
                                        color = Sage,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = SoftGreen
            ) {
                Text(
                    "﴿ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ ﴾",
                    Modifier.padding(18.dp),
                    color = Dark,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        }
    }
}

fun loadPrayerTimes(
    context: android.content.Context,
    onSuccess: (PrayerTimes) -> Unit,
    onError: (String) -> Unit,
    onLoading: () -> Unit
) {
    onLoading()

    LocationHelper.getCurrentLocation(
        context,
        { latitude, longitude ->
            PrayerTimesRepository.fetch(
                latitude,
                longitude,
                onSuccess,
                onError
            )
        },
        onError
    )
}

fun getNextPrayer(times: PrayerTimes): Pair<String, String> {
    val format = SimpleDateFormat("HH:mm", Locale.US)
    val now = format.format(Date())

    val prayers = listOf(
        "الفجر" to times.fajr,
        "الظهر" to times.dhuhr,
        "العصر" to times.asr,
        "المغرب" to times.maghrib,
        "العشاء" to times.isha
    )

    return prayers.firstOrNull { it.second > now }
        ?: ("الفجر" to times.fajr)
}

@Composable
fun SimpleScreen(title: String, description: String) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, color = Sage, fontSize = 29.sp)
        Spacer(Modifier.height(12.dp))
        Text(description, color = Muted)
    }
}

@Composable
fun SettingsScreen() {
    Column(
        Modifier
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
        Text("إعداد وتطوير: عبدالرحمن أحمد", color = Muted)
        Text("مِيقات | MEQAT", color = Sage)
    }
}
