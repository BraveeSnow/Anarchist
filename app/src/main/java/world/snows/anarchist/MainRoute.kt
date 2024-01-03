package world.snows.anarchist

enum class MainRoute(val id: Int, val iconId: Int, val route: String) {
    Feed(R.string.nav_feed, R.drawable.feed, "feed"),
    Anime(R.string.nav_anime, R.drawable.play, "anime"),
    Manga(R.string.nav_manga, R.drawable.book, "manga"),
    Browse(R.string.nav_browse, R.drawable.browse, "browse"),
    Profile(R.string.nav_profile, R.drawable.account, "profile")
}