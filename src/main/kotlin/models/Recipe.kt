package models

data class Recipe (
    var recipeTitle: String,
    var cookingTime: Int,  //minutes
    var difficultyLevel: String,
    var isRecipeVegan: Boolean,
    var recipeCreator: String
)
