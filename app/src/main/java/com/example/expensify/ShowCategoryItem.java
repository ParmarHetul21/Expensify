package com.example.expensify;

public class ShowCategoryItem {

    String CategoryName,Categorytype,CatgeoryNote;
    int image;

    public ShowCategoryItem(String categoryName, String categorytype, String catgeoryNote, int image) {
        CategoryName = categoryName;
        Categorytype = categorytype;
        CatgeoryNote = catgeoryNote;
        this.image = image;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategorytype() {
        return Categorytype;
    }

    public void setCategorytype(String categorytype) {
        Categorytype = categorytype;
    }

    public String getCatgeoryNote() {
        return CatgeoryNote;
    }

    public void setCatgeoryNote(String catgeoryNote) {
        CatgeoryNote = catgeoryNote;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
