package com.cocido.formokaizen.data.repository

import com.cocido.formokaizen.domain.entities.Category
import com.cocido.formokaizen.domain.entities.WorkArea
import com.cocido.formokaizen.domain.repository.CategoriesRepository
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    // private val categoriesApi: CategoriesApi,
    // private val categoriesDao: CategoriesDao
) : CategoriesRepository {

    override suspend fun getCategories(): Flow<Resource<List<Category>>> {
        // Devolver categorías ficticias para testing
        val categories = listOf(
            Category(1, "Seguridad", "Problemas de seguridad", "#FF0000", "security", true, emptyList(), null, "", 0, 0),
            Category(2, "Calidad", "Problemas de calidad", "#00FF00", "quality", true, emptyList(), null, "", 0, 0),
            Category(3, "Mantenimiento", "Problemas de mantenimiento", "#0000FF", "maintenance", true, emptyList(), null, "", 0, 0)
        )
        return flowOf(Resource.Success(categories))
    }

    override suspend fun getAllCategories(): Flow<Resource<List<Category>>> {
        return getCategories() // Alias
    }

    override suspend fun getCategoryById(id: Int): Flow<Resource<Category>> {
        val category = Category(id, "Categoria $id", "Descripcion", "#FF0000", "icon", true, emptyList(), null, "", 0, 0)
        return flowOf(Resource.Success(category))
    }

    override suspend fun createCategory(category: Category): Flow<Resource<Category>> {
        return flowOf(Resource.Success(category))
    }

    override suspend fun updateCategory(category: Category): Flow<Resource<Category>> {
        return flowOf(Resource.Success(category))
    }

    override suspend fun deleteCategory(id: Int): Flow<Resource<Unit>> {
        return flowOf(Resource.Success(Unit))
    }

    override suspend fun getWorkAreas(categoryId: Int): Flow<Resource<List<WorkArea>>> {
        return flowOf(Resource.Success(emptyList()))
    }

    override suspend fun createWorkArea(categoryId: Int, workArea: WorkArea): Flow<Resource<WorkArea>> {
        return flowOf(Resource.Success(workArea))
    }

    override fun getCategoriesOffline(): Flow<List<Category>> {
        return flowOf(emptyList())
    }

    override suspend fun syncCategories(): Flow<Resource<Unit>> {
        return flowOf(Resource.Success(Unit))
    }
}