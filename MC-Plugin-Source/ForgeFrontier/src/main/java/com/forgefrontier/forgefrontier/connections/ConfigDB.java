package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.connections.wrappers.SelectQueryWrapper;
import com.forgefrontier.forgefrontier.generators.Generator;
import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.GeneralCustomItem;
import com.forgefrontier.forgefrontier.stashes.Stash;
import com.forgefrontier.forgefrontier.utils.JSONWrapper;
import org.bukkit.Material;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConfigDB extends DBConnection {

    public ConfigDB(Connection existingConn) {
        super(existingConn);
    }


    public void loadItems(Consumer<List<GeneralCustomItem>> consumer) {
        SelectQueryWrapper wrapper = new SelectQueryWrapper();
        wrapper.setTable("public.items");
        wrapper.setFields(
            "item_id",
            "material",
            "name",
            "lore"
        );
        ResultSet resultSet = wrapper.executeSyncQuery(this.dbConn);
        List<GeneralCustomItem> items = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String itemId = resultSet.getString(1);
                String material = resultSet.getString(2);
                String name = resultSet.getString(3);
                String lore = resultSet.getString(4);
                GeneralCustomItem item = new GeneralCustomItem(itemId, material, name, lore);
                items.add(item);
            }
        } catch(SQLException e) {
            ForgeFrontier.getInstance().getLogger().severe("Unable to import items from database.");
        }
        consumer.accept(items);
    }

    public void loadGenerators(Consumer<List<Generator>> consumer) {
        SelectQueryWrapper wrapper = new SelectQueryWrapper();
        wrapper.setTable("public.generator_config");
        wrapper.setFields(
            "generator_id",
            "friendly_name",
            "block_material",
            "resource_item_id",
            "costs",
            "levels"
        );

        ResultSet resultSet = wrapper.executeSyncQuery(this.dbConn);
        List<Generator> generators = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String generatorId = resultSet.getString(1);
                String friendlyName = resultSet.getString(2);
                Material blockMaterial = Material.matchMaterial(resultSet.getString(3));
                CustomItem resourceItem = CustomItemManager.getCustomItem(resultSet.getString(4));
                List<JSONWrapper> costs = JSONWrapper.parseList(resultSet.getString(5));
                List<JSONWrapper> levels = JSONWrapper.parseList(resultSet.getString(6));
                generators.add(new Generator(generatorId, friendlyName, blockMaterial, resourceItem, costs, levels));
            }
        } catch(SQLException e) {
            ForgeFrontier.getInstance().getLogger().severe("Unable to import items from database.");
        }
        consumer.accept(generators);
    }

    public void loadStash(Consumer<List<Stash>> consumer) {
        SelectQueryWrapper wrapper = new SelectQueryWrapper();
        wrapper.setTable("public.stash_config");
        wrapper.setFields(
                "stash_id",
                "friendly_name",
                "block_material",
                "costs",
                "contents"
        );

        ResultSet resultSet = wrapper.executeSyncQuery(this.dbConn);
        List<Stash> stashes = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String stashId = resultSet.getString(1);
                String friendlyName = resultSet.getString(2);
                Material blockMaterial = Material.matchMaterial(resultSet.getString(3));
                List<JSONWrapper> costs = JSONWrapper.parseList(resultSet.getString(4));
                List<JSONWrapper> contents = JSONWrapper.parseList(resultSet.getString(5));
                stashes.add(new Stash(stashId, friendlyName, blockMaterial, costs, contents));
            }
        } catch(SQLException e) {
            ForgeFrontier.getInstance().getLogger().severe("Unable to import items from database.");
        }
        consumer.accept(stashes);
    }

}
