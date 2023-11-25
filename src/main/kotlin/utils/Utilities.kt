package utils

import models.Recipe
import models.Ingredients

object Utilities {

    // NOTE: JvmStatic annotation means that the methods are static i.e. we can call them over the class
    //      name; we don't have to create an object of Utilities to use them.

    @JvmStatic
    fun formatListString(notesToFormat: List<Recipe>): String =
        notesToFormat
            .joinToString(separator = "\n") { recipe ->  "$recipe" }

    @JvmStatic
    fun formatSetString(itemsToFormat: Set<Ingredients>): String =
        itemsToFormat
            .joinToString(separator = "\n") { ingredient ->  "\t$ingredient" }
}
