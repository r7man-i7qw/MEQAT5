package com.meqat.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeqatStart()
        }
    }
}

@Composable
fun MeqatStart() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9F6)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "مِيقات",
            color = Color(0xFF8A9A5B),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "رفيقك إلى الصلاة",
            color = Color(0xFF333333),
            fontSize = 18.sp
        )
    }
}
