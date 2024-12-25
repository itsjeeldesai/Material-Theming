package com.ui.vehiclerenting.presentation.screens

import android.app.Application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.ui.vehiclerenting.R
import com.ui.vehiclerenting.domain.GetCarDetailsUseCase
import com.ui.vehiclerenting.domain.GetCategoryUseCase
import com.ui.vehiclerenting.model.CarDetails
import com.ui.vehiclerenting.model.CarDetailsRepositoryImpl
import com.ui.vehiclerenting.model.Category
import com.ui.vehiclerenting.model.CategoryRepositoryImpl
import com.ui.vehiclerenting.model.DataSource
import com.ui.vehiclerenting.presentation.viewmodels.AppSettingsViewModel
import com.ui.vehiclerenting.presentation.viewmodels.CategoryViewModel
import com.ui.vehiclerenting.presentation.viewmodels.ViewModelFactory
import com.ui.vehiclerenting.ui.theme.grayColorScheme
import com.ui.vehiclerenting.ui.theme.useAppColors

@Composable
fun HomeScreen(
    viewModel: AppSettingsViewModel,
    navController: NavHostController,
    paddingValues: PaddingValues
) {

    val useDynamicColor by viewModel.useDynamicColor
    val context = LocalContext.current
    val categoryViewModel: CategoryViewModel = viewModel(
        factory = ViewModelFactory(
            application = context.applicationContext as Application,
            getCategoryUseCase = GetCategoryUseCase(CategoryRepositoryImpl(DataSource())),
            getCarDetailsUseCase = GetCarDetailsUseCase(CarDetailsRepositoryImpl(DataSource()))
        )
    )
    val categories by categoryViewModel.categories.collectAsState()
    val selectedCategoryIndex by categoryViewModel.selectedCategoryIndex.collectAsState()
    val filteredCarDetails = categoryViewModel.getFilteredCarDetails()
    val scrollState = rememberLazyListState()
    val showTopRow by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex == 0 && scrollState.firstVisibleItemScrollOffset == 0
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .clip(RoundedCornerShape(bottomStartPercent = 5, bottomEndPercent = 5))
            .background(
                useAppColors(
                    grayColors = grayColorScheme().surface,
                    MaterialTheme.colorScheme.surface
                )
            )
    ) {
        AnimatedVisibility(
            visible = showTopRow,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {

            @Composable
            fun SearchBarRow() {
                var query by rememberSaveable {
                    mutableStateOf("")
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {

                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = "search"
                            )
                        },
                        placeholder = {
                            Text(
                                text = "City, airport, address or hotel etc.",
                                fontSize = 14.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        },
                        shape = RoundedCornerShape(45),
                        colors = useAppColors(
                            grayColors = TextFieldDefaults.colors(
                                unfocusedContainerColor = grayColorScheme().background,
                                focusedContainerColor = grayColorScheme().background,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            TextFieldDefaults.colors(
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .weight(4f)
                .border(1.dp, Color.Gray, RoundedCornerShape(45))
                    )

                    Button(
                        onClick = {
                            viewModel.toggleDynamicColor()
                        },
                        modifier = Modifier.size(56.dp),
                        shape = CircleShape,
            border = BorderStroke(1.dp, Color.Gray),
                        colors = if (useDynamicColor) {
                            useAppColors(
                                grayColors = ButtonDefaults.buttonColors(
                                    containerColor = grayColorScheme().secondary,
                                    contentColor = grayColorScheme().onSecondary
                                ),
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary,
                                )
                            )
                        } else useAppColors(
                            grayColors = ButtonDefaults.buttonColors(
                                containerColor = grayColorScheme().background,
                                contentColor = grayColorScheme().inverseSurface
                            ),
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        ),
                        contentPadding = PaddingValues(0.dp),  //avoid the little icon
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.notification),
                            contentDescription = "notification"
                        )
                    }

                }
            }

            @Composable
            fun ProfileRow() {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(4f)
                    ) {
                        Text(text = "Hii, Joseph", color = MaterialTheme.colorScheme.secondary)
                        Text(
                            text = "Find Your Ideal Drive",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.key_handover),
                        contentDescription = "key handover",
                        modifier = Modifier.weight(2f)
                    )

                }
            }


            Column {
                SearchBarRow()
                ProfileRow()
            }
        }

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun CategoryChip(
            categories: List<Category>,
            selectedCategoryIndex: Int?,
            onCategorySelected: (Int) -> Unit
        ) {
            LazyRow {
                itemsIndexed(categories) { index, category ->
                    FilterChip(
                        selected = category.index == selectedCategoryIndex,
                        onClick = { onCategorySelected(category.index) },
                        label = {
                            Text(
                                text = category.name,
                            )
                        },
                        shape = RoundedCornerShape(32),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = when (index) {
                            0 -> {
                                Modifier.padding(start = 16.dp, end = 3.dp)
                            }

                            categories.lastIndex -> {
                                Modifier.padding(end = 16.dp, start = 3.dp)
                            }

                            else -> {
                                Modifier.padding(horizontal = 3.dp)
                            }
                        }
                    )
                }
            }
        }


        CategoryChip(
            categories = categories,
            selectedCategoryIndex = selectedCategoryIndex,
            onCategorySelected = categoryViewModel::onCategorySelected
        )

        if (filteredCarDetails.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        useAppColors(
                            grayColors = grayColorScheme().surface,
                            MaterialTheme.colorScheme.surface
                        )
                    )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "There are currently no cars available in this category.",
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {

            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        useAppColors(
                            grayColors = grayColorScheme().surface,
                            MaterialTheme.colorScheme.surface
                        )
                    )
            ) {
                itemsIndexed(filteredCarDetails) { index, carDetails ->

                    CarCard(
                        navController = navController,
                        carDetails = carDetails,
                        index = index
                    )
                }
            }
        }
    }
}

@Composable
fun CarCard(
    navController: NavHostController,
    carDetails: CarDetails,
    index: Int
) {

    Card(
        shape = RoundedCornerShape(5),
        colors = useAppColors(
            grayColors = CardDefaults.cardColors(
                containerColor = grayColorScheme().surfaceVariant
            ),
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(5))
            .clickable { navController.navigate("car_detail/$index") }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    .clip(RoundedCornerShape(topStartPercent = 5, topEndPercent = 5))
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                SubcomposeAsyncImage(
                    model = carDetails.imageResource,
                    loading = {
                        AsyncImage(
                            model = R.drawable.loading_car,
                            contentDescription = "loading image",
                            contentScale = ContentScale.Inside
                        )
                    },
                    error = {
                        AsyncImage(
                            model = R.drawable.error_car,
                            contentDescription = "error image",
                            contentScale = ContentScale.Inside
                        )
                    },
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(30))
                        .background(
                            useAppColors(
                                grayColors = grayColorScheme().background,
                                MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = carDetails.validityText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(30))
                        .background(
                            useAppColors(
                                grayColors = grayColorScheme().background,
                                MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                        .align(Alignment.TopEnd)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = "star",
                            tint = useAppColors(
                                grayColors = grayColorScheme().scrim,
                                MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(
                            text = carDetails.ratingText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(30))
                        .background(
                            useAppColors(
                                grayColors = grayColorScheme().background,
                                MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                        .align(Alignment.BottomEnd)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.favourite),
                        contentDescription = "favourite",
                        tint = useAppColors(
                            grayColors = grayColorScheme().error,
                            MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = carDetails.nameText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoChip(
                        icon = R.drawable.trips,
                        text = "${carDetails.tripsText} trips"
                    )
                    InfoChip(
                        icon = R.drawable.map,
                        text = carDetails.locationText,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = carDetails.priceText,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(36)
                    ) {
                        Text(
                            text = "Book Now",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoChip(
    icon: Int, text: String, chipColor: Color = useAppColors(
        grayColors = grayColorScheme().surface,
        defaultColors = MaterialTheme.colorScheme.background
    ), iconColor: Color = useAppColors(
        grayColors = grayColorScheme().primary,
        defaultColors = MaterialTheme.colorScheme.primary
    )
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(chipColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = text,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}