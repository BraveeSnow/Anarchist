package world.snows.anarchist.anilist

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import world.snows.anarchist.SearchResultQuery
import world.snows.anarchist.anilist.type.Genre
import world.snows.anarchist.type.MediaType

const val ANILIST_ENDPOINT = "https://graphql.anilist.co/"

class GraphQLClient {
    private var client = ApolloClient.Builder()
        .serverUrl(ANILIST_ENDPOINT)
        .build()

    suspend fun searchTitle(
        title: String,
        type: MediaType,
        genreFilter: Optional<List<Genre>>
    ): ApolloResponse<SearchResultQuery.Data> {
        return client.query(
            SearchResultQuery(
                Optional.present(title),
                Optional.present(type),
                Optional.presentIfNotNull(genreFilter.getOrNull()?.map { genre -> genre.queryName })
            )
        ).execute()
    }
}