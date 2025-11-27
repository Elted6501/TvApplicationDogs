package com.example.tvapplication

data class Breed(
    val name: String,
    val displayName: String,
    val subBreeds: List<String> = emptyList(),
    val hasSubBreeds: Boolean = false
)
