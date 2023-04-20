package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.connections.wrappers.SelectQueryWrapper;
import com.forgefrontier.forgefrontier.crafting.FFRecipe;
import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

public class CraftingRecipeDB extends DBConnection {

    public CraftingRecipeDB(Connection existingConn) {
        super(existingConn);
    }

    /**
     * Loads recipes directly into CraftingManager
     * WARNING: Do not modify the array for sake of concurrency immediately after this runs
     * Loads in a different thread to speed up initialization
     */
    public void loadRecipes() {
        ArrayList<FFRecipe> recipes = new ArrayList<>();

        SelectQueryWrapper wrapper = new SelectQueryWrapper();
        wrapper.setTable("recipe");
        wrapper.setFields(
                "mat1", "mat2", "mat3", "mat4", "mat5", "mat6", "mat7", "mat8", "mat9",
                "type1", "type2", "type3", "type4", "type5", "type6", "type7", "type8", "type9",
                "amt1", "amt2", "amt3", "amt4", "amt5", "amt6", "amt7", "amt8", "amt9",
                "output", "out_type", "output_amount");
        wrapper.executeAsyncQuery(dbConn, this::parseRecipeResult);
    }
    void parseRecipeResult(ResultSet rs) {
        try {
            int nRecipes = 0;
            while (rs.next()) {
                int colIdx = 1;
                String[] mats = new String[9];
                String[] metas = new String[9];
                int[] amts = new int[9];
                String output;
                String outputMeta;
                int outAmt;
                for (int i = 0; i < 9; i++) {
                    mats[i] = rs.getString(colIdx++);
                }
                for (int i = 0; i < 9; i++) {
                    metas[i] = rs.getString(colIdx++);
                }
                for (int i = 0; i < 9; i++) {
                    amts[i] = rs.getInt(colIdx++);
                }
                output = rs.getString(colIdx++);
                outputMeta = rs.getString(colIdx++);
                outAmt = rs.getInt(colIdx);

                ItemStack[] recipeComponents = new ItemStack[9];
                FFRecipe.SLOT_META[] recipeMeta = new FFRecipe.SLOT_META[9];
                boolean failure = false;
                for (int i = 0; i < 9; i++) {
                    ItemStack itm = null;
                    if (metas[i].equals("C")) {
                        recipeMeta[i] = FFRecipe.SLOT_META.CUSTOM;
                        CustomItem ci = CustomItemManager.getCustomItem(mats[i]);
                        if (ci == null) {
                            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                                    "Failed to load custom material: " + mats[i]);
                            failure = true;
                            break;
                        }
                        itm = ci.asInstance(null).asItemStack();
                    } else {
                        if (metas[i].equals("V")) { recipeMeta[i] = FFRecipe.SLOT_META.VANILLA; }
                        else recipeMeta[i] = FFRecipe.SLOT_META.ANY;
                        if (mats[i] == null) { recipeComponents[i] = null; continue;};
                        Material m = Material.getMaterial(mats[i]);
                        if (m == null) {
                            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                                    "Failed to load recipe with material: " + mats[i]);
                            continue;
                        }
                        itm = new ItemStack(m);
                    }
                    if (itm != null)
                        itm.setAmount(amts[i]);
                    recipeComponents[i] = itm;
                }
                if (failure) continue;

                ItemStack outItem;
                if (outputMeta.equals("C")) {
                    CustomItem ci = CustomItemManager.getCustomItem(output);
                    if (ci == null) {
                        ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                                "Failed to load custom material: " + output);
                        continue;
                    }
                    outItem = ci.asInstance(null).asItemStack();
                } else {
                    Material m = Material.getMaterial(output);
                    if (m == null) {
                        ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                                "Failed to load recipe with material: " + output);
                        failure = true;
                        break;
                    }
                    outItem = new ItemStack(m);
                }
                outItem.setAmount(outAmt);
                FFRecipe nRec = new FFRecipe(recipeComponents,outItem);
                nRec.setRecipeMeta(recipeMeta);
                ForgeFrontier.getInstance().getCraftingManager().addRecipe(nRec);
                nRecipes++;
            }
            ForgeFrontier.getInstance().getLogger().log(Level.INFO,
                    "Finished loading " + nRecipes + " Custom Recipes");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void addRecipeDBAsync(FFRecipe recipe) {
        new Thread(() -> addRecipeToDBSync(recipe)).start();
    }
    public void addRecipeToDBSync(FFRecipe recipe) {
        String[] components = new String[9];
        String[] meta = new String[9];
        int[] amounts = new int[9];

        String output;
        String outputType;

        ItemStack[] itmComponents = recipe.getComponents();
        FFRecipe.SLOT_META[] itmMeta = recipe.getRecipeMeta();
        for (int i = 0; i < 9; i++) {
            if (itmMeta[i] == FFRecipe.SLOT_META.CUSTOM) {
                meta[i] = "C";
                CustomItemInstance cii = CustomItemManager.asCustomItemInstance(itmComponents[i]);
                if (cii == null) {
                    ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                            "Failed to save recipe -- Could not load recipe component as custom item.");
                    return;
                }
                components[i] = cii.getBaseItem().getCode();
                amounts[i] = itmComponents[i].getAmount();
            } else {
                if (itmMeta[i] == FFRecipe.SLOT_META.VANILLA) meta[i] = "V";
                else meta[i] = "A";
                ItemStack itm = itmComponents[i];
                if (itm == null || itm.getType() == Material.AIR) {
                    components[i] = null;
                    continue;
                }
                components[i] = itm.getType().toString();
                amounts[i] = itmComponents[i].getAmount();
            }
        }
        ItemStack outputItem = recipe.getResult();
        CustomItemInstance coutput = CustomItemManager.asCustomItemInstance(outputItem);
        if (coutput != null) {
            outputType = "C";
            output = coutput.getBaseItem().getCode();
        } else {
            outputType = "V";
            output = outputItem.getType().toString();
        }
        int outputAmount = outputItem.getAmount();
        String query = "INSERT INTO recipe " +
                "(mat1, mat2, mat3, mat4, mat5, mat6, mat7, mat8, mat9, " +
                "type1, type2, type3, type4, type5, type6, type7, type8, type9," +
                "amt1, amt2, amt3, amt4, amt5, amt6, amt7, amt8, amt9, " +
                "output, out_type, output_amount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?)";
        try {
            PreparedStatement preparedStatement = dbConn.prepareStatement(query);
            int idx = 1;
            for (int i = 0; i < 9; i++) {
                preparedStatement.setString(idx++, components[i]);
            }
            for (int i = 0; i < 9; i++) {
                preparedStatement.setString(idx++, meta[i]);
            }
            for (int i = 0; i < 9; i++) {
                preparedStatement.setInt(idx++, amounts[i]);
            }
            preparedStatement.setString(idx++, output);
            preparedStatement.setString(idx++, outputType);
            preparedStatement.setInt(idx, outputAmount);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                    "[QUERY FAILURE] Recipe Insert\n" + e.getMessage());
        }
    }







}
