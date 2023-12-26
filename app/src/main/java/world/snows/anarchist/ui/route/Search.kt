package world.snows.anarchist.ui.route

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import world.snows.anarchist.R

enum class SearchType {
    Anime,
    Manga
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {
    var queryText by remember {
        mutableStateOf("")
    }
    var active by remember {
        mutableStateOf(false)
    }
    var searchBy by remember {
        mutableStateOf(SearchType.Anime)
    }

    Scaffold(topBar = {
        Column {
            SearchBar(
                query = queryText,
                onQueryChange = { queryText = it },
                onSearch = {
                    active = false
                },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                leadingIcon = { Icon(painterResource(R.drawable.search), "Search box icon") },
                trailingIcon = {
                    if (active && queryText.isNotEmpty()) {
                        Icon(painterResource(R.drawable.close),
                            "Remove search query",
                            modifier = Modifier.clickable {
                                queryText = "";
                            })
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { }
            TabRow(selectedTabIndex = searchBy.ordinal) {
                Tab(
                    selected = searchBy == SearchType.Anime,
                    onClick = { searchBy = SearchType.Anime }) {
                    Text(stringResource(R.string.tab_anime), modifier = Modifier.padding(16.dp))
                }
                Tab(
                    selected = searchBy == SearchType.Manga,
                    onClick = { searchBy = SearchType.Manga }) {
                    Text(stringResource(R.string.tab_manga), modifier = Modifier.padding(16.dp))
                }
            }
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {

        }
    }
}