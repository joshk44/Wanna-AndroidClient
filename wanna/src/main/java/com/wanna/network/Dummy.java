package com.wanna.network;

/**
 * Created by Roger on 6/12/14.
 */
public class Dummy {
    private String content;
    private String title;

    public Dummy(String content, String title) {
        this.content = content;
        this.title = title;
    }

    public String toString() {
        return content + "\n" + title;
    }

}
