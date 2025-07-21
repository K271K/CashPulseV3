package core.data.repository

import android.util.Log
import core.data.local.dao.CategoryDao
import core.data.local.entity.CategoryEntity
import core.data.remote.connection.ConnectivityObserver
import core.data.remote.retrofit.RemoteDataSource
import core.domain.model.category.CategoryDomainModel
import core.domain.repository.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val connectivityObserver: ConnectivityObserver,
    private val categoryDao: CategoryDao
) : CategoriesRepository {

    override suspend fun getAllCategories(): List<CategoryDomainModel> =
        withContext(Dispatchers.IO) {

            // Если есть интернет - проверяем новые категории с сервера
            if (connectivityObserver.isCurrentlyConnected()) {
                try {
                    val remoteCategories = remoteDataSource.getAllCategories()
                    Log.d(
                        "CategoriesRepositoryImpl",
                        "Категории с сервера: ${remoteCategories.map { it.id }}"
                    )

                    // Проверяем какие категории новые
                    val existingCategoryIds = categoryDao.getAllCategories().map { it.id }.toSet()
                    val newCategories = remoteCategories.filter { it.id !in existingCategoryIds }

                    if (newCategories.isNotEmpty()) {
                        Log.d(
                            "CategoriesRepositoryImpl",
                            "Найдено новых категорий: ${newCategories.size}"
                        )

                        val newCategoryEntities = newCategories.map { category ->
                            CategoryEntity(
                                id = category.id,
                                name = category.name,
                                emoji = category.emoji,
                                isIncome = category.isIncome
                            )
                        }
                        categoryDao.insertAll(newCategoryEntities)
                    } else {
                        Log.d("CategoriesRepositoryImpl", "Новых категорий нет")
                    }

                } catch (e: Exception) {
                    Log.e(
                        "CategoriesRepositoryImpl",
                        "Ошибка синхронизации категорий: ${e.message}"
                    )
                }
            } else {
                Log.d("CategoriesRepositoryImpl", "Нет интернета, работаем с локальными данными")
            }

            // Всегда возвращаем данные из локальной БД
            return@withContext getCategoriesFromLocalDb()
    }

    override suspend fun getCategoriesByType(isIncome: Boolean): List<CategoryDomainModel> =
        withContext(Dispatchers.IO) {

            // Если есть интернет - проверяем новые категории с сервера
            if (connectivityObserver.isCurrentlyConnected()) {
                try {
                    val remoteCategories = remoteDataSource.getCategoriesByType(isIncome = isIncome)
                    Log.d(
                        "CategoriesRepositoryImpl",
                        "Категории по типу с сервера: ${remoteCategories.map { it.id }}"
                    )

                    // Проверяем какие категории новые
                    val existingCategoryIds = categoryDao.getAllCategories().map { it.id }.toSet()
                    val newCategories = remoteCategories.filter { it.id !in existingCategoryIds }

                    if (newCategories.isNotEmpty()) {
                        Log.d(
                            "CategoriesRepositoryImpl",
                            "Найдено новых категорий по типу: ${newCategories.size}"
                        )

                        val newCategoryEntities = newCategories.map { category ->
                            CategoryEntity(
                                id = category.id,
                                name = category.name,
                                emoji = category.emoji,
                                isIncome = category.isIncome
                            )
                        }
                        categoryDao.insertAll(newCategoryEntities)
                    } else {
                        Log.d("CategoriesRepositoryImpl", "Новых категорий по типу нет")
                    }

                } catch (e: Exception) {
                    Log.e(
                        "CategoriesRepositoryImpl",
                        "Ошибка синхронизации категорий по типу: ${e.message}"
                    )
                }
            } else {
                Log.d("CategoriesRepositoryImpl", "Нет интернета, работаем с локальными данными")
            }

            // Всегда возвращаем данные из локальной БД
            return@withContext getCategoriesFromLocalDb(isIncome)
    }

    private suspend fun getCategoriesFromLocalDb(): List<CategoryDomainModel> {
        val entities = categoryDao.getAllCategories()
        return entities.map { entity ->
            CategoryDomainModel(
                id = entity.id,
                name = entity.name,
                emoji = entity.emoji,
                isIncome = entity.isIncome
            )
        }
    }

    private suspend fun getCategoriesFromLocalDb(isIncome: Boolean): List<CategoryDomainModel> {
        val entities = categoryDao.getAllCategories().filter { it.isIncome == isIncome }
        return entities.map { entity ->
            CategoryDomainModel(
                id = entity.id,
                name = entity.name,
                emoji = entity.emoji,
                isIncome = entity.isIncome
            )
        }
    }
}