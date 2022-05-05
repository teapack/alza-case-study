package cz.vratislavjindra.alzacasestudy.feature_products.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.ProductOverview

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val categoryId: Int
) {

    fun toProduct(): Product {
        return Product(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl
        )
    }

    fun toProductOverview(): ProductOverview {
        return ProductOverview(
            id = id,
            name = name
        )
    }
}