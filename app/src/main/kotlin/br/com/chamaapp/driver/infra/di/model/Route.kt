package br.com.chamaapp.driver.infra.di.model

class Route(
    val bounds: String?,
    val distance: Int,
    val points: String,
    val duration: Int,
    val durationWithTraffic: Int
)