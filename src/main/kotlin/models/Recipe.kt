package models

import utils.Utilities

data class Recipe(
    var recipeId: Int = 0,
    var recipeTitle: String,
    var cookingTime: Int,  //minutes
    var difficultyLevel: String,
    var isRecipeVegan: Boolean,
    var recipeCreator: String,
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