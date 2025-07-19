package feature.account.presentation.screens.account_edit

data class AccountEditScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false,
    val id: Int = 0,
    val name: String = "",
    val balance: String = "",
    val currency: String = "USD",
) {
    fun getValidationErrors(): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (name.isEmpty()) {
            errors["name"] = "Введите имя"
        }
        if (balance.isBlank()) {
            errors["balance"] = "Введите баланс"
        }
        if (currency.isBlank()) {
            errors["currency"] = "Выберите валюту"
        }
        return errors
    }
}