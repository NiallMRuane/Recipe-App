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

    fun numberOfRecipes() = recipes.size


}