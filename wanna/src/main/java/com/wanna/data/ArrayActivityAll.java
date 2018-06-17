package com.wanna.data;

/**
 * Created by fede on 1/04/15.
 */


import com.wanna.models.ActivityAll;

import java.util.ArrayList;


public class ArrayActivityAll {
    // Encapsulamiento de Activities
    private ArrayList<ActivityAll> itemsE;


    public ArrayActivityAll(ArrayList<ActivityAll> itemsE) {
        this.itemsE = itemsE;


    }

    public ArrayList<ActivityAll> getItemsE() {
        return itemsE;
    }

    public void setItemsE(ArrayList<ActivityAll> itemsE) {
        this.itemsE = itemsE;
    }
}
