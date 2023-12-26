package world.snows.anarchist

enum class MainRoute(val id: Int, val iconId: Int, val route: String) {
    Feed(R.string.nav_feed, R.drawable.feed, "feed"),
    Anime(R.string.nav_anime, R.drawable.play, "anime"),
    Manga(R.string.nav_manga, R.drawable.book, "manga"),
    Search(R.string.nav_search, R.drawable.search, "search"),
    Profile(R.string.nav_profile, R.drawable.account, "profile")
}