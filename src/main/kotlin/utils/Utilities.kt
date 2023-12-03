package utils

import models.Ingredients
import models.Recipe

/**
 * Utility class for formatting Recipe and Ingredients data.
 */
object Utilities {

    /**
     * Formats a list of Recipe objects into a readable string.
     *
     * @param recipesToFormat List of Recipe objects to format.
     * @return Formatted string representing the Recipe objects.
     */
    @JvmStatic
    fun formatListString(recipesToFormat: List<Recipe>): String =
        recipesToFormat
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
    |
                """.trimMargin()
            }

    /**
     * Formats a set of Ingredients objects into a readable string.
     *
     * @param itemsToFormat Set of Ingredients objects to format.
     * @return Formatted string representing the Ingredients objects.
     */
    @JvmStatic
    fun formatSetString(itemsToFormat: Set<Ingredients>): String =
        itemsToFormat.joinToString("") { ingredient ->
            """"
            ${ingredient.quantity} of ${ingredient.name}(${ingredient.weight})
            Organic Status: ${ingredient.isOrganic}
            """.trimMargin()
        }
}
