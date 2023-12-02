package models

import utils.Utilities

data class Recipe(
    var recipeId: Int = 0,
    var recipeTitle: String = "Untitled",
    var cookingTime: Int = 60,  //minutes
    var difficultyLevel: String = "Unknown",
    var isRecipeVegan: Boolean = false,
    var calories: Int = 500,
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

    fun deleteIngredient(id: Int): Boolean {
        return ingredients.removeIf { ingredients -> ingredients.ingredientId == id}
    }

    fun findOneIngredient(id: Int): Ingredients?{
        return ingredients.find{ ingredients -> ingredients.ingredientId == id }
    }

    fun updateIngredient(id: Int, newIngredients : Ingredients): Boolean {
        val foundItem = findOneIngredient(id)
        if (foundItem != null){
            foundItem.name = newIngredients.name
            foundItem.quantity = newIngredients.quantity
            foundItem.weight = newIngredients.weight
            return true
        }
        return false
    }


    fun findIngredient(id: Int): Ingredients? {
        return ingredients.find { ingredients -> ingredients.ingredientId == id }
    }

    fun listIngredients() =
        if (ingredients.isEmpty()) "\tNo ingredients added"
        else Utilities.formatSetString(ingredients)
    fun numberOfIngredients() = ingredients.size
}