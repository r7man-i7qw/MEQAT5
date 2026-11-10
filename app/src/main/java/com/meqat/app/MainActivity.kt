package com.meqat.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.meqat.app.screens.HomeScreen as MeqatHomeScreen
import com.meqat.app.ui.theme.MeqatTheme
import com.meqat.app.ui.theme.MeqatPalette

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MeqatTheme {
                MeqatApp()
            }
        }
    }
}

@Composable
fun MeqatApp() {
    val context = androidx.compose.ui.platform.LocalContext.current

    var selectedTab by remember { mutableIntStateOf(0) }
    var prayerTimes by remember { mutableStateOf<PrayerTimes?>(null) }
    var loading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("جارٍ تحديد موقعك") }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (granted) {
            loadPrayerTimes(
                context,
                { prayerTimes = it; loading = false; message = "تم تحديث المواقيت" },
                { message = it; loading = false },
                { loading = true }
            )
        } else {
            loading = false
            message = "يجب السماح بالوصول إلى الموقع"
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

    val next = prayerTimes?.let { getNextPrayer(it) }

    Scaffold(
        containerColor = MeqatPalette.Background,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                val tabs = listOf(
                    "الرئيسية", "القبلة", "القرآن",
                    "الأذكار", "الإعدادات"
                )
                val icons = listOf("⌂", "◉", "▤", "۞", "⚙")

                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Text(icons[index], fontSize = 21.sp)
                        },
                        label = {
                            Text(title, fontSize = 10.sp)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MeqatPalette.Primary,
                            selectedTextColor = MeqatPalette.Primary,
                            indicatorColor = MeqatPalette.SoftGreen,
                            unselectedIconColor = MeqatPalette.TextSecondary,
                            unselectedTextColor = MeqatPalette.TextSecondary
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
                0 -> MeqatHomeScreen(
                    locationName = if (prayerTimes != null)
                        "تم تحديد موقعك"
                    else message,
                    nextPrayer = next?.first ?: "--",
                    nextPrayerTime = next?.second ?: "--:--",
                    countdown = "--:--:--",
                    fajr = prayerTimes?.fajr ?: "--:--",
                    dhuhr = prayerTimes?.dhuhr ?: "--:--",
                    asr = prayerTimes?.asr ?: "--:--",
                    maghrib = prayerTimes?.maghrib ?: "--:--",
                    isha = prayerTimes?.isha ?: "--:--"
                )

                1 -> SimpleScreen("القبلة", "سيتم تفعيل البوصلة الحقيقية هنا")
                2 -> SimpleScreen("القرآن الكريم", "سيتم إضافة المصحف الكامل هنا")
                3 -> SimpleScreen("الأذكار", "سيتم إضافة الأذكار والعداد هنا")
                4 -> SettingsScreen(
                    message = message,
                    loading = loading,
                    onUpdateLocation = { requestLocation() }
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
    val format = java.text.SimpleDateFormat("HH:mm", java.util.Locale.US)
    val now = format.format(java.util.Date())

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
        modifier = Modifier
            .fillMaxSize()
            .background(MeqatPalette.Background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            title,
            color = MeqatPalette.Primary,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        Text(
            description,
            color = MeqatPalette.TextSecondary
        )
    }
}

@Composable
fun SettingsScreen(
    message: String,
    loading: Boolean,
    onUpdateLocation: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MeqatPalette.Background)
            .padding(24.dp)
    ) {
        Text(
            "الإعدادات",
            color = MeqatPalette.TextPrimary,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        Text("الموقع ومواقيت الصلاة", color = MeqatPalette.Primary)

        Spacer(Modifier.height(8.dp))

        Text(message, color = MeqatPalette.TextSecondary)

        Button(
            onClick = onUpdateLocation,
            colors = ButtonDefaults.buttonColors(
                containerColor = MeqatPalette.Primary
            )
        ) {
            Text(if (loading) "جارٍ التحديث..." else "تحديث الموقع والمواقيت")
        }

        Spacer(Modifier.height(25.dp))

        Text(
            "إعداد وتطوير: عبدالرحمن أحمد",
            color = MeqatPalette.TextSecondary
        )

        Text(
            "مِيقات | MEQAT",
            color = MeqatPalette.Primary
        )

        Text(
            "رفيقك إلى الصلاة",
            color = MeqatPalette.TextSecondary,
            fontSize = 13.sp
        )
    }
}
