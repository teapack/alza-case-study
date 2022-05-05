package cz.vratislavjindra.alzacasestudy.feature_products.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String?
) {

    fun toCategory(): Category {
        return Category(
            id = id,
            name = name,
            imageUrl = imageUrl
        )
    }
}