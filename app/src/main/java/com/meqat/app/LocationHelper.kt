package com.meqat.app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.core.content.ContextCompat

object LocationHelper {

    fun getCurrentLocation(
        context: Context,
        onSuccess: (Double, Double) -> Unit,
        onError: (String) -> Unit
    ) {
        val finePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarsePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!finePermission && !coarsePermission) {
            onError("يجب السماح للتطبيق بالوصول إلى الموقع")
            return
        }

        try {
            val manager = context.getSystemService(
                Context.LOCATION_SERVICE
            ) as LocationManager

            val providers = listOf(
                LocationManager.GPS_PROVIDER,
                LocationManager.NETWORK_PROVIDER
            )

            for (provider in providers) {
                if (!manager.isProviderEnabled(provider)) continue

                val lastLocation = manager.getLastKnownLocation(provider)

                if (lastLocation != null) {
                    onSuccess(
                        lastLocation.latitude,
                        lastLocation.longitude
                    )
                    return
                }
            }

            val provider = providers.firstOrNull {
                manager.isProviderEnabled(it)
            }

            if (provider == null) {
                onError("فعّل خدمة الموقع في الهاتف")
                return
            }

            val listener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    manager.removeUpdates(this)

                    onSuccess(
                        location.latitude,
                        location.longitude
                    )
                }

                override fun onProviderDisabled(provider: String) {
                    manager.removeUpdates(this)
                    onError("خدمة الموقع غير متاحة")
                }

                override fun onProviderEnabled(provider: String) {}

                @Deprecated("Deprecated by Android")
                override fun onStatusChanged(
                    provider: String?,
                    status: Int,
                    extras: Bundle?
                ) {}
            }

            manager.requestSingleUpdate(
                provider,
                listener,
                Looper.getMainLooper()
            )

        } catch (e: SecurityException) {
            onError("لم يتم السماح للتطبيق باستخدام الموقع")
        } catch (e: Exception) {
            onError("تعذر تحديد الموقع حاليًا")
        }
    }
}
