package com.forgefrontier.forgefrontier.fishing;

import java.util.HashMap;
import java.util.UUID;

public class PlayerFishStat {

    UUID playerID;

    private long fishCaught;
    private int level;
    private int common;
    private int uncommon;
    private int rare;
    private int superrare;
    private int ultrarare;
    private int legendary;
    private boolean modified = false;

    public PlayerFishStat(UUID playerID, long fishCaught, int level, int common, int uncommon,
                          int rare, int superrare, int ultrarare, int legendary, boolean modified) {
        this.playerID = playerID;
        this.fishCaught = fishCaught;
        this.level = level;
        this.modified = modified;
        this.common = common;
        this.uncommon = uncommon;
        this.rare = rare;
        this.superrare = superrare;
        this.ultrarare = ultrarare;
        this.legendary = legendary;
    }


    public PlayerFishStat(UUID playerID, long fishCaught, int level, boolean modified) {
        this.playerID = playerID;
        this.fishCaught = fishCaught;
        this.level = level;
        this.modified = modified;
        this.common = 0;
        this.uncommon = 0;
        this.rare = 0;
        this.superrare = 0;
        this.ultrarare = 0;
        this.legendary = 0;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
        modified = true;
    }

    public long getFishCaught() {
        return fishCaught;
    }

    public void setFishCaught(long fishCaught) {
        this.fishCaught = fishCaught;
        modified = true;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        modified = true;
    }

    public void incrementFishCaught(int rarity) {
        switch (rarity) {
            case 0 -> common++;
            case 1 -> uncommon++;
            case 2 -> rare++;
            case 3 -> superrare++;
            case 4 -> ultrarare++;
            default -> legendary++;
        }
        fishCaught++;
        modified = true;
    }

    public void setModified(boolean mod) {
        this.modified = mod;
    }

    public boolean isModified() { return this.modified; }


    public int getCommon() {
        return common;
    }

    public void setCommon(int common) {
        this.common = common;
    }

    public int getUncommon() {
        return uncommon;
    }

    public void setUncommon(int uncommon) {
        this.uncommon = uncommon;
    }

    public int getRare() {
        return rare;
    }

    public void setRare(int rare) {
        this.rare = rare;
    }

    public int getSuperrare() {
        return superrare;
    }

    public void setSuperrare(int superrare) {
        this.superrare = superrare;
    }

    public int getUltrarare() {
        return ultrarare;
    }

    public void setUltrarare(int ultrarare) {
        this.ultrarare = ultrarare;
    }

    public int getLegendary() {
        return legendary;
    }

    public void setLegendary(int legendary) {
        this.legendary = legendary;
    }
}
