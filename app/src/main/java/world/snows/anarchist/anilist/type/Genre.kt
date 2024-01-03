package world.snows.anarchist.anilist.type

import world.snows.anarchist.R

enum class Genre(val stringId: Int, val queryName: String) {
    Action(R.string.genre_action, "Action"),
    Adventure(R.string.genre_adventure, "Adventure"),
    Comedy(R.string.genre_comedy, "Comedy"),
    Drama(R.string.genre_drama, "Drama"),
    Ecchi(R.string.genre_ecchi, "Ecchi"),
    Fantasy(R.string.genre_fantasy, "Fantasy"),
    Horror(R.string.genre_horror, "Horror"),
    Mecha(R.string.genre_mecha, "Mecha"),
    Music(R.string.genre_music, "Music"),
    Mystery(R.string.genre_mystery, "Mystery"),
    Psychological(R.string.genre_psychological, "Psychological"),
    Romance(R.string.genre_romance, "Romance"),
    SciFi(R.string.genre_scifi, "Sci-Fi"),
    SliceOfLife(R.string.genre_sol, "Slice of Life"),
    Sports(R.string.genre_sports, "Sports"),
    Supernatural(R.string.genre_supernatural, "Supernatural"),
    Thriller(R.string.genre_thriller, "Thriller")
}