package com.kimseongwooo.pawming.feature.shelterdetail.components

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.overlay.Marker

@Composable
internal fun ShelterLocationMap(
    lat: Double,
    lng: Double,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember { MapView(context) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDestroy()
        }
    }

    Box(modifier = modifier.background(Color(0xFFEEEEEE))) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.matchParentSize(),
            update = { view ->
                view.getMapAsync { naverMap ->
                    val position = LatLng(lat, lng)
                    naverMap.cameraPosition = CameraPosition(position, 15.0)
                    with(naverMap.uiSettings) {
                        isZoomControlEnabled = true
                        isZoomGesturesEnabled = true
                        isScrollGesturesEnabled = false
                        isTiltGesturesEnabled = false
                        isRotateGesturesEnabled = false
                    }
                    Marker().apply {
                        this.position = position
                        this.map = naverMap
                    }
                }
            }
        )
    }
}