package com.ui.vehiclerenting.presentation.screens

import android.app.Application
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ui.vehiclerenting.R
import com.ui.vehiclerenting.domain.GetCarDetailsUseCase
import com.ui.vehiclerenting.domain.GetCategoryUseCase
import com.ui.vehiclerenting.model.CarDetails
import com.ui.vehiclerenting.model.CarDetailsRepositoryImpl
import com.ui.vehiclerenting.model.CategoryRepositoryImpl
import com.ui.vehiclerenting.model.DataSource
import com.ui.vehiclerenting.model.Review
import com.ui.vehiclerenting.presentation.viewmodels.CategoryViewModel
import com.ui.vehiclerenting.presentation.viewmodels.ViewModelFactory
import kotlin.math.max

@Composable
fun CarDetailScreen(
    paddingValues: PaddingValues,
    carIndex: Int
) {
    val viewModel: CategoryViewModel = viewModel(
        factory = ViewModelFactory(
            application = LocalContext.current.applicationContext as Application,
            getCategoryUseCase = GetCategoryUseCase(CategoryRepositoryImpl(DataSource())),
            getCarDetailsUseCase = GetCarDetailsUseCase(CarDetailsRepositoryImpl(DataSource()))
        )
    )
    val carDetails by viewModel.carDetails.collectAsState()
    val selectedCar = carDetails.getOrNull(carIndex) ?: return

    val scrollState = rememberScrollState()
    val imageHeight = 240.dp
    val minImageHeight = 0.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(imageHeight))
            CarDetailsContent(selectedCar)
        }

        // Image
        val imageHeightPx = with(LocalDensity.current) { imageHeight.toPx() }
        val minImageHeightPx = with(LocalDensity.current) { minImageHeight.toPx() }
        val pictureHeight by animateFloatAsState(
            targetValue = (imageHeightPx - scrollState.value).coerceIn(
                minImageHeightPx,
                imageHeightPx
            ),
            animationSpec = spring(), label = "image animation"
        )

        CarImagesCarousel(
            selectedCar.imageResource,
            modifier = Modifier
                .height(with(LocalDensity.current) { pictureHeight.toDp() })
                .fillMaxWidth()
        )
    }
}

@Composable
fun CarDetailsContent(car: CarDetails) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        CarHeader(car)
        Spacer(modifier = Modifier.height(8.dp))
        SpecificationsCard()
        Spacer(modifier = Modifier.height(8.dp))
        OwnerInfoCard()
        Spacer(modifier = Modifier.height(8.dp))
        TravelDateRangeCard()
        Spacer(modifier = Modifier.height(8.dp))
        TripLocationCard()
        Spacer(modifier = Modifier.height(8.dp))
        CancellationCard()
        Spacer(modifier = Modifier.height(8.dp))
        RatingsAndReviewsCard(
            overallRating = 4.5f,
            totalReviews = 87,
            categoryRatings = mapOf(
                "Cleanliness" to 4.7f,
                "Maintenance" to 4.0f,
                "Communication" to 5.0f,
                "Convenience" to 4.5f,
                "Listing Accuracy" to 5.0f
            )
        )
    }
}

@Composable
fun CarImagesCarousel(imageResource: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        AsyncImage(
            model = imageResource,
            contentDescription = "Car Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun CarHeader(car: CarDetails) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = car.nameText,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            lineHeight = 28.sp,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(4f)
        )
        RatingBox(car.ratingText, MaterialTheme.colorScheme.secondaryContainer)
    }
}

@Composable
fun RatingBox(rating: String, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(36))
            .background(backgroundColor)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.star),
                contentDescription = "star",
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(16.dp)
            )
            Text(
                text = rating,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun SpecificationsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                RoundedCornerShape(12)
            )
            .clip(RoundedCornerShape(12)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Text(
                text = "Specifications",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
            SpecificationRow(
                { PlacesIndicator(R.drawable.seat, "Places", "4 Seats") },
                { PlacesIndicator(R.drawable.speed, "Top speed", "320 km/h") }
            )
            SpecificationRow(
                { PlacesIndicator(R.drawable.transmission, "Transmission", "Automatic") },
                { PlacesIndicator(R.drawable.fuel, "Fuel type", "Petrol") }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun SpecificationRow(indicator1: @Composable () -> Unit, indicator2: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier.weight(1f)) { indicator1() }
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier.weight(1f)) { indicator2() }
    }
}

@Composable
fun PlacesIndicator(
    icon: Int,
    heading: String,
    body: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(50))
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = heading,
                fontSize = 12.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = body,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
fun OwnerInfoCard() {
    Layout(
        content = {
            OwnerDetailsCard(Modifier.layoutId("details"))
            MapCard(Modifier.layoutId("map"))
        },
        modifier = Modifier.fillMaxWidth()
    ) { measurable, constraints ->
        val spacerWidth = 8.dp.roundToPx()
        val availableWidth = constraints.maxWidth - spacerWidth

        val detailsWidth = (availableWidth * 0.63f).toInt()
        val mapWidth = availableWidth - detailsWidth

        val detailsPlaceable = measurable.first { it.layoutId == "details" }
            .measure(Constraints(maxWidth = detailsWidth, maxHeight = Constraints.Infinity))
        val mapPlaceable = measurable.first { it.layoutId == "map" }
            .measure(Constraints(maxWidth = mapWidth, maxHeight = Constraints.Infinity))

        val height = max(detailsPlaceable.height, mapPlaceable.height)

        layout(constraints.maxWidth, height) {
            detailsPlaceable.place(0, 0)
            mapPlaceable.place(detailsWidth + spacerWidth, 0)
        }
    }
}

@Composable
fun OwnerDetailsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                RoundedCornerShape(12)
            )
            .clip(RoundedCornerShape(12)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(text = "Owner Info.")
            Spacer(modifier = Modifier.height(8.dp))
            OwnerInfo()
            Spacer(modifier = Modifier.height(8.dp))
            OwnerStats()
        }
    }
}

@Composable
fun OwnerInfo() {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Image(
            painter = painterResource(id = R.drawable.owner),
            contentDescription = "Owner",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "John Smith",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.medal_star),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Star host",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun InfoChipDetails(icon: Int, text: String, chipColor: Color) {
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
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
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

@Composable
fun OwnerStats() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        InfoChipDetails(
            icon = R.drawable.destination,
            text = "91 trips",
            MaterialTheme.colorScheme.secondaryContainer
        )
        Spacer(modifier = Modifier.width(4.dp))
        InfoChipDetails(
            icon = R.drawable.location,
            text = "San Francisco",
            MaterialTheme.colorScheme.secondaryContainer
        )
    }
}

@Composable
fun MapCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                RoundedCornerShape(12)
            )
            .clip(RoundedCornerShape(12)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier.aspectRatio(1f),  // This ensures the map is always square
            contentAlignment = Alignment.TopCenter
        ) {
            // Original Image
            Image(
                painter = painterResource(id = R.drawable.map_image),
                contentDescription = "Map image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            // Text overlay
            Text(
                text = "Car Location",
                modifier = Modifier
                    .padding(8.dp)
                    .padding(4.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun TravelDateRangeCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                RoundedCornerShape(12)
            ),
        shape = RoundedCornerShape(12),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Trip Date & Timing",
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DateTimeColumn(
                    label = "From",
                    date = "18th July, 2024",
                    time = "10:00 AM",
                    modifier = Modifier.weight(1f)
                )
                DateDivider()
                DateTimeColumn(
                    label = "To",
                    date = "22nd July, 2024",
                    time = "05:30 PM",
                    modifier = Modifier.weight(1f),
                    alignment = Alignment.End
                )
            }
        }
    }
}

@Composable
private fun DateTimeColumn(
    label: String,
    date: String,
    time: String,
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start
) {
    Column(
        modifier = modifier,
        horizontalAlignment = alignment
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = date,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = time,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
        )
    }
}

@Composable
private fun DateDivider() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .height(60.dp)
                .width(1.dp)
        )
        Icon(
            imageVector = Icons.Rounded.DateRange,
            tint = MaterialTheme.colorScheme.surface,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.secondary, CircleShape)
                .padding(8.dp)
        )
    }
}

@Composable
fun TripLocationCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(20))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Trip Location",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                )

                Text(
                    text = "West Men lo Park, Random Street, Random Town, CA 83265, USA",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun CancellationCard() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(20))
            .clip(RoundedCornerShape(32))
    ) {

        Box(Modifier.padding(16.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.cancellation),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        CircleShape
                    )
                    .padding(8.dp)

            )
        }

        Column {
            Text(
                text = "FREE Cancellation",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Full refund before 17th July, 10:00 AM",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

    }
}

@Composable
fun RatingsAndReviewsCard(
    overallRating: Float,
    totalReviews: Int,
    categoryRatings: Map<String, Float>
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondaryContainer),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Text(
                text = "Ratings & Reviews",
            )

            RatingsHeader(
                overallRating = overallRating,
                totalReviews = totalReviews,
                isExpanded = isExpanded,
                onExpandClick = { isExpanded = !isExpanded }
            )
            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                CategoryRatings(categoryRatings)
                Spacer(modifier = Modifier.height(16.dp))
                ClientReviews()
            }
        }
    }
}

@Composable
fun RatingsHeader(
    overallRating: Float,
    totalReviews: Int,
    isExpanded: Boolean,
    onExpandClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(32))
                .padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.star),
                contentDescription = null,
                tint = Color(0xFFFFB532),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$overallRating ($totalReviews Reviews)",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
        IconButton(onClick = onExpandClick) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Collapse" else "Expand"
            )
        }
    }
}

@Composable
fun CategoryRatings(categoryRatings: Map<String, Float>) {
    Column {
        categoryRatings.forEach { (category, rating) ->
            CategoryRatingItem(category, rating)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun CategoryRatingItem(category: String, rating: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = category,
            modifier = Modifier.width(120.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.width(8.dp))

        RatingBar(
            rating = rating,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = String.format("%.1f", rating),
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun RatingBar(rating: Float, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        repeat(5) { index ->
            Pill(
                fillPercentage = (rating - index).coerceIn(0f, 1f),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 2.dp)
            )
        }
    }
}

@Composable
fun Pill(fillPercentage: Float, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.LightGray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fillPercentage)
                .background(Color(0xFFFFB532))
        )
    }
}

@Composable
fun ClientReviews() {
    val fakeReviews = listOf(
        Review(
            "David Harris",
            "May 28, 2023",
            4.9f,
            "John was absolutely great in communicating & vehicle is super reliable. Totally will book again in the future.",
            "https://media.assettype.com/esakal%2F2023-05%2F1666f23e-85d9-4cbe-9889-981636615514%2F1.jpg"
        ),
        Review(
            "Sarah Johnson",
            "June 15, 2023",
            4.7f,
            "Great experience! The car was clean and in perfect condition.",
            "https://media.assettype.com/esakal%2F2023-05%2F1666f23e-85d9-4cbe-9889-981636615514%2F1.jpg"
        ),
        Review(
            "Michael Brown",
            "July 3, 2023",
            4.8f,
            "Smooth rental process and excellent communication.",
            "https://media.assettype.com/esakal%2F2023-05%2F1666f23e-85d9-4cbe-9889-981636615514%2F1.jpg"
        ),
        Review(
            "Emily Davis",
            "July 20, 2023",
            4.6f,
            "The car was fantastic for our trip. Would definitely rent again!",
            "https://media.assettype.com/esakal%2F2023-05%2F1666f23e-85d9-4cbe-9889-981636615514%2F1.jpg"
        )
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(
                text = "Client Reviews",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(fakeReviews.size) { index ->
                    ReviewCard(review = fakeReviews[index])
                }
            }
            Spacer(modifier = Modifier.height(45.dp))
        }

        Text(
            text = "See all reviews",
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .clip(CircleShape)
                .clickable {}
                .padding(8.dp)
        )
    }
}

@Composable
fun ReviewCard(review: Review) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(180.dp)
            .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = review.imageUrl,
                    contentDescription = "Reviewer photo",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = review.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = review.date,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                RatingBox(rating = review.rating.toString(), MaterialTheme.colorScheme.surface)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}