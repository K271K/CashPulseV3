package feature.expenses.domain.usecase

import core.domain.model.category.CategoryDomainModel
import core.domain.repository.CategoriesRepository
import javax.inject.Inject

class GetExpenseCategoriesUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository
) {

    suspend operator fun invoke() : Result<List<CategoryDomainModel>> {
        return try {
            val categoriesList = categoriesRepository.getCategoriesByType(isIncome = false)
            Result.success(categoriesList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}