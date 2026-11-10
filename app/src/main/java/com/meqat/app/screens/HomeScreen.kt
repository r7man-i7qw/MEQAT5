package com.meqat.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meqat.app.ui.theme.MeqatPalette

@Composable
fun HomeScreen(
    locationName: String = "جاري تحديد الموقع",
    nextPrayer: String = "الفجر",
    nextPrayerTime: String = "--:--",
    countdown: String = "--:--:--",
    fajr: String = "--:--",
    dhuhr: String = "--:--",
    asr: String = "--:--",
    maghrib: String = "--:--",
    isha: String = "--:--"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MeqatPalette.Background)
            .padding(20.dp)
    ) {
        Text(
            text = "مِيقات",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MeqatPalette.Primary
        )

        Text(
            text = "رفيقك إلى الصلاة",
            fontSize = 14.sp,
            color = MeqatPalette.TextSecondary
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "السلام عليكم ورحمة الله",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = MeqatPalette.TextPrimary
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = locationName,
            fontSize = 14.sp,
            color = MeqatPalette.TextSecondary
        )

        Spacer(Modifier.height(22.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(
                containerColor = MeqatPalette.Primary
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(23.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "الصلاة القادمة",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = nextPrayer,
                    color = Color.White,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = nextPrayerTime,
                    color = Color.White,
                    fontSize = 22.sp
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "الوقت المتبقي",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )

                Text(
                    text = countdown,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(25.dp))

        Text(
            text = "مواقيت الصلاة",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MeqatPalette.TextPrimary
        )

        Spacer(Modifier.height(12.dp))

        PrayerTimeRow("الفجر", fajr)
        PrayerTimeRow("الظهر", dhuhr)
        PrayerTimeRow("العصر", asr)
        PrayerTimeRow("المغرب", maghrib)
        PrayerTimeRow("العشاء", isha)

        Spacer(Modifier.height(18.dp))

        Text(
            text = "اللهم أعنّا على ذكرك وشكرك وحسن عبادتك",
            fontSize = 14.sp,
            color = MeqatPalette.PrimaryDark,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun PrayerTimeRow(
    prayer: String,
    time: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(
                MeqatPalette.Surface,
                RoundedCornerShape(15.dp)
            )
            .padding(horizontal = 17.dp, vertical = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = prayer,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MeqatPalette.TextPrimary
        )

        Text(
            text = time,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MeqatPalette.Primary
        )
    }
}
