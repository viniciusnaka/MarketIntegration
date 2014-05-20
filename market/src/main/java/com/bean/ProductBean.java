package com.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by vinicius on 4/12/14.
 */
public class ProductBean implements Serializable {

    private static final long serialVersionUID = -8536558759259811285L;
	private Integer id;
    private String name;
    private CategoryBean categoryBean;
    private BigDecimal price;
    private String img;
    private Integer quantity = 1;
    private Integer stock;
    
    public ProductBean(){}

    public ProductBean(Integer id, String name, CategoryBean categoryBean, String img, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.categoryBean = categoryBean;
        this.img = img;
        this.price = price;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductBean product = (ProductBean) o;

        if (id != null ? !id.equals(product.id) : product.id != null) return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        if (price != null ? !price.equals(product.price) : product.price != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryBean getCategoryBean() {
        return categoryBean;
    }

    public void setCategoryBean(CategoryBean categoryBean) {
        this.categoryBean = categoryBean;
    }

    public BigDecimal getPrice() {
        return price.setScale(2, RoundingMode.HALF_EVEN);
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public void setId(Integer id) {
		this.id = id;
	}
    
}
