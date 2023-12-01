package controllers

import models.Recipe
import persistence.Serializer
import utils.Utilities.formatListString
import java.util.ArrayList
class RecipeAPI(serializerType: Serializer) {

    private var serializer: Serializer = serializerType
    private var recipes = ArrayList<Recipe>()
    private var lastId = 0
    private fun getId() = lastId++

    fun add(recipe: Recipe): Boolean {
        recipe.recipeId = getId()
        return recipes.add(recipe)
    }

    fun delete(id: Int) = recipes.removeIf { recipe -> recipe.recipeId == id }

    fun updateRecipe(id: Int, recipe: Recipe?): Boolean {
        val foundRecipe = findRecipe(id)

        if ((foundRecipe != null) && (recipe != null)) {
            foundRecipe.recipeTitle = recipe.recipeTitle
            foundRecipe.cookingTime = recipe.cookingTime
            foundRecipe.difficultyLevel = recipe.difficultyLevel
            foundRecipe.isRecipeVegan = recipe.isRecipeVegan
            foundRecipe.recipeCreator = recipe.recipeCreator
            return true
        }
        return false
    }

    fun listRecipes(): String =
        if (recipes.isEmpty()) "No recipes stored"
        else formatListString(recipes)

    fun numberOfRecipes() = recipes.size

    fun findRecipe(recipeId : Int) =  recipes.find{ recipe -> recipe.recipeId == recipeId }

    fun searchByTitle(searchString : String) =
        formatListString(recipes.filter { recipe -> recipe.recipeTitle.contains(searchString, ignoreCase = true)})

    fun searchByCookingTime(cookingTime : Int) =
        formatListString(recipes.filter { recipe -> recipe.cookingTime == cookingTime})

    fun searchByDifficultyLevel(searchString : String) =
        formatListString(recipes.filter { recipe -> recipe.difficultyLevel.contains(searchString, ignoreCase = true)})

    @Throws(Exception::class)
    fun load() {
        recipes = serializer.read() as ArrayList<Recipe>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(recipes)


    }

}