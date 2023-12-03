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

    private var learnKotlin: Recipe? = null
    private var summerHoliday: Recipe? = null
    private var codeApp: Recipe? = null
    private var testApp: Recipe? = null
    private var swim: Recipe? = null
    private var populatedNotes: RecipeAPI? = RecipeAPI(YAMLSerializer(File("recipe.yaml")))
    private var emptyNotes: RecipeAPI? = RecipeAPI(YAMLSerializer(File("recipe.yaml")))
}