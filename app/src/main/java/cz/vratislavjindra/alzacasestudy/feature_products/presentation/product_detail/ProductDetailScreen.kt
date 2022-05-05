package cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_detail

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ProductDetailScreen(
    navController: NavController,
    viewModel: ProductDetailViewModel = hiltViewModel(),
    productId: Int?,
    productName: String?
) {

}