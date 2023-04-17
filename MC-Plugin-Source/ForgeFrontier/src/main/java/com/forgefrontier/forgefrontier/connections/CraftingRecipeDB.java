package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.crafting.FFRecipe;

import java.sql.Connection;
import java.util.ArrayList;

public class CraftingRecipeDB extends DBConnection {

    public CraftingRecipeDB(Connection existingConn) {
        super(existingConn);
    }

    public ArrayList<FFRecipe> getRecipes() {
        ArrayList<FFRecipe> recipes = new ArrayList<>();
        return recipes;
    }



}
