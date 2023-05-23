package com.example.quizhuntercompose.feature_auth.presentation_profile.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable

@Composable
fun LanguagePicker(
    languages: Map<Boolean, String>,
    onSelectLanguage: (language: String) -> Unit,
    expanded: Boolean
){
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { /*TODO*/ }
    ) {
       LazyColumn(
           content = {}
       )
    }
}

@Composable
fun languageItem(language: String){
    Row() {

    }
}