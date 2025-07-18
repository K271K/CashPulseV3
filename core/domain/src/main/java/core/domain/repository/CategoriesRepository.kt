package core.domain.repository

import core.domain.model.category.CategoryDomainModel

interface CategoriesRepository {

    suspend fun getAllCategories(): List<CategoryDomainModel>

    suspend fun getCategoriesByType(isIncome: Boolean): List<CategoryDomainModel>
}