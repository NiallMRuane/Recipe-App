package controllers

import models.Recipe
import utils.Utilities.formatListString
import java.util.ArrayList
class RecipeAPI {

    private var recipes = ArrayList<Recipe>()

    fun add(recipe: Recipe): Boolean {
        return recipes.add(recipe)
    }

    fun listRecipes(): String =
        if (recipes.isEmpty()) "No recipes stored"
        else formatListString(recipes)

    fun searchByTitle(searchString : String) =
        formatListString(recipes.filter { recipe -> recipe.recipeTitle.contains(searchString, ignoreCase = true)})

    fun searchByCookingTime(cookingTime : Int) =
        formatListString(recipes.filter { recipe -> recipe.cookingTime == cookingTime})

    fun searchByDifficultyLevel(searchString : String) =
        formatListString(recipes.filter { recipe -> recipe.difficultyLevel.contains(searchString, ignoreCase = true)})
    fun numberOfRecipes() = recipes.size


}