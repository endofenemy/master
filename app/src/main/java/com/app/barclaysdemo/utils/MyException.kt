package com.app.barclaysdemo.utils

import java.io.IOException

class ApiExceptions(message: String) : IOException(message)

class NoNetworkException(message: String) : IOException(message)

