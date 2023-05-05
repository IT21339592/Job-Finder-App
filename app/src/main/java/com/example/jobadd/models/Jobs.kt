package com.example.jobadd.models

data class Jobs(
    var jobName: String? = null,
    var company: String? = null,
    var jobDes: String? = null,
    var startDate: String? = null,
    var budget:Double?=null)