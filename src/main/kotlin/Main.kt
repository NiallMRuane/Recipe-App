/**
 * Recipe Application main class.
 * This application allows users to manage recipes, ingredients, and perform various operations.
 * @author [Niall Ruane]
 */

import controllers.RecipeAPI
import models.Ingredients
import models.Recipe
import mu.KotlinLogging
import persistence.YAMLSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextBoolean
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import kotlin.system.exitProcess


/**
 * Logger for the Recipe Application.
 */
private val logger = KotlinLogging.logger {}
private val recipeAPI = RecipeAPI(YAMLSerializer(File("recipe.yaml")))

/**
 * Entry point of the Recipe Application.
 */
fun main() = runMenu()

/**
 * Runs the main menu of the Recipe Application in a loop until the user chooses to exit.
 */
fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> addRecipe()
            2 -> deleteRecipe()
            3 -> updateRecipe()
            4 -> listRecipes()
            5 -> searchRecipes()
            6 -> markRecipeVegan()
            7 -> sortRecipes()
            8 -> addIngredientToRecipe()
            9 -> deleteIngredient()
            10 -> updateIngredient()
            11 -> searchIngredientName()
            12 -> markOrganic()
            20 -> save()
            21 -> load()
            0 -> exitApp()
            else -> logger.info("Invalid menu choice: $option")
        }
    } while (true)
}

fun mainMenu() = readNextInt(
    """ 
      -----------------------------------------------
    /-------------------------------------------------\
   |                   RECIPE APP                      |
   |---------------------------------------------------|
   | RECIPE MENU          |            ITEM MENU       |
   |                     |                             |
   |   1) Add a recipe   |   8) Add ingredients        |
   |   2) Delete a recipe|   9) Delete ingredient      |
   |   3) Update recipes |   10) Update ingredient     |
   |   4) List recipes   |   11) Search by name        |
   |   5) Search recipes |   12) Mark as organic       |
   |   6) Mark as vegan  |                             |
   |   7) Sort recipes   |                             |
   |                     |                             |
   |---------------------------------------------------|
   |   20) Save all recipes and ingredients            |
   |   21) Load all recipes and ingredients            |
   |   0) Exit                                         |
    \-------------------------------------------------/
      -----------------------------------------------
   ==>> """
)
//------------
// RECIPE MENU
//------------

/**
 * Adds a new recipe to the application.
 */
fun addRecipe() {
    val recipeTitle = readNextLine("Enter a title for the recipe: ")
    val cookingTime = readNextInt("Enter the cooking time (minutes): ")
    val difficultyLevel = readNextLine("Enter the difficulty level: ")
    val calories = readNextInt("Enter the calorie count: ")
    val recipeCreator = readNextLine("Enter the creator name: ")
    val isAdded = recipeAPI.add(Recipe(
        recipeTitle = recipeTitle, cookingTime = cookingTime, difficultyLevel = difficultyLevel, calories =  calories, recipeCreator = recipeCreator))

    if (isAdded) println("Added Successfully")
    else logger.info("Add Failed")
}

/**
 * Deletes a recipe from the application.
 */
fun deleteRecipe() {
    listRecipe()
    if (recipeAPI.numberOfRecipes() > 0) {
        val id = readNextInt("Enter the id of the recipe to delete: ")
        val recipeToDelete = recipeAPI.delete(id)
        if (recipeToDelete) {
            logger.info("Delete Successful!")
        } else {
            logger.info("Delete NOT Successful")
        }
    }
}

/**
 * Updates an existing recipe in the application.
 */
fun updateRecipe() {
    listRecipe()
    if (recipeAPI.numberOfRecipes() > 0) {
        val id = readNextInt("Enter the id of the recipe to update: ")
        if (recipeAPI.findRecipe(id) != null){
            val recipeTitle = readNextLine("Enter a title for the recipe: ")
            val cookingTime = readNextInt("Enter the cooking time (minutes): ")
            val difficultyLevel = readNextLine("Enter the difficulty level: ")
            val calories = readNextInt("Enter the calorie count:  ")
            val recipeCreator = readNextLine("Enter the creator name: ")

            if (recipeAPI.updateRecipe(id, Recipe(0, recipeTitle, cookingTime, difficultyLevel, isRecipeVegan = false, calories, recipeCreator))) {
                println("Update Successful")
            } else {
                logger.info("Update Failed")
            }
        }
    } else {
        logger.info("There are no recipes for this index number")
    }
}

/**
 * Lists recipes based on user preferences.
 */
fun listRecipes(){
    if (recipeAPI.numberOfRecipes() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL recipes        |
                  > |   2) View non vegan recipes  |
                  > |   3) View vegan recipes      |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listRecipe()
            2 -> listNonVeganRecipes()
            3 -> listVeganRecipes()
            else -> logger.info("Invalid option entered: $option")
        }
    } else {
        logger.info("Option Invalid - No recipes stored")
    }
}

/**
 * Lists all recipes in the application.
 */
fun listRecipe() = if (recipeAPI.numberOfRecipes() > 0)
    println(recipeAPI.listRecipes())
else
    logger.info("No recipes stored")

/**
 * Lists all recipes marked as vegan in the application.
 */
fun listVeganRecipes(){
    println(recipeAPI.listVeganRecipes())
}

/**
 * Lists all recipes not marked as vegan in the application.
 */
fun listNonVeganRecipes() {
    println(recipeAPI.listNonVeganRecipes())
}

/**
 * Marks a non-vegan recipe as vegan.
 */
fun markRecipeVegan() {
    listNonVeganRecipes()
    if (recipeAPI.numberOfNonVeganRecipes() > 0) {
        val indexToMark = readNextInt("Enter the index of the recipe to mark vegan: ")
        if (recipeAPI.markRecipeVegan(indexToMark)) {
            println("Recipe marked vegan successfully!")
        } else {
            logger.info("Recipe NOT marked vegan successfully")
        }
    }
}

//--------------------
//  SEARCH FOR RECIPES
//--------------------

/**
 * Searches for recipes based on user input, allowing various search criteria.
 * Displays a menu with options to search by title, cooking time, difficulty level, or calories.
 */
fun searchRecipes(){
    if (recipeAPI.numberOfRecipes() > 0) {
        val option = readNextInt(
            """
                  > -------------------------------------
                  > |   1) Search by title               |
                  > |   2) Search by cooking time        |
                  > |   3) Search by difficulty level    |
                  > |   4) Search by calories            |
                  > -------------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> searchByTitle()
            2 -> searchByCookingTime()
            3 -> searchByDifficultyLevel()
            4 -> searchByCalories()
            else -> logger.info("Invalid option entered: $option")
        }
    } else {
        logger.info("Option Invalid - No recipes stored")
    }
}

/**
 * Searches for recipes by title.
 * Prompts the user to enter the title to search by and displays the matching results.
 */
fun searchByTitle (){
    if (recipeAPI.numberOfRecipes() > 0) {
    val searchTitle = readNextLine("Enter the title to search by: ")
    val searchResults = recipeAPI.searchByTitle(searchTitle)
    if (searchResults.isEmpty()) logger.info("No recipes found")
    else
        println(searchResults)
    } else {
        logger.info("Option Invalid, no recipes found")
    }
}

/**
 * Searches for recipes by cooking time.
 * Prompts the user to enter the cooking time to search by and displays the matching results.
 */
fun searchByCookingTime (){
    if (recipeAPI.numberOfRecipes() > 0) {
    val searchCookingTime = readNextInt("Enter the cooking time to search by: ")
    val searchResults = recipeAPI.searchByCookingTime(searchCookingTime)
    if (searchResults.isEmpty()) logger.info("No recipes found")
    else
        println(searchResults)
    } else {
        logger.info("Option Invalid, no recipes found")
    }
}

/**
 * Searches for recipes by difficulty level.
 * Prompts the user to enter the difficulty level to search by and displays the matching results.
 */
fun searchByDifficultyLevel (){
    if (recipeAPI.numberOfRecipes() > 0) {
    val searchDifficulty = readNextLine("Enter the difficulty level to search by: ")
    val searchResults = recipeAPI.searchByDifficultyLevel(searchDifficulty)
    if (searchResults.isEmpty()) logger.info("No recipes found")
    else
        println(searchResults)
    } else {
    logger.info("Option Invalid, no recipes found")
   }
}

/**
 * Searches for recipes by calories.
 * Prompts the user to enter the maximum calories and displays the matching results.
 */
fun searchByCalories() {
    if (recipeAPI.numberOfRecipes() > 0) {
        val maxCalories = readNextInt("Enter the max amount of calories: ")
        val searchResults = recipeAPI.searchByCalories(maxCalories)
        if (searchResults.isEmpty()) logger.info("No recipes found with calories equal, or below $maxCalories")
        else
            println(searchResults)
    } else {
        logger.info("Option Invalid, no recipes found")
    }
}

/**
 * Displays a menu for sorting recipes based on user-selected criteria.
 * The user can choose to sort recipes by calories or cooking time in ascending or descending order.
 */
fun sortRecipes(){
    if (recipeAPI.numberOfRecipes() > 0) {
        val option = readNextInt(
            """
                  > -------------------------------------
                  > |   1) Sort by calories              |
                  > |   2) Sort by cooking time          |
                  > -------------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> {
                val calorieOption = readNextInt(
                    """
                  > --------------------------------
                  > |   1) Ascending                |
                  > |   2) Descending               |
                  > --------------------------------
         > ==>> """.trimMargin(">"))
                when (calorieOption) {
                    1 -> sortCaloriesAsc()
                    2 -> sortCaloriesDesc()
                    else -> logger.info("Invalid option entered: $calorieOption");
                }
            }
            2 -> {
                val cookingTimeOption = readNextInt(
                    """
                  > --------------------------------
                  > |   1) Ascending                |
                  > |   2) Descending               |
                  > --------------------------------
         > ==>> """.trimMargin(">"))
                when (cookingTimeOption) {
                    1 -> sortCookingTimeAsc()
                    2 -> sortCookingTimeDesc()
                    else -> logger.info("Invalid option entered: $cookingTimeOption");
                }
            }
            else -> logger.info("Invalid option entered: $option")
        }
    } else {
        logger.info("Option Invalid - No recipes stored")
    }
}

/**
 * Sorts recipes in ascending order based on calories and prints the result.
 */
fun sortCaloriesAsc (){
    val ascCalories = recipeAPI.sortByCaloriesAsc()
    println("Recipes ascending by calories: \n $ascCalories")
}

/**
 * Sorts recipes in descending order based on calories and prints the result.
 */

fun sortCaloriesDesc (){
    val descCalories = recipeAPI.sortByCaloriesDesc()
    println("Recipes ascending by calories: \n $descCalories")
}

/**
 * Sorts recipes in ascending order based on cooking time and prints the result.
 */
fun sortCookingTimeAsc (){
    val ascCookingTime = recipeAPI.sortByCookingTimeAsc()
    println("Recipes ascending by cooking time: \n$ascCookingTime")
}

/**
 * Sorts recipes in descending order based on cooking time and displays the results.
 */
fun sortCookingTimeDesc (){
    val descCookingTime = recipeAPI.sortByCookingTimeDesc()
    println("Recipes ascending by cooking time: \n$descCookingTime")
}
//------------
// ITEM MENU
//------------

/**
 * Adds a new ingredient to the chosen recipe.
 */
private fun addIngredientToRecipe() {
    val recipe: Recipe? = askUserToChooseRecipe()
    if (recipe != null) {
        val name = readNextLine("\tIngredient Name: ")
        val quantity = readNextLine("\tIngredient Quantity: ")
        val weight = readNextInt("\tIngredient weight (grams): ")
        val ingredient = Ingredients(name = name, quantity = quantity, weight = weight)
        if (recipe.addIngredient(ingredient))
            println("Add Successful")
        else
            logger.info("Add Not Successful")
    }
}

/**
 * Deletes the chosen ingredient from the chosen recipe.
 */
    fun deleteIngredient() {
        val recipe: Recipe? = askUserToChooseRecipe()
        if (recipe != null) {
            val ingredient: Ingredients? = askUserToChooseIngredient(recipe)
            if (ingredient != null) {
                val isDeleted = recipe.deleteIngredient(ingredient.ingredientId)
                if (isDeleted) {
                    println("Delete Successful!")
                } else {
                    println("Delete NOT Successful")
                }
            }
        }
    }

/**
 * Updates the details of the chosen ingredient in the chosen recipe.
 */
fun updateIngredient() {
    val recipe: Recipe? = askUserToChooseRecipe()
    if (recipe != null) {
        val ingredients: Ingredients? = askUserToChooseIngredient(recipe)
        if (ingredients != null) {
            val newName = readNextLine("Enter new name: ")
            val newQuantity = readNextLine("Enter new quantity: ")
            val newWeight = readNextInt("Enter new weight (in grams): ")
            if (recipe.updateIngredient(ingredients.ingredientId, Ingredients(name = newName, quantity = newQuantity, weight = newWeight))) {
                println("Item contents updated")
            } else {
                println("Item contents NOT updated")
            }
        } else {
            println("Invalid Item Id")
        }
    }
}

/**
 * Searches for ingredients by name and displays the results.
 */
fun searchIngredientName() {
    if (recipeAPI.numberOfRecipes() > 0) {
        val searchName = readNextLine("Enter the ingredient name to search by: ")
        val searchResults = recipeAPI.searchIngredientByName(searchName)
        if (searchResults.isEmpty()) {
            logger.info("No ingredients found")
        } else {
            println(searchResults)
        }
    }
}

/**
 * Marks the organic status of the chosen ingredient based on user input.
 */
fun markOrganic() {
    val recipe: Recipe? = askUserToChooseRecipe()
    if (recipe != null) {
        val ingredients: Ingredients? = askUserToChooseIngredient(recipe)
        if (ingredients != null) {
            var changeStatus = 'X'
            if (ingredients.isOrganic) {
                changeStatus =
                    ScannerInput.readNextChar("The ingredient is currently organic, do you want to mark it as non-organic? \n" +
                            " Enter 'Y' to mark as non-organic \n Press any key to exit: \n==>>")
                if ((changeStatus == 'Y') ||  (changeStatus == 'y'))
                    ingredients.isOrganic = false
            }
            else {
                changeStatus =
                    ScannerInput.readNextChar("TThe ingredient is currently non-organic, do you want to mark it as organic? \n" +
                            " Enter 'Y' to mark as organic \n Press any key to exit: \n==>>")
                if ((changeStatus == 'Y') ||  (changeStatus == 'y'))
                    ingredients.isOrganic = true
            }
        }
    }
}

/**
 * Asks the user to choose a recipe from the list.
 * Displays the list of recipes and returns the chosen recipe.
 *
 * @return The chosen recipe, or null if the chosen recipe is not valid.
 */
private fun askUserToChooseRecipe(): Recipe? {
    listRecipe()
    if (recipeAPI.numberOfRecipes() > 0) {
        val recipe = recipeAPI.findRecipe(readNextInt("\nEnter the id of the recipe: "))
        if (recipe != null) {
            return recipe
        } else {
            logger.info("Recipe id is not valid")
        }
    }
    return null
}

/**
 * Asks the user to choose an ingredient from the list of ingredients of a recipe.
 * Displays the list of ingredients and returns the chosen ingredient.
 *
 * @param recipe The chosen recipe.
 * @return The chosen ingredient, or null if there are no ingredients for the chosen recipe.
 */
private fun askUserToChooseIngredient(recipe: Recipe): Ingredients? {
    if (recipe.numberOfIngredients() > 0) {
        print(recipe.listIngredients())
        return recipe.findIngredient(readNextInt("\nEnter the id of the ingredient: "))
    }
    else{
        logger.info("No ingredients for chosen recipe")
        return null
    }
}

/**
 * Saves all recipes to the file using the RecipeAPI.
 * Logs a success message if the operation is successful.
 * Prints an error message to the standard error if any exception occurs during the save operation.
 */
fun save() {
    try {
        recipeAPI.store()
        logger.info { "All recipes successfully saved" }
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

/**
 * Loads all recipes from the file using the RecipeAPI.
 * Logs a success message if the operation is successful.
 * Prints an error message to the standard error if any exception occurs during the load operation.
 */
fun load() {
    try {
        recipeAPI.load()
        logger.info { "All recipes successfully loaded" }
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

/**
 * Exits the application by logging a message and terminating the process.
 */
    fun exitApp() {
        logger.info("Exiting...")
        exitProcess(0)
    }
