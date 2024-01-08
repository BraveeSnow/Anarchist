package world.snows.anarchist.ui.route

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import coil.compose.AsyncImage
import com.apollographql.apollo3.api.Optional
import world.snows.anarchist.MainRoute
import world.snows.anarchist.R
import world.snows.anarchist.SearchResultQuery
import world.snows.anarchist.anilist.GraphQLClient
import world.snows.anarchist.anilist.type.Genre
import world.snows.anarchist.type.MediaType
import world.snows.anarchist.ui.component.Title
import world.snows.anarchist.ui.component.TitleEntry
import world.snows.anarchist.ui.theme.DarkSuccessGreen
import world.snows.anarchist.ui.theme.LightSuccessGreen
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

const val browseRoute = "browseCommon"
const val searchRoute = "search"
const val resultRoute = "result"

@ExperimentalMaterial3Api
@ExperimentalEncodingApi
@ExperimentalLayoutApi
fun NavGraphBuilder.browseGraph(navigator: NavHostController) {
    navigation(startDestination = browseRoute, route = MainRoute.Browse.route) {
        composable(browseRoute) {
            BrowseScreen(navigator)
        }
        composable(searchRoute) {
            SearchScreen(navigator)
        }
        composable(
            "$resultRoute/{query}/{type}/{genres}", arguments = listOf(navArgument("query") {
                type = NavType.StringType
            }, navArgument("type") {
                type = NavType.StringType
            }, navArgument("genres") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val args = backStackEntry.arguments
            val genreFilters = args?.getInt("genres")!!
            ResultScreen(
                navigator = navigator,
                query = Base64.decode(args.getString("query")!!).decodeToString(),
                type = args.getString("type")!!,
                genres = List(Genre.entries.size) { i -> (genreFilters and (1 shl i)) != 0 })
        }
    }
}

@Composable
@ExperimentalMaterial3Api
fun BrowseScreen(navigator: NavHostController) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(stringResource(R.string.browse_title)) }, actions = {
            IconButton(onClick = { navigator.navigate(searchRoute) }) {
                Icon(
                    painter = painterResource(R.drawable.search),
                    contentDescription = "Search button"
                )
            }
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {}
    }
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalEncodingApi
@ExperimentalLayoutApi
fun SearchScreen(navigator: NavHostController) {
    var searchQuery by remember {
        mutableStateOf("")
    }
    var mediaType by remember {
        mutableStateOf(true)
    }
    var genreSelection by remember {
        mutableIntStateOf(0)
    }
    var searchActive by remember {
        mutableStateOf(false)
    }

    Scaffold(topBar = {
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = {
                searchActive = false
                navigator.navigate(
                    "$resultRoute/${
                        Base64.encode(
                            it.toByteArray()
                        )
                    }/${if (mediaType) "ANIME" else "MANGA"}/$genreSelection"
                )
            },
            active = searchActive,
            onActiveChange = { searchActive = !searchActive },
            leadingIcon = { Icon(painterResource(R.drawable.search), "Search bar icon") },
            modifier = Modifier.fillMaxWidth()
        ) {}
    }, bottomBar = {
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
            LargeFloatingActionButton(
                onClick = {
                    navigator.navigate(
                        "$resultRoute/${
                            Base64.encode(
                                searchQuery.toByteArray()
                            )
                        }/${if (mediaType) "ANIME" else "MANGA"}/$genreSelection"
                    )
                }, modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    painterResource(R.drawable.search), "Search", modifier = Modifier.scale(1.5F)
                )
            }
        }

    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Title(
                stringResource(R.string.search_type), modifier = Modifier.padding(4.dp)
            )
            Row(
                modifier = Modifier.selectableGroup(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RadioButton(selected = mediaType, onClick = { mediaType = true })
                Text(stringResource(R.string.browse_anime))

                RadioButton(selected = !mediaType, onClick = { mediaType = false })
                Text(stringResource(R.string.browse_manga))

            }
            Title(
                stringResource(R.string.search_genre), modifier = Modifier.padding(4.dp)
            )
            FlowRow {
                Genre.entries.onEachIndexed { index, entry ->
                    FilterChip(selected = (genreSelection and (1 shl index)) != 0, onClick = {
                        genreSelection = genreSelection xor (1 shl index)
                    }, label = {
                        Text(
                            stringResource(entry.stringId)
                        )
                    }, leadingIcon = {
                        if ((genreSelection and (1 shl index)) != 0) {
                            Icon(painterResource(R.drawable.check), "Selected")
                        }
                    }, colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = if (isSystemInDarkTheme()) DarkSuccessGreen else LightSuccessGreen,
                        selectedLeadingIconColor = MaterialTheme.colorScheme.background,
                        selectedLabelColor = MaterialTheme.colorScheme.background
                    ), modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
fun ResultScreen(
    navigator: NavHostController, query: String, type: String, genres: List<Boolean> = emptyList()
) {
    val anilistClient by remember {
        mutableStateOf(GraphQLClient())
    }
    var result by remember {
        mutableStateOf(SearchResultQuery.Data(null))
    }
    var bottomSheetVisible by remember {
        mutableStateOf(false)
    }
    var sheetTitleData by remember {
        mutableStateOf<SearchResultQuery.Medium?>(null)
    }

    LaunchedEffect(query, type, genres) {
        val filteredGenres = Genre.entries.filterIndexed { index, _ -> genres[index] }
        result = anilistClient.searchTitle(
            query,
            if (type == MediaType.ANIME.rawValue) MediaType.ANIME else MediaType.MANGA,
            Optional.presentIfNotNull(filteredGenres.ifEmpty { null })
        ).dataAssertNoErrors
    }

    if (result.Page == null) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else if (result.Page?.media?.isEmpty() == true) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painterResource(R.drawable.error),
                stringResource(R.string.result_none_found),
                modifier = Modifier
                    .scale(1.5F)
                    .padding(16.dp)
            )
            Text(stringResource(R.string.result_none_found))
        }
    } else {
        Scaffold(topBar = {
            TopAppBar(
                title = { Text(query) },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(painterResource(R.drawable.back), null)
                    }
                })
        }) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                result.Page?.media?.forEach { entry ->
                    if (entry != null) {
                        TitleEntry(entry) {
                            sheetTitleData = entry
                            bottomSheetVisible = true
                        }
                    }
                }
            }
        }


        if (bottomSheetVisible) {
            ModalBottomSheet(onDismissRequest = { bottomSheetVisible = false }) {
                AsyncImage(
                    sheetTitleData?.bannerImage,
                    null,
                    contentScale = ContentScale.FillHeight,
                    alignment = Alignment.Center,
                    modifier = Modifier.height(128.dp)
                )
                Title(sheetTitleData?.title?.english ?: sheetTitleData?.title?.romaji!!)
            }
        }
    }
}