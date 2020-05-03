package com.abdotareq.subway_e_ticketing.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MetroStation(@SerializedName("STATION_ID")
                        var stationId: Int = 0,

                        @SerializedName("STATION_NAME")
                        var stationName: String? = null,

                        @SerializedName("LINE_NUMBER")
                        var lineNumber: Int = 0,

                        @SerializedName("PREVIOUS_STATION")
                        var previousStation: Int? = null,

                        @SerializedName("NEXT_STATION")
                        var nextStation: Int? = null,

                        @SerializedName("IS_DOUBLE_LINE")
                        var doubleLineStation: Int = 0,

                        @SerializedName("COMMON_STATION_ID")
                        var commonStationId: Int? = null,

                        @SerializedName("COMMON_LINE_NUMBER")
                        var commonLineNumber: Int? = null,

                        @SerializedName("LATITUDE")
                        var latitude: Double = 0.0,

                        @SerializedName("LONGITUDE")
                        var longitude: Double = 0.0)