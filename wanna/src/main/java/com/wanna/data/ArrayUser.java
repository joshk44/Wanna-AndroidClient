package com.wanna.data;

import com.wanna.models.User;

import java.util.ArrayList;

/**
 * Created by fede on 25/03/15.
 */
public class ArrayUser {


    // Encapsulamiento de Posts
    private ArrayList<User> itemsU;

    public ArrayUser(ArrayList<User> dataUsers) {
        this.itemsU = itemsU;
    }

    public ArrayList<User> getItemsU() {
        return itemsU;
    }

    public void setItemsU(ArrayList<User> itemsU) {
        this.itemsU = itemsU;
    }
}