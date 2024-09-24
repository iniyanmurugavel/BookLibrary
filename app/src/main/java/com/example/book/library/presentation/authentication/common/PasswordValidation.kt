package com.example.book.library.presentation.authentication.common

import java.util.regex.Pattern

fun validatePassword(password: String): String {
    if (password.length < 8) {
        return "Password must be at least 8 characters long."
    }

    val numberPattern = Pattern.compile("[0-9]")
    if (!numberPattern.matcher(password).find()) {
        return "Password must contain at least one number."
    }

    val lowercasePattern = Pattern.compile("[a-z]")
    if (!lowercasePattern.matcher(password).find()) {
        return "Password must contain at least one lowercase letter."
    }

    val uppercasePattern = Pattern.compile("[A-Z]")
    if (!uppercasePattern.matcher(password).find()) {
        return "Password must contain at least one uppercase letter."
    }

    val specialCharPattern = Pattern.compile("[!@#\$%^&*()]")
    if (!specialCharPattern.matcher(password).find()) {
        return "Password must contain at least one special character (!@#\$%^&*())."
    }

    return ""
}