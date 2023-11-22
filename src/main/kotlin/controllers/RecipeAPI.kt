package controllers

import models.Recipe
import java.util.ArrayList
class RecipeAPI {

    private var recipes = ArrayList<Recipe>()

    fun add(recipe: Recipe): Boolean {
        return recipes.add(recipe)
    }


}