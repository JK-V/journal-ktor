package com.jkv.journal.model

val blogEntries = mutableListOf(BlogEntry(
    "The drive to develop!",
    "...it's what keeps me going."
))

data class BlogEntry(val headline: String, val body: String)