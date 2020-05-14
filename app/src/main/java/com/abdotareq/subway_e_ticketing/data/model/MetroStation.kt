package com.abdotareq.subway_e_ticketing.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MetroStation(@SerializedName("stationId")
                        var stationId: Int = 0,

                        @SerializedName("stationName")
                        var stationName: String? = null,

                        @SerializedName("lineNumber")
                        var lineNumber: Int = 0,

                        @SerializedName("previousStation")
                        var previousStation: Int? = null,

                        @SerializedName("nextStation")
                        var nextStation: Int? = null,

                        @SerializedName("doubleLineStation")
                        var doubleLineStation: Int = 0,

                        @SerializedName("commonStationId")
                        var commonStationId: Int? = null,

                        @SerializedName("commonLineNumber")
                        var commonLineNumber: Int? = null,

                        @SerializedName("latitude")
                        var latitude: Double = 0.0,

                        @SerializedName("longitude")
                        var longitude: Double = 0.0)