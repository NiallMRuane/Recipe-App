import controllers.RecipeAPI
import models.Ingredients
import models.Recipe
import mu.KotlinLogging
import utils.ScannerInput
import utils.ScannerInput.readNextBoolean
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine

private val logger = KotlinLogging.logger {}
private val recipeAPI = RecipeAPI()
fun main() = runMenu()


fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> addRecipe()
            2 -> listRecipe()
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

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listRecipe() {
    if (recipeAPI.numberOfRecipes() > 0) {
        println(recipeAPI.listRecipes())
    } else {
        println("No recipes stored")
    }
}
fun exitApp(){
    logger.info("Exiting...")
    System.exit(0)
}

