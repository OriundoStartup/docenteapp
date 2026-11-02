package com.example.docenteapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform