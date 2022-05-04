package cz.vratislavjindra.alzacasestudy.feature_categories.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.model.Category

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