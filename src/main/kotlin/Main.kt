import controllers.RecipeAPI
import models.Ingredients
import models.Recipe
import mu.KotlinLogging
import utils.ScannerInput
import utils.ScannerInput.readNextBoolean
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import utils.Utilities.formatListString
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}
private val recipeAPI = RecipeAPI()
fun main() = runMenu()


fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> addRecipe()
            2 -> listRecipe()
            3 -> searchByTitle()
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
         > |   2) List recipes                                 |
         > |   3) Search recipes by title                      |
         > -----------------------------------------------------  
         > | ITEM MENU                                         | 
         > -----------------------------------------------------  
         > |   0) Exit                                         |
         > -----------------------------------------------------  
         > ==>> """.trimMargin(">")
)
//------------
// RECIPE MENU
//------------
fun addRecipe() {
    var recipeTitle = readNextLine("Enter a title for the recipe:")
    var cookingTime = readNextInt("Enter the cooking time in minutes:")
    var difficultyLevel = readNextLine("Enter the difficulty level:")
    var isRecipeVegan = readNextBoolean("Is the recipe vegan? (true/false) ")
    var recipeCreator = readNextLine("Enter the creator name:")
    val isAdded = recipeAPI.add(Recipe(recipeTitle = recipeTitle, cookingTime = cookingTime,difficultyLevel = difficultyLevel, isRecipeVegan = isRecipeVegan, recipeCreator = recipeCreator))

    if (isAdded) println("Added Successfully")
    else logger.info("Add Failed")
}

fun listRecipe() = if (recipeAPI.numberOfRecipes() > 0)
    println(recipeAPI.listRecipes())
else
    logger.info("No recipes stored")

fun searchByTitle (){
    val searchTitle = readNextLine("Enter the title to search by: ")
    val searchResults = recipeAPI.searchByTitle(searchTitle)
    if (searchResults.isEmpty()) logger.info("No notes found")
    else
        println(searchResults)
}
fun exitApp(){
    logger.info("Exiting...")
    exitProcess(0)
}

