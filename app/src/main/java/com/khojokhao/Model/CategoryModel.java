package com.khojokhao.Model;

public class CategoryModel {

    String category_id;
    String category_name;
    String image;
    String subcategorey;

    public String getSubcategorey() {
        return subcategorey;
    }

    public void setSubcategorey(String subcategorey) {
        this.subcategorey = subcategorey;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
