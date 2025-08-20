package com.cocido.formokaizen.domain.usecases.categories

import com.cocido.formokaizen.domain.entities.Category
import com.cocido.formokaizen.domain.repository.CategoriesRepository
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Category>>> {
        return categoriesRepository.getCategories()
    }
}