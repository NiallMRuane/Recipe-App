package utils

import models.Recipe
import models.Ingredients

object Utilities {

    // NOTE: JvmStatic annotation means that the methods are static i.e. we can call them over the class
    //      name; we don't have to create an object of Utilities to use them.

    @JvmStatic
    fun formatListString(notesToFormat: List<Recipe>): String =
        notesToFormat
            .joinToString(separator = "\n\n") { recipe ->
                """
        | Recipe Id: ${recipe.recipeId}
        | Recipe Title: ${recipe.recipeTitle}
        | Cooking Time: ${recipe.cookingTime} minutes
        | Difficulty Level: ${recipe.difficultyLevel}
        | Calories: ${recipe.calories}
        | Vegan Status: ${recipe.isRecipeVegan}
        | Recipe Creator: ${recipe.recipeCreator}
        | Ingredients: ${
                    recipe.ingredients.joinToString("\n") { ingredient ->
                        """${ingredient.quantity} of ${ingredient.name}(${ingredient.weight} in grams)
                    | Organic Status: ${ingredient.isOrganic}
                    """.trimMargin()
                    }
                }
    |""".trimMargin()
            }

    @JvmStatic
    fun formatSetString(itemsToFormat: Set<Ingredients>): String =
        itemsToFormat.joinToString(""){ ingredient ->
                """"
            ${ingredient.quantity} of ${ingredient.name}(${ingredient.weight})
            Organic Status: ${ingredient.isOrganic}
            """.trimMargin()

            }
}
