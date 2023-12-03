package models

import utils.Utilities

/**
 * Data class representing a recipe.
 *
 * @property recipeId The unique identifier for the recipe.
 * @property recipeTitle The title of the recipe.
 * @property cookingTime The cooking time of the recipe in minutes.
 * @property difficultyLevel The difficulty level of the recipe.
 * @property isRecipeVegan A flag indicating whether the recipe is vegan or not. Default is false.
 * @property calories The calories of the recipe.
 * @property recipeCreator The creator of the recipe.
 * @property ingredients The set of ingredients in the recipe.
 */
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
    /**
     * Gets the unique identifier for a new ingredient.
     *
     * @return The ingredient identifier.
     */
    private fun getIngredientId() = lastIngredientId++

    /**
     * Adds an ingredient to the recipe.
     *
     * @param ingredient The ingredient to be added.
     * @return `true` if the addition is successful, `false` otherwise.
     */
    fun addIngredient(ingredient: Ingredients): Boolean {
        ingredient.ingredientId = getIngredientId()
        return ingredients.add(ingredient)
    }

    /**
     * Deletes an ingredient from the recipe.
     *
     * @param id The identifier of the ingredient to be deleted.
     * @return `true` if the deletion is successful, `false` otherwise.
     */
    fun deleteIngredient(id: Int): Boolean {
        return ingredients.removeIf { ingredients -> ingredients.ingredientId == id}
    }

    /**
     * Finds an ingredient in the recipe.
     *
     * @param id The identifier of the ingredient to be found.
     * @return The found ingredient or `null` if not found.
     */
    fun findOneIngredient(id: Int): Ingredients?{
        return ingredients.find{ ingredients -> ingredients.ingredientId == id }
    }

    /**
     * Updates an ingredient in the recipe.
     *
     * @param id The identifier of the ingredient to be updated.
     * @param newIngredients The new ingredient data.
     * @return `true` if the update is successful, `false` otherwise.
     */
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

    /**
     * Finds an ingredient in the recipe.
     *
     * @param id The identifier of the ingredient to be found.
     * @return The found ingredient or `null` if not found.
     */
    fun findIngredient(id: Int): Ingredients? {
        return ingredients.find { ingredients -> ingredients.ingredientId == id }
    }

    /**
     * Generates a formatted string representing the list of ingredients.
     *
     * @return The formatted string.
     */
    fun listIngredients() =
        if (ingredients.isEmpty()) "\tNo ingredients added"
        else Utilities.formatSetString(ingredients)

    /**
     * Gets the number of ingredients in the recipe.
     *
     * @return The number of ingredients.
     */
    fun numberOfIngredients() = ingredients.size
}