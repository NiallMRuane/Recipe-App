package models

/**
 * Data class representing an ingredient in a recipe.
 *
 * @property ingredientId The unique identifier for the ingredient.
 * @property name The name of the ingredient.
 * @property quantity The quantity of the ingredient.
 * @property weight The weight of the ingredient in grams.
 * @property isOrganic A flag indicating whether the ingredient is organic or not. Default is false.
 */

data class Ingredients (
    var ingredientId: Int = 0,
    var name: String,
    var quantity: String,
    var weight: Int,  //grams
    var isOrganic: Boolean = false
)

