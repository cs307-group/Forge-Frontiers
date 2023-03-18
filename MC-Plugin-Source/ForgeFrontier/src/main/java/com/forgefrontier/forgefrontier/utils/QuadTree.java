package com.forgefrontier.forgefrontier.utils;

import com.forgefrontier.forgefrontier.ForgeFrontier;

import java.util.Arrays;

public class QuadTree<T extends Locatable> {

    T[] items;
    int x;
    int y;
    int z;

    int w;
    int h;
    int l;

    QuadTree[][][] subtrees;
    int totalAmt = 0;

    public QuadTree(int x, int y, int z, int w, int h, int l) {
        this.items = (T[]) new Locatable[4];
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        this.h = h;
        this.l = l;
        this.totalAmt = 0;
    }

    public T get(int x, int y, int z) {
        for(T item: items) {
            if(item == null) return null;
            if(item.isAt(x, y, z)) {
                return item;
            }
        }
        if(this.subtrees == null)
            return null;
        int i = 0;
        int j = 0;
        int k = 0;
        if(w > 1 && (x - this.x) >= w/2) {
            i = 1;
        }
        if(h > 1 && (y - this.y) >= h/2) {
            h = 1;
        }
        if(l > 1 && (z - this.z) >= l/2) {
            l = 1;
        }
        QuadTree<T> subtree = this.subtrees[i][j][k];
        if(subtree == null)
            return null;
        return subtree.get(x, y, z);
    }

    public void insert(T item) {
        for(int i = 0; i < items.length; i++) {
            if(items[i] == null) {
                items[i] = item;
                this.totalAmt += 1;
                return;
            }
        }
        if(this.subtrees == null) {
            this.subtrees = new QuadTree
                [this.w > 1 ? 2 : 1]
                [this.h > 1 ? 2 : 1]
                [this.l > 1 ? 2 : 1];
        }
        int i = item.getX() >= (this.x + this.w / 2) ? 1 : 0;
        int j = item.getY() >= (this.y + this.h / 2) ? 1 : 0;
        int k = item.getZ() >= (this.z + this.l / 2) ? 1 : 0;
        if(this.w <= 1) i = 0;
        if(this.h <= 1) j = 0;
        if(this.l <= 1) k = 0;
        if(this.subtrees[i][j][k] == null)
            this.subtrees[i][j][k] = new QuadTree<T>(
                this.x + i * this.w/2,
                this.y + i * this.h/2,
                this.z + i * this.l/2,
                this.w <= 1 ? 1 : (i == 0 ? this.w / 2 : (this.w - this.w / 2)),
                this.h <= 1 ? 1 : (j == 0 ? this.h / 2 : (this.h - this.h / 2)),
                this.l <= 1 ? 1 : (k == 0 ? this.l / 2 : (this.l - this.l / 2))
            );
        this.subtrees[i][j][k].insert(item);
        this.totalAmt += 1;
    }

    public void remove(T item) {
        for(int i = 0; i < items.length; i++) {
            if(items[i] == item) {
                items[i] = null;
                this.totalAmt -= 1;
                return;
            }
        }
        if(this.subtrees == null) {
            ForgeFrontier.getInstance().getLogger().severe("Unable to find Generator Instance to remove.");
            return;
        }
        int i = item.getX() >= (this.x + this.w / 2) ? 1 : 0;
        int j = item.getY() >= (this.y + this.h / 2) ? 1 : 0;
        int k = item.getZ() >= (this.z + this.l / 2) ? 1 : 0;
        if(this.w <= 1) i = 0;
        if(this.h <= 1) j = 0;
        if(this.l <= 1) k = 0;
        if(this.subtrees[i][j][k] == null) {
            ForgeFrontier.getInstance().getLogger().severe("Unable to find Generator Instance to remove.");
            return;
        }
        this.subtrees[i][j][k].remove(item);
        this.totalAmt -= 1;
    }

    @Override
    public String toString() {
        if(this.subtrees == null)
            return Arrays.asList(this.items).toString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Arrays.asList(this.items));
        stringBuilder.append(" + ");
        for(int i = 0; i < (this.w > 1 ? 2 : 1); i++) {
            for(int j = 0; j < (this.h > 1 ? 2 : 1); j++) {
                for(int k = 0; k < (this.l > 1 ? 2 : 1); k++) {
                    stringBuilder.append("(" + this.subtrees[i][j][k] + "), ");
                }
            }
        }
        return stringBuilder.toString();
    }

}
