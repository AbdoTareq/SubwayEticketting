package com.abdotareq.subway_e_ticketing.model

import ir.mirrajabi.searchdialog.core.Searchable

class SearchModel(private var name: String) : Searchable {
    override fun getTitle(): String {
        return name
    }
}