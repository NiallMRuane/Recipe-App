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

private val logger = KotlinLogging.logger {}
private val recipeAPI = RecipeAPI(YAMLSerializer(File("recipe.yaml")))
fun main() = runMenu()


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

fun listRecipe() = if (recipeAPI.numberOfRecipes() > 0)
    println(recipeAPI.listRecipes())
else
    logger.info("No recipes stored")

fun listVeganRecipes(){
    println(recipeAPI.listVeganRecipes())
}

fun listNonVeganRecipes() {
    println(recipeAPI.listNonVeganRecipes())
}

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

fun sortCaloriesAsc (){
    val ascCalories = recipeAPI.sortByCaloriesAsc()
    println("Recipes ascending by calories: \n $ascCalories")
}

fun sortCaloriesDesc (){
    val descCalories = recipeAPI.sortByCaloriesDesc()
    println("Recipes ascending by calories: \n $descCalories")
}

fun sortCookingTimeAsc (){
    val ascCookingTime = recipeAPI.sortByCookingTimeAsc()
    println("Recipes ascending by cooking time: \n$ascCookingTime")
}

fun sortCookingTimeDesc (){
    val descCookingTime = recipeAPI.sortByCookingTimeDesc()
    println("Recipes ascending by cooking time: \n$descCookingTime")
}
//------------
// ITEM MENU
//------------

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

fun save() {
    try {
        recipeAPI.store()
        logger.info { "All recipes successfully saved" }
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        recipeAPI.load()
        logger.info { "All recipes successfully loaded" }
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

    fun exitApp() {
        logger.info("Exiting...")
        exitProcess(0)
    }
