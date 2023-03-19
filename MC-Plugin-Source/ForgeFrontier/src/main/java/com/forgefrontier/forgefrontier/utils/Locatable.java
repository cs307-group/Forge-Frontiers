package com.forgefrontier.forgefrontier.utils;

public interface Locatable {

    int getX();
    int getY();
    int getZ();

    boolean isAt(int x, int y, int z);

}
