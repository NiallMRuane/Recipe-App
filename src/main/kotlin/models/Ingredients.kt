package models

data class Ingredients (
    var ingredientId: Int = 0,
    var name: String,
    var quantity: String,
    var weight: Int,  //grams
    var isOrganic: Boolean = false
)

