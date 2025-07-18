package core.data.repository

import core.data.remote.retrofit.RemoteDataSource
import core.domain.model.category.CategoryDomainModel
import core.domain.repository.CategoriesRepository
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : CategoriesRepository {
    override suspend fun getAllCategories(): List<CategoryDomainModel> {
        return remoteDataSource.getAllCategories()
    }

    override suspend fun getCategoriesByType(isIncome: Boolean): List<CategoryDomainModel> {
        return remoteDataSource.getCategoriesByType(isIncome = isIncome)
    }
}