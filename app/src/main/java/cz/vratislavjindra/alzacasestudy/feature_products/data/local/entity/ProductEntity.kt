package cz.vratislavjindra.alzacasestudy.feature_products.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: String?,
    val availability: String,
    val canBuy: Boolean,
    val rating: Float,
    val order: Int,
    val categoryId: Int
) {

    fun toProduct(): Product {
        return Product(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl,
            price = price,
            availability = availability,
            canBuy = canBuy,
            rating = rating
        )
    }
}