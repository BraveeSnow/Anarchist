package world.snows.anarchist.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import world.snows.anarchist.R
import world.snows.anarchist.SearchResultQuery

@Composable
@ExperimentalMaterial3Api
fun TitleEntry(pageData: SearchResultQuery.Medium, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row {
            AsyncImage(
                model = pageData.coverImage?.large,
                contentDescription = "${stringResource(R.string.result_a11y_image)} ${pageData.title?.english}"
            )
            Text(
                pageData.title?.english ?: pageData.title?.romaji!!,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}