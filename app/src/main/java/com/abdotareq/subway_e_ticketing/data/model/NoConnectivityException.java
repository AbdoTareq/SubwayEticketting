package com.abdotareq.subway_e_ticketing.data.model;

import java.io.IOException;

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No Internet Connection";
        // You can send any message whatever you want from here.
    }
}