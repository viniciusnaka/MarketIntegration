package com.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vinicius on 4/12/14.
 */
public class CategoryBean implements Serializable {

	private static final long serialVersionUID = -7607092237874162657L;
	private Integer id;
    private String name;
    private List<SubCategory> subCategories;

    public CategoryBean(){}

    public enum SubCategory{

        MEAT_BOVINE(1),
        MEAT_VARIOUS(2), // pork, smoked
        MEAT_CHICKEN(3),
        VEGETABLES(4), // legumes e verduras
        BEER(5),
        FRUIT(6),
        BLEACH(7),
        DISINFECTANT(8),
        SOFTENER(9);

        private int code;

        private SubCategory(int code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
    
    public CategoryBean(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryBean category = (CategoryBean) o;

        if (id != null ? !id.equals(category.id) : category.id != null) return false;
        if (name != null ? !name.equals(category.name) : category.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
