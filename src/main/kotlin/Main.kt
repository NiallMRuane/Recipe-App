import controllers.RecipeAPI
import models.Ingredients
import models.Recipe
import mu.KotlinLogging
import persistence.YAMLSerializer
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
            4 -> listRecipe()
            5 -> searchRecipes()
            6 -> addIngredientToRecipe()
            7 -> deleteIngredient()
            20 -> save()
            21 -> load()
            0 -> exitApp()
            else -> logger.info("Invalid menu choice: $option")
        }
    } while (true)
}

fun mainMenu() = readNextInt(
    """ 
         > -----------------------------------------------------  
         > |                  RECIPE APP                       |
         > -----------------------------------------------------  
         > | RECIPE MENU                                       |
         > |   1) Add a recipe                                 |
         > |   2) Delete a recipe                              |
         > |   3) Update recipes                                 |
         > |   4) List recipes                                 |
         > |   5) Search recipes                               |
         > -----------------------------------------------------  
         > | ITEM MENU                                         | 
         > -----------------------------------------------------  
         > |   6) Add ingredients                              |
         > |   7) Delete ingredient                            |
         > -----------------------------------------------------
         > |   20) Save all recipes and ingredients            |
         > |   21) Load all recipes and ingredients            |
         > |   0) Exit                                         |
         > -----------------------------------------------------  
         > ==>> """.trimMargin(">")
)
//------------
// RECIPE MENU
//------------
fun addRecipe() {
    val recipeTitle = readNextLine("Enter a title for the recipe: ")
    val cookingTime = readNextInt("Enter the cooking time (minutes): ")
    val difficultyLevel = readNextLine("Enter the difficulty level: ")
    val isRecipeVegan = readNextBoolean("Is the recipe vegan? (true/false) ")
    val recipeCreator = readNextLine("Enter the creator name: ")
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

fun updateRecipe() {
    listRecipe()
    if (recipeAPI.numberOfRecipes() > 0) {
        val id = readNextInt("Enter the id of the recipe to update: ")
        if (recipeAPI.findRecipe(id) != null){
            val recipeTitle = readNextLine("Enter a title for the recipe: ")
            val cookingTime = readNextInt("Enter the cooking time (minutes): ")
            val difficultyLevel = readNextLine("Enter the difficulty level: ")
            val isRecipeVegan = readNextBoolean("Is the recipe vegan? (true/false) ")
            val recipeCreator = readNextLine("Enter the creator name: ")

            if (recipeAPI.updateRecipe(id, Recipe(0, recipeTitle, cookingTime, difficultyLevel, isRecipeVegan, recipeCreator ))) {
                println("Update Successful")
            } else {
                logger.info("Update Failed")
            }
        }
    } else {
        logger.info("There are no recipes for this index number")
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


private fun askUserToChooseRecipe(): Recipe? {
    listRecipe()
    if (recipeAPI.numberOfRecipes() > 0) {
        val recipe = recipeAPI.findRecipe(readNextInt("\nEnter the id of the recipe: "))
        if (recipe != null) {
            return recipe
        } else {
            println("Recipe id is not valid")
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
