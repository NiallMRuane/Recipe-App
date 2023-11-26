import controllers.RecipeAPI
import models.Ingredients
import models.Recipe
import mu.KotlinLogging
import utils.ScannerInput.readNextBoolean
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}
private val recipeAPI = RecipeAPI()
fun main() = runMenu()


fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> addRecipe()
            2 -> deleteRecipe()
            3 -> listRecipe()
            4 -> searchRecipes()
            5 -> addIngredientToRecipe()
            0 -> exitApp()
            else -> println("Invalid menu choice: $option")
        }
    } while (true)
}

fun mainMenu() = readNextInt(
    """ 
         > -----------------------------------------------------  
         > |                  RECIPE APP                  |
         > -----------------------------------------------------  
         > | RECIPE MENU                                       |
         > |   1) Add a recipe                                 |
         > |   2) Delete a recipe                              |
         > |   3) List recipes                                 |
         > |   4) Search recipes                               |
         > -----------------------------------------------------  
         > | ITEM MENU                                         | 
         > -----------------------------------------------------  
         > |   5) Add ingredients                              |
         > -----------------------------------------------------
         > |   0) Exit                                         |
         > -----------------------------------------------------  
         > ==>> """.trimMargin(">")
)
//------------
// RECIPE MENU
//------------
fun addRecipe() {
    var recipeTitle = readNextLine("Enter a title for the recipe: ")
    var cookingTime = readNextInt("Enter the cooking time (minutes): ")
    var difficultyLevel = readNextLine("Enter the difficulty level: ")
    var isRecipeVegan = readNextBoolean("Is the recipe vegan? (true/false) ")
    var recipeCreator = readNextLine("Enter the creator name: ")
    val isAdded = recipeAPI.add(Recipe(
        recipeTitle = recipeTitle, cookingTime = cookingTime,
        difficultyLevel = difficultyLevel, isRecipeVegan = isRecipeVegan, recipeCreator = recipeCreator))

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

fun listRecipe() = if (recipeAPI.numberOfRecipes() > 0)
    println(recipeAPI.listRecipes())
else
    logger.info("No recipes stored")

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
                  > -------------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> searchByTitle()
            2 -> searchByCookingTime()
            3 -> searchByDifficultyLevel()
            else -> logger.info("Invalid option entered: $option")
        }
    } else {
        logger.info("Option Invalid - No recipes stored")
    }
}
fun searchByTitle (){
    val searchTitle = readNextLine("Enter the title to search by: ")
    val searchResults = recipeAPI.searchByTitle(searchTitle)
    if (searchResults.isEmpty()) logger.info("No recipes found")
    else
        println(searchResults)
}

fun searchByCookingTime (){
    val searchCookingTime = readNextInt("Enter the cooking time to search by: ")
    val searchResults = recipeAPI.searchByCookingTime(searchCookingTime)
    if (searchResults.isEmpty()) logger.info("No recipes found")
    else
        println(searchResults)
}

fun searchByDifficultyLevel (){
    val searchDifficulty = readNextLine("Enter the difficulty level to search by: ")
    val searchResults = recipeAPI.searchByDifficultyLevel(searchDifficulty)
    if (searchResults.isEmpty()) logger.info("No recipes found")
    else
        println(searchResults)
}

//------------
// ITEM MENU
//------------

private fun addIngredientToRecipe() {
    val recipe: Recipe? = askUserToChooseRecipe()
    if (recipe != null) {
        val name = readNextLine("\tIngredient Name: ")
        val quantity = readNextInt("\tIngredient Quantity: ")
        val weight = readNextInt("\tIngredient weight (grams): ")
        val ingredient = Ingredients(name = name, quantity = quantity, weight = weight)
        if (recipe.addIngredient(ingredient))
            println("Add Successful")
        else
            logger.info("Add Not Successful")
    }

}
private fun askUserToChooseRecipe(): Recipe? {
    listRecipe()
    if (recipeAPI.numberOfRecipes() > 0) {
        val recipe = recipeAPI.findRecipe(readNextInt("\nEnter the id of the note: "))
        if (recipe != null) {
            return recipe
        } else {
            println("Recipe id is not valid")
        }
    }
    return null
}

    fun exitApp() {
        logger.info("Exiting...")
        exitProcess(0)
    }
