package world.snows.anarchist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import world.snows.anarchist.ui.route.BrowseScreen
import world.snows.anarchist.ui.route.browseGraph
import world.snows.anarchist.ui.route.searchRoute
import world.snows.anarchist.ui.theme.AnarchistTheme

class MainActivity : ComponentActivity() {
    @ExperimentalMaterial3Api
    @ExperimentalLayoutApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnarchistTheme {
                AnarchistMainPage()
            }
        }
    }
}

@Composable
@Preview
@ExperimentalMaterial3Api
@ExperimentalLayoutApi
fun AnarchistMainPage() {
    val navigator = rememberNavController()
    val backStack by navigator.currentBackStackEntryAsState()
    var currentRoute by remember {
        mutableIntStateOf(MainRoute.Feed.ordinal)
    }
    var navbarVisible by remember {
        mutableStateOf(true)
    }

    navbarVisible = when (backStack?.destination?.route) {
        searchRoute -> false
        else -> true
    }

    return Scaffold(bottomBar = {
        if (navbarVisible) {
            NavigationBar {
                MainRoute.entries.forEach { entry ->
                    NavigationBarItem(selected = entry.ordinal == currentRoute,
                        onClick = {
                            currentRoute = entry.ordinal
                            navigator.navigate(entry.route) {
                                popUpTo(navigator.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(painterResource(id = entry.iconId), entry.name) },
                        label = { Text(text = stringResource(id = entry.id)) })
                }
            }
        }
    }) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            NavHost(navController = navigator, startDestination = MainRoute.Feed.route) {
                composable(MainRoute.Feed.route) { }
                browseGraph(navigator)
            }
        }
    }
}