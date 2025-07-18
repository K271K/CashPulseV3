package core.ui.model

import core.domain.model.category.CategoryDomainModel

fun CategoryDomainModel.toCategoryPickerUiModel() = CategoryPickerUiModel(
    name = this.name,
    id = this.id,
    emoji = this.emoji
)

fun List<CategoryDomainModel>.toCategoryPickerUiModel() = this.map { it.toCategoryPickerUiModel() }