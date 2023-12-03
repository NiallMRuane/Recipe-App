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
import kotlin.test.assertNull
class RecipeAPITest {

    private var verySmall: Recipe? = null
    private var somethingFilling: Recipe? = null
    private var feedsMany: Recipe? = null
    private var veryTasty: Recipe? = null
    private var somethingHealthy: Recipe? = null
    private var populatedNotes: RecipeAPI? = RecipeAPI(YAMLSerializer(File("recipe.yaml")))
    private var emptyNotes: RecipeAPI? = RecipeAPI(YAMLSerializer(File("recipe.yaml")))


    @BeforeEach
    fun setup() {
        verySmall = Recipe(0, "Tacos", 30, "Easy", false, 250, "Niall")
        somethingFilling = Recipe(1, "Pasta", 15, "Easy", true, 500, "Rob")
        feedsMany = Recipe(2, "Lasagna", 45, "Hard", false, 1000, "Christina")
        veryTasty = Recipe(3, "Pizza", 20, "Medium", false, 800, "Amy")
        somethingHealthy = Recipe(4, "Salad", 10, "Easy", true, 100, "Kieran")

        //adding 5 Note to the notes api
        populatedNotes!!.add(verySmall!!)
        populatedNotes!!.add(somethingFilling!!)
        populatedNotes!!.add(feedsMany!!)
        populatedNotes!!.add(veryTasty!!)
        populatedNotes!!.add(somethingHealthy!!)
    }











}