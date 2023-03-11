package com.forgefrontier.forgefrontier.utils;

import com.forgefrontier.forgefrontier.ForgeFrontier;

public abstract class Manager {

    /**
     * Instance of the ForgeFrontier plugin class to access all other managers easily.
     */
    protected ForgeFrontier plugin;

    public Manager(ForgeFrontier plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs whenever the plugin enables the module. You can safely access other modules here, but no guarantee data will be in them.
     */
    public abstract void init();

    /**
     * Runs whenever the plugin disables the module. Assume all data in RAM will be deleted after this is ran.
     */
    public abstract void disable();

}
