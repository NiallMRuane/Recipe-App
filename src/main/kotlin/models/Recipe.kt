package models

import utils.Utilities

data class Recipe(
    var recipeId: Int = 0,
    var recipeTitle: String = "Untitled",
    var cookingTime: Int = 60,  //minutes
    var difficultyLevel: String = "Unknown",
    var isRecipeVegan: Boolean = false,
    var recipeCreator: String = "Unknown",
    var ingredients: MutableSet<Ingredients> = mutableSetOf()
)
{
    private var lastIngredientId = 0
    private fun getIngredientId() = lastIngredientId++

    fun addIngredient(ingredient: Ingredients): Boolean {
        ingredient.ingredientId = getIngredientId()
        return ingredients.add(ingredient)
    }

    fun findIngredient(id: Int): Ingredients? {
        return ingredients.find { ingredients -> ingredients.ingredientId == id }
    }

    fun listIngredients() =
        if (ingredients.isEmpty()) "\tNo ingredients added"
        else Utilities.formatSetString(ingredients)
    fun numberOfIngredients() = ingredients.size
}