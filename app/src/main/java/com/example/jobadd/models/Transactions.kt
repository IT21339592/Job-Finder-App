package com.example.jobadd.models

data class Transactions (
    var transactionName: String? = null,
    var accHolderName: String? = null,
    var accNumber: String? = null,
    var branch: String? = null,
    var amount: Double? = null,
)