package controllers
import models.Recipe
import persistence.Serializer
import utils.Utilities.formatListString
import java.util.ArrayList

/**
 * The `RecipeAPI` class manages the collection of recipes and provides various operations to interact with them.
 *
 * @param serializerType The type of serializer used for reading and writing recipes to a file.
 */
class RecipeAPI(serializerType: Serializer) {

    private var serializer: Serializer = serializerType
    private var recipes = ArrayList<Recipe>()
    private var lastId = 0

    /**
     * Generates and retrieves a unique ID for a new recipe.
     */
    private fun getId() = lastId++

    /**
     * Adds a new recipe to the collection.
     *
     * @param recipe The recipe to be added.
     * @return `true` if the addition is successful, `false` otherwise.
     */
    fun add(recipe: Recipe): Boolean {
        recipe.recipeId = getId()
        return recipes.add(recipe)
    }

    /**
     * Deletes a recipe from the collection based on its ID.
     *
     * @param id The ID of the recipe to be deleted.
     * @return `true` if the deletion is successful, `false` otherwise.
     */
    fun delete(id: Int) = recipes.removeIf { recipe -> recipe.recipeId == id }

    /**
     * Updates the details of a recipe based on its ID.
     *
     * @param id The ID of the recipe to be updated.
     * @param recipe The updated recipe details.
     * @return `true` if the update is successful, `false` otherwise.
     */
    fun updateRecipe(id: Int, recipe: Recipe?): Boolean {
        val foundRecipe = findRecipe(id)

        if ((foundRecipe != null) && (recipe != null)) {
            foundRecipe.recipeTitle = recipe.recipeTitle
            foundRecipe.cookingTime = recipe.cookingTime
            foundRecipe.difficultyLevel = recipe.difficultyLevel
            foundRecipe.calories = recipe.calories
            foundRecipe.recipeCreator = recipe.recipeCreator
            return true
        }
        return false
    }

    /**
     * Returns a formatted string listing all recipes.
     *
     * @return A formatted string listing all recipes.
     */
    fun listRecipes(): String =
        if (recipes.isEmpty()) {
            "No recipes stored"
        } else {
            formatListString(recipes)
        }

    /**
     * Returns a formatted string listing non-vegan recipes.
     *
     * @return A formatted string listing non-vegan recipes.
     */
    fun listNonVeganRecipes(): String =
        if (numberOfNonVeganRecipes() == 0) {
            "No non vegan recipes stored"
        } else {
            formatListString(recipes.filter { recipe -> !recipe.isRecipeVegan })
        }

    /**
     * Returns a formatted string listing vegan recipes.
     *
     * @return A formatted string listing vegan recipes.
     */
    fun listVeganRecipes(): String =
        if (numberOfVeganRecipes() == 0) {
            "No vegan recipes stored"
        } else {
            formatListString(recipes.filter { recipe -> recipe.isRecipeVegan })
        }

    /**
     * Returns the total number of recipes.
     *
     * @return The total number of recipes.
     */

    fun numberOfRecipes() = recipes.size

    /**
     * Returns the number of non-vegan recipes.
     *
     * @return The number of non-vegan recipes.
     */
    fun numberOfNonVeganRecipes(): Int = recipes.count { recipe: Recipe -> !recipe.isRecipeVegan }

    /**
     * Returns the number of vegan recipes.
     *
     * @return The number of vegan recipes.
     */
    fun numberOfVeganRecipes(): Int = recipes.count { recipe: Recipe -> recipe.isRecipeVegan }

    /**
     * Finds a recipe by its ID.
     *
     * @param recipeId The ID of the recipe to be found.
     * @return The found recipe or `null` if not found.
     */
    fun findRecipe(recipeId: Int) = recipes.find { recipe -> recipe.recipeId == recipeId }

    /**
     * Searches for recipes by title containing the specified string.
     *
     * @param searchString The string to search for in recipe titles.
     * @return A formatted string listing matching recipes.
     */
    fun searchByTitle(searchString: String) =
        formatListString(recipes.filter { recipe -> recipe.recipeTitle.contains(searchString, ignoreCase = true) })

    /**
     * Searches for recipes by cooking time.
     *
     * @param cookingTime The cooking time to search for.
     * @return A formatted string listing matching recipes.
     */
    fun searchByCookingTime(cookingTime: Int) =
        formatListString(recipes.filter { recipe -> recipe.cookingTime == cookingTime })

    /**
     * Searches for recipes by difficulty level containing the specified string.
     *
     * @param searchString The string to search for in difficulty levels.
     * @return A formatted string listing matching recipes.
     */
    fun searchByDifficultyLevel(searchString: String) =
        formatListString(recipes.filter { recipe -> recipe.difficultyLevel.contains(searchString, ignoreCase = true) })

    /**
     * Searches for recipes with calories less than or equal to the specified maximum calories.
     *
     * @param maxCalories The maximum calories to search for.
     * @return A formatted string listing matching recipes.
     */
    fun searchByCalories(maxCalories: Int) =
        formatListString((recipes.filter { recipe -> recipe.calories <= maxCalories }))

    /**
     * Searches for ingredients by name across all recipes.
     *
     * @param searchString The string to search for in ingredient names.
     * @return A formatted string listing matching recipes and their ingredients.
     */
    fun searchIngredientByName(searchString: String): String {
        return if (numberOfRecipes() == 0) {
            "No recipes stored"
        } else {
            var listOfRecipes = ""
            for (recipe in recipes) {
                for (ingredients in recipe.ingredients) {
                    if (ingredients.name.contains(searchString, ignoreCase = true)) {
                        listOfRecipes += "${recipe.recipeId}: ${recipe.recipeTitle} \n\t${ingredients}\n"
                    }
                }
            }
            if (listOfRecipes == "") {
                "No items found for: $searchString"
            } else {
                listOfRecipes
            }
        }
    }

    /**
     * Returns a formatted string listing recipes sorted by calories in ascending order.
     *
     * @return A formatted string listing recipes sorted by calories in ascending order.
     */
    fun sortByCaloriesAsc() =
        formatListString(recipes.sortedWith(compareBy { it.calories }))

    /**
     * Returns a formatted string listing recipes sorted by calories in descending order.
     *
     * @return A formatted string listing recipes sorted by calories in descending order.
     */
    fun sortByCaloriesDesc() =
        formatListString(recipes.sortedWith(compareBy { recipe -> recipe.calories }).reversed())

    /**
     * Returns a formatted string listing recipes sorted by cooking time in ascending order.
     *
     * @return A formatted string listing recipes sorted by cooking time in ascending order.
     */
    fun sortByCookingTimeAsc() =
        formatListString(recipes.sortedWith(compareBy { it.cookingTime }))

    /**
     * Returns a formatted string listing recipes sorted by cooking time in descending order.
     *
     * @return A formatted string listing recipes sorted by cooking time in descending order.
     */
    fun sortByCookingTimeDesc() =
        formatListString(recipes.sortedWith(compareBy { recipe -> recipe.cookingTime }).reversed())

    /**
     * Checks if the given index is valid for the provided list.
     *
     * @param index The index to be checked.
     * @param list The list for which the index is checked.
     * @return `true` if the index is valid, `false` otherwise.
     */
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    /**
     * Checks if the given index is valid for the recipes list.
     *
     * @param index The index to be checked.
     * @return `true` if the index is valid, `false` otherwise.
     */
    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, recipes)
    }

    /**
     * Marks a recipe as vegan based on its index in the recipes list.
     *
     * @param indexToVegan The index of the recipe to be marked as vegan.
     * @return `true` if the marking is successful, `false` otherwise.
     */
    fun markRecipeVegan(indexToVegan: Int): Boolean {
        if (isValidIndex(indexToVegan)) {
            val recipeToMark = recipes[indexToVegan]
            if (!recipeToMark.isRecipeVegan) {
                recipeToMark.isRecipeVegan = true
                return true
            }
        }
        return false
    }

    /**
     * Reads recipes from a file using the configured serializer.
     *
     * @throws Exception if an error occurs during the reading process.
     */
    @Throws(Exception::class)
    fun load() {
        recipes = serializer.read() as ArrayList<Recipe>
    }

    /**
     * Writes recipes to a file using the configured serializer.
     *
     * @throws Exception if an error occurs during the writing process.
     */
    @Throws(Exception::class)
    fun store() {
        serializer.write(recipes)
    }
}
