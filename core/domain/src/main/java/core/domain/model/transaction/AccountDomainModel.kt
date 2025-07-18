package core.domain.model.transaction

data class AccountDomainModel(
    val balance: String,
    val currency: String,
    val id: Int,
    val name: String,
    val userId: Int?,
    val createdAt: String?,
    val updatedAt: String?
)
