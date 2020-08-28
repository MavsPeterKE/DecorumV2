package com.rapid.furnitureaugmentreal.domain;

import com.google.ar.sceneform.rendering.ViewRenderable;

import java.io.Serializable;

public class Furniture implements Serializable {

    String id;
    String name;
    String description;
    String image;
    String price;
    int selected=0;
    ViewRenderable viewRenderable;


    public Furniture() {
    }


    public ViewRenderable getViewRenderable() {
        return viewRenderable;
    }

    public void setViewRenderable(ViewRenderable viewRenderable) {
        this.viewRenderable = viewRenderable;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
