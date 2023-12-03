package controllers

import controllers.RecipeAPI
import models.Recipe
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import persistence.JSONSerializer
import persistence.XMLSerializer
import persistence.YAMLSerializer
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNull
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
        verySmall = Recipe(0, "Tacos", 30, "Easy", false, 250, "Niall")
        somethingFilling = Recipe(1, "Pasta", 15, "Easy", true, 500, "Rob")
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
            assertTrue(recipesString.contains("Tacos", ignoreCase = true))
            assertTrue(recipesString.contains("Pasta", ignoreCase = true))
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
            assertTrue(nonVeganRecipesString.contains("Tacos", ignoreCase = true))
            Assertions.assertFalse(nonVeganRecipesString.contains("Pasta", ignoreCase = true))
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
            Assertions.assertFalse(veganRecipesString.contains("Tacos", ignoreCase = true))
            assertTrue(veganRecipesString.contains("Pasta", ignoreCase = true))
            Assertions.assertFalse(veganRecipesString.contains("Lasagna", ignoreCase = true))
            Assertions.assertFalse(veganRecipesString.contains("Pizza", ignoreCase = true))
            assertTrue(veganRecipesString.contains("Salad", ignoreCase = true))
        }

    }
}