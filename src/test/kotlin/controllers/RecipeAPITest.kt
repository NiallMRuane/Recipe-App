package controllers


import models.Recipe
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import persistence.JSONSerializer
import persistence.XMLSerializer
import persistence.YAMLSerializer
import java.io.File
import kotlin.test.assertEquals

class RecipeAPITest {

    private var verySmall: Recipe? = null
    private var somethingFilling: Recipe? = null
    private var feedsMany: Recipe? = null
    private var veryTasty: Recipe? = null
    private var somethingHealthy: Recipe? = null
    private var populatedRecipes: RecipeAPI? = RecipeAPI(YAMLSerializer(File("recipe.yaml")))
    private var emptyRecipes: RecipeAPI? = RecipeAPI(YAMLSerializer(File("recipe.yaml")))


    @BeforeEach
    fun setup() {
        verySmall = Recipe(0, "Cheesy Tacos", 30, "Easy", false, 250, "Niall")
        somethingFilling = Recipe(1, "Cheesy Pasta", 15, "Easy", true, 500, "Rob")
        feedsMany = Recipe(2, "Lasagna", 45, "Hard", false, 1000, "Christina")
        veryTasty = Recipe(3, "Pizza", 20, "Medium", false, 800, "Amy")
        somethingHealthy = Recipe(4, "Salad", 10, "Easy", true, 100, "Kieran")

        //adding 5 Recipes to the recipe api
        populatedRecipes!!.add(verySmall!!)
        populatedRecipes!!.add(somethingFilling!!)
        populatedRecipes!!.add(feedsMany!!)
        populatedRecipes!!.add(veryTasty!!)
        populatedRecipes!!.add(somethingHealthy!!)
    }

    @AfterEach
    fun tearDown() {
        verySmall = null
        somethingFilling = null
        feedsMany = null
        veryTasty = null
        somethingHealthy = null
        populatedRecipes = null
        emptyRecipes = null
    }

    @Nested
    inner class AddRecipes {
        @Test
        fun `adding a Recipe to a populated list adds to ArrayList`() {
            val newRecipe = Recipe(0, "Grilled Cheese", 10, "Easy", true, 100, "Niall")
            assertEquals(5, populatedRecipes!!.numberOfRecipes())
            assertTrue(populatedRecipes!!.add(newRecipe))
            assertEquals(6, populatedRecipes!!.numberOfRecipes())
            assertEquals(newRecipe, populatedRecipes!!.findRecipe(populatedRecipes!!.numberOfRecipes() - 1))
        }

        @Test
        fun `adding a Recipe to an empty list adds to ArrayList`() {
            val newRecipe = Recipe(0, "Grilled Cheese", 10, "Easy", true, 100, "Niall")
            assertEquals(0, emptyRecipes!!.numberOfRecipes())
            assertTrue(emptyRecipes!!.add(newRecipe))
            assertEquals(1, emptyRecipes!!.numberOfRecipes())
            assertEquals(newRecipe, emptyRecipes!!.findRecipe(emptyRecipes!!.numberOfRecipes() - 1))
        }
    }

    @Nested
    inner class DeleteRecipes {

        @Test
        fun `deleting a Recipe that does not exist, returns false`() {
            assertFalse(emptyRecipes!!.delete(0))
            assertFalse(populatedRecipes!!.delete(-1))
            assertFalse(populatedRecipes!!.delete(5))
        }

        @Test
        fun `deleting a Recipe that exists delete and returns true`() {
            assertEquals(5, populatedRecipes!!.numberOfRecipes())
            assertTrue(populatedRecipes!!.delete(4))
            assertEquals(4, populatedRecipes!!.numberOfRecipes())
            assertTrue(populatedRecipes!!.delete(0))
            assertEquals(3, populatedRecipes!!.numberOfRecipes())
        }
    }

    @Nested
    inner class UpdateRecipes {
        @Test
        fun `updating a Recipe that does not exist returns false`(){
            assertFalse(populatedRecipes!!.updateRecipe(6, Recipe(0, "Updating Recipe", 10, "Easy", true, 500, "Sharon")))
            assertFalse(populatedRecipes!!.updateRecipe(-1, Recipe(1, "Updating Recipe", 20, "Medium", false, 600, "Siobhan" )))
            assertFalse(emptyRecipes!!.updateRecipe(0, Recipe(2, "Updating Recipe", 30, "Hard",true, 700, "Sandra" )))
        }

        @Test
        fun `updating a Recipe that exists returns true and updates`() {
            //check recipe 5 exists and check the contents
            assertEquals(somethingHealthy, populatedRecipes!!.findRecipe(4))
            assertEquals("Salad", populatedRecipes!!.findRecipe(4)!!.recipeTitle)
            assertEquals(10, populatedRecipes!!.findRecipe(4)!!.cookingTime)
            assertEquals("Easy", populatedRecipes!!.findRecipe(4)!!.difficultyLevel)
            assertEquals(100, populatedRecipes!!.findRecipe(4)!!.calories)
            assertEquals("Kieran", populatedRecipes!!.findRecipe(4)!!.recipeCreator)

            //update recipe 5 with new information and ensure contents updated successfully
            assertTrue(populatedRecipes!!.updateRecipe(4, Recipe(4, "Updating Recipe", 40, "simple", false, 800, "Sally")))
            assertEquals("Updating Recipe", populatedRecipes!!.findRecipe(4)!!.recipeTitle)
            assertEquals(40, populatedRecipes!!.findRecipe(4)!!.cookingTime)
            assertEquals("simple", populatedRecipes!!.findRecipe(4)!!.difficultyLevel)
            assertEquals(800, populatedRecipes!!.findRecipe(4)!!.calories)
            assertEquals("Sally", populatedRecipes!!.findRecipe(4)!!.recipeCreator)
        }
    }

    @Nested
    inner class ListRecipe {


        @Test
        fun `listRecipes returns No Recipes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyRecipes!!.numberOfRecipes())
            assertTrue(emptyRecipes!!.listRecipes().lowercase().contains("no recipes"))
        }

        @Test
        fun `listRecipes returns Recipes when ArrayList has recipes stored`() {
            assertEquals(5, populatedRecipes!!.numberOfRecipes())
            val recipesString = populatedRecipes!!.listRecipes().lowercase()
            assertTrue(recipesString.contains("Cheesy Tacos", ignoreCase = true))
            assertTrue(recipesString.contains("Cheesy Pasta", ignoreCase = true))
            assertTrue(recipesString.contains("Lasagna", ignoreCase = true))
            assertTrue(recipesString.contains("Pizza", ignoreCase = true))
            assertTrue(recipesString.contains("Salad", ignoreCase = true))
        }
        @Test
        fun `listNonVeganRecipes returns no non vegan recipes stored when ArrayList is empty`() {
            assertEquals(0, emptyRecipes!!.numberOfNonVeganRecipes())
            assertTrue(
                emptyRecipes!!.listNonVeganRecipes().lowercase().contains("no non vegan recipes")
            )
        }
        @Test
        fun `listNonVeganRecipes returns non vegan recipes when ArrayList has non vegan recipes stored`() {
            assertEquals(3, populatedRecipes!!.numberOfNonVeganRecipes())
            val nonVeganRecipesString = populatedRecipes!!.listNonVeganRecipes().lowercase()
            assertTrue(nonVeganRecipesString.contains("Cheesy Tacos", ignoreCase = true))
            Assertions.assertFalse(nonVeganRecipesString.contains("Cheesy Pasta", ignoreCase = true))
            assertTrue(nonVeganRecipesString.contains("Lasagna", ignoreCase = true))
            assertTrue(nonVeganRecipesString.contains("Pizza", ignoreCase = true))
            Assertions.assertFalse(nonVeganRecipesString.contains("Salad", ignoreCase = true))
        }
        @Test
        fun `listVeganRecipes returns no vegan recipes when ArrayList is empty`() {
            assertEquals(0, emptyRecipes!!.numberOfVeganRecipes())
            assertTrue(
                emptyRecipes!!.listVeganRecipes().lowercase().contains("no vegan recipes")
            )
        }

        @Test
        fun `listVeganRecipes returns no vegan recipes when ArrayList has no vegan recipes stored`() {
            assertEquals(2, populatedRecipes!!.numberOfVeganRecipes())
            val veganRecipesString = populatedRecipes!!.listVeganRecipes().lowercase()
            Assertions.assertFalse(veganRecipesString.contains("Cheesy Tacos", ignoreCase = true))
            assertTrue(veganRecipesString.contains("Cheesy Pasta", ignoreCase = true))
            Assertions.assertFalse(veganRecipesString.contains("Lasagna", ignoreCase = true))
            Assertions.assertFalse(veganRecipesString.contains("Pizza", ignoreCase = true))
            assertTrue(veganRecipesString.contains("Salad", ignoreCase = true))
        }

    }

    @Nested
    inner class markRecipeVegan {
        @Test
        fun `marking a recipe as vegan that does not exist returns false`(){
            assertFalse(populatedRecipes!!.markRecipeVegan(6))
            assertFalse(populatedRecipes!!.markRecipeVegan(-1))
            assertFalse(emptyRecipes!!.markRecipeVegan(0))
        }

        @Test
        fun `marking an already marked recipe returns true`(){
            assertTrue(populatedRecipes!!.findRecipe(1)!!.isRecipeVegan)
            assertFalse(populatedRecipes!!.markRecipeVegan(1))
        }

        @Test
        fun `marking a recipe as vegan that exists returns true and marks`() {
            assertFalse(populatedRecipes!!.findRecipe(0)!!.isRecipeVegan)
            assertTrue(populatedRecipes!!.markRecipeVegan(0))
            assertTrue(populatedRecipes!!.findRecipe(0)!!.isRecipeVegan)
        }
    }

    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfRecipesCalculatedCorrectly() {
            assertEquals(5, populatedRecipes!!.numberOfRecipes())
            assertEquals(0, emptyRecipes!!.numberOfRecipes())
        }

        @Test
        fun numberOfVeganRecipesCalculatedCorrectly() {
            assertEquals(2, populatedRecipes!!.numberOfVeganRecipes())
            assertEquals(0, emptyRecipes!!.numberOfVeganRecipes())
        }

        @Test
        fun numberOfNonVeganRecipesCalculatedCorrectly() {
            assertEquals(3, populatedRecipes!!.numberOfNonVeganRecipes())
            assertEquals(0, emptyRecipes!!.numberOfNonVeganRecipes())
        }
    }

    @Nested
    inner class SearchMethods {

        @Test
        fun `search recipes by title returns no recipes when no recipes with that title exist`() {
            //Searching a populated collection for a title that doesn't exist.
            assertEquals(5, populatedRecipes!!.numberOfRecipes())
            val searchResults = populatedRecipes!!.searchByTitle("no results expected")
            assertTrue(searchResults.isEmpty())

            //Searching an empty collection
            assertEquals(0, emptyRecipes!!.numberOfRecipes())
            assertTrue(emptyRecipes!!.searchByTitle("").isEmpty())
        }

        @Test
        fun `search recipes by title returns recipes when recipes with that title exist`() {
            assertEquals(5, populatedRecipes!!.numberOfRecipes())

            //Searching a populated collection for a full title that exists (case matches exactly)
            var searchResults = populatedRecipes!!.searchByTitle("Lasagna")
            assertTrue(searchResults.contains("Lasagna"))
            assertFalse(searchResults.contains("Pizza"))

            //Searching a populated collection for a partial title that exists (case matches exactly)
            searchResults = populatedRecipes!!.searchByTitle("Cheesy")
            assertTrue(searchResults.contains("Cheesy Tacos"))
            assertTrue(searchResults.contains("Cheesy Pasta"))
            assertFalse(searchResults.contains("Lasagna"))

            //Searching a populated collection for a partial title that exists (case doesn't match)
            searchResults = populatedRecipes!!.searchByTitle("CheESy")
            assertTrue(searchResults.contains("Cheesy Tacos"))
            assertTrue(searchResults.contains("Cheesy Pasta"))
            assertFalse(searchResults.contains("Lasagna"))
        }

        @Test
        fun `search recipes by cooking time returns no recipes when no recipes with that cook time exist`() {
            //Searching a populated collection for a cooking time that doesn't exist.
            assertEquals(5, populatedRecipes!!.numberOfRecipes())
            val searchResults = populatedRecipes!!.searchByCookingTime(0)
            assertTrue(searchResults.isEmpty())

            //Searching an empty collection
            assertEquals(0, emptyRecipes!!.numberOfRecipes())
            assertTrue(emptyRecipes!!.searchByCookingTime(0).isEmpty())
        }

        @Test
        fun `search recipes by cooking time returns recipes when recipes with that cooking time exist`() {
            assertEquals(5, populatedRecipes!!.numberOfRecipes())

            //Searching a populated collection for a full cooking time that exists (case matches exactly)
            var searchResults = populatedRecipes!!.searchByCookingTime(20)
            assertTrue(searchResults.contains("Pizza"))
            assertFalse(searchResults.contains("Cheesy Pasta"))
            assertFalse(searchResults.contains("Lasagna"))

            searchResults = populatedRecipes!!.searchByCookingTime(10)
            assertFalse(searchResults.contains("Pizza"))
            assertFalse(searchResults.contains("Cheesy Tacos"))
            assertTrue(searchResults.contains("Salad"))

        }

        @Test
        fun `search recipes by difficulty level returns no recipes when no recipes with that difficulty level exist`() {
            //Searching a populated collection for a difficulty level that doesn't exist.
            assertEquals(5, populatedRecipes!!.numberOfRecipes())
            val searchResults = populatedRecipes!!.searchByDifficultyLevel("no results expected")
            assertTrue(searchResults.isEmpty())

            //Searching an empty collection
            assertEquals(0, emptyRecipes!!.numberOfRecipes())
            assertTrue(emptyRecipes!!.searchByDifficultyLevel("").isEmpty())
        }

        @Test
        fun `search recipes by difficulty level returns recipes when recipes with that difficulty level exist`() {
            assertEquals(5, populatedRecipes!!.numberOfRecipes())

            //Searching a populated collection for a full difficulty level that exists (case matches exactly)
            var searchResults = populatedRecipes!!.searchByDifficultyLevel("Easy")
            assertTrue(searchResults.contains("Cheesy Tacos"))
            assertFalse(searchResults.contains("Pizza"))

        }

        @Test
        fun `search recipes by calories returns no recipes when no recipes with that calories exist`() {
            //Searching a populated collection for a cooking time that doesn't exist.
            assertEquals(5, populatedRecipes!!.numberOfRecipes())
            val searchResults = populatedRecipes!!.searchByCalories(0)
            assertTrue(searchResults.isEmpty())

            //Searching an empty collection
            assertEquals(0, emptyRecipes!!.numberOfRecipes())
            assertTrue(emptyRecipes!!.searchByCookingTime(0).isEmpty())
        }

        @Test
        fun `search recipes by calories returns recipes when recipes with that calories exist`() {
            assertEquals(5, populatedRecipes!!.numberOfRecipes())

            //Searching a populated collection for a full calories that exists (case matches exactly)
            var searchResults = populatedRecipes!!.searchByCalories(500)
            assertTrue(searchResults.contains("Cheesy Pasta"))
            assertFalse(searchResults.contains("Pizza"))
            assertFalse(searchResults.contains("Lasagna"))

            searchResults = populatedRecipes!!.searchByCalories(100)
            assertFalse(searchResults.contains("Pizza"))
            assertFalse(searchResults.contains("Cheesy Tacos"))
            assertTrue(searchResults.contains("Salad"))

        }
/*
        @Test
        fun `sort recipes by calories returns recipes when recipes with that calories exist`() {
            assertEquals(5, populatedRecipes!!.numberOfRecipes())

            //Searching a populated collection for a full calories that exists (case matches exactly)
            var searchResults = populatedRecipes!!.sortByCaloriesAsc(<=500)
            assertTrue(searchResults.contains("Salad"))
            assertTrue(searchResults.contains("Cheesy Tacos"))
            assertTrue(searchResults.contains("Cheesy Pasta"))
            assertFalse(searchResults.contains("Lasagna"))
            assertFalse(searchResults.contains("Pizza"))

            searchResults = populatedRecipes!!.sortByCaloriesAsc(<=100)
            assertFalse(searchResults.contains("Pizza"))
            assertFalse(searchResults.contains("Cheesy Tacos"))
            assertTrue(searchResults.contains("Salad"))

        }

 */


    }
    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty recipes.XML file.
            val storingRecipes = RecipeAPI(XMLSerializer(File("recipe.xml")))
            storingRecipes.store()

            //Loading the empty recipe.xml file into a new object
            val loadedRecipes = RecipeAPI(XMLSerializer(File("recipe.xml")))
            loadedRecipes.load()

            //Comparing the source of the recipes (storingRecipes) with the XML loaded recipes (loadedRecipes)
            assertEquals(0, storingRecipes.numberOfRecipes())
            assertEquals(0, loadedRecipes.numberOfRecipes())
            assertEquals(storingRecipes.numberOfRecipes(),loadedRecipes.numberOfRecipes())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 recipes to the recipe.XML file.
            val storingRecipes = RecipeAPI(XMLSerializer(File("recipe.xml")))
            storingRecipes.add(veryTasty!!)
            storingRecipes.add(somethingHealthy!!)
            storingRecipes.add(somethingFilling!!)
            storingRecipes.store()

            //Loading recipe.xml into a different collection
            val loadedRecipes = RecipeAPI(XMLSerializer(File("recipe.xml")))
            loadedRecipes.load()

            //Comparing the source of the recipes (storingRecipes) with the XML loaded recipes (loadedRecipes)
            assertEquals(3, storingRecipes.numberOfRecipes())
            assertEquals(3, loadedRecipes.numberOfRecipes())
            assertEquals(storingRecipes.numberOfRecipes(), loadedRecipes.numberOfRecipes())
            assertEquals(storingRecipes.findRecipe(0), loadedRecipes.findRecipe(0))
            assertEquals(storingRecipes.findRecipe(1), loadedRecipes.findRecipe(1))
            assertEquals(storingRecipes.findRecipe(2), loadedRecipes.findRecipe(2))
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty recipe.json file.
            val storingRecipes = RecipeAPI(JSONSerializer(File("recipe.json")))
            storingRecipes.store()

            //Loading the empty recipe.json file into a new object
            val loadingRecipes = RecipeAPI(JSONSerializer(File("recipe.json")))
            loadingRecipes.load()

            //Comparing the source of the recipes (storingRecipes) with the json loaded recipes (loadedRecipes)
            assertEquals(0, storingRecipes.numberOfRecipes())
            assertEquals(0, loadingRecipes.numberOfRecipes())
            assertEquals(storingRecipes.numberOfRecipes(), loadingRecipes.numberOfRecipes())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 recipes to the recipe.json file.
            val storingRecipes = RecipeAPI(JSONSerializer(File("recipe.json")))
            storingRecipes.add(veryTasty!!)
            storingRecipes.add(somethingHealthy!!)
            storingRecipes.add(somethingFilling!!)
            storingRecipes.store()

            //Loading recipe.json into a different collection
            val loadedRecipes = RecipeAPI(JSONSerializer(File("recipe.json")))
            loadedRecipes.load()

            //Comparing the source of the recipes (storingRecipes) with the json loaded recipes (loadedRecipes)
            assertEquals(3, storingRecipes.numberOfRecipes())
            assertEquals(3, loadedRecipes.numberOfRecipes())
            assertEquals(storingRecipes.numberOfRecipes(), loadedRecipes.numberOfRecipes())
            assertEquals(storingRecipes.findRecipe(0), loadedRecipes.findRecipe(0))
            assertEquals(storingRecipes.findRecipe(1), loadedRecipes.findRecipe(1))
            assertEquals(storingRecipes.findRecipe(2), loadedRecipes.findRecipe(2))
        }

        @Test
        fun `saving and loading an empty collection in YAML doesn't crash app`() {
            // Saving an empty recipe.yaml file.
            val storingRecipes = RecipeAPI(YAMLSerializer(File("recipe.yaml")))
            storingRecipes.store()

            //Loading the empty recipe.yaml file into a new object
            val loadedRecipes = RecipeAPI(YAMLSerializer(File("recipe.yaml")))
            loadedRecipes.load()

            //Comparing the source of the recipes (storingRecipes) with the yaml loaded recipes (loadedRecipes)
            assertEquals(0, storingRecipes.numberOfRecipes())
            assertEquals(0, loadedRecipes.numberOfRecipes())
            assertEquals(storingRecipes.numberOfRecipes(), loadedRecipes.numberOfRecipes())
        }

        @Test
        fun `saving and loading an loaded collection in YAML doesn't loose data`() {
            // Storing 3 recipes to the recipe.yaml file.
            val storingRecipes = RecipeAPI(YAMLSerializer(File("recipe.yaml")))
            storingRecipes.add(veryTasty!!)
            storingRecipes.add(somethingHealthy!!)
            storingRecipes.add(somethingFilling!!)
            storingRecipes.store()

            //Loading recipe.yaml into a different collection
            val loadedRecipes = RecipeAPI(YAMLSerializer(File("recipe.yaml")))
            loadedRecipes.load()

            //Comparing the source of the recipes (storingRecipes) with the yaml loaded recipes (loadedRecipes)
            assertEquals(3, storingRecipes.numberOfRecipes())
            assertEquals(3, loadedRecipes.numberOfRecipes())
            assertEquals(storingRecipes.numberOfRecipes(), loadedRecipes.numberOfRecipes())
            assertEquals(storingRecipes.findRecipe(0), loadedRecipes.findRecipe(0))
            assertEquals(storingRecipes.findRecipe(1), loadedRecipes.findRecipe(1))
            assertEquals(storingRecipes.findRecipe(2), loadedRecipes.findRecipe(2))
        }

    }
}

