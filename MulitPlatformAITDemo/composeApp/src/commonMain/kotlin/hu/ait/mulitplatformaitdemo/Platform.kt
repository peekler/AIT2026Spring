package hu.ait.mulitplatformaitdemo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform