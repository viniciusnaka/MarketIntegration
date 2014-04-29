package com.br.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by vinicius on 4/12/14.
 */
public class CartBean implements Serializable {

	private static final long serialVersionUID = -6091744099802654668L;
	private Integer id;
    private List<ProductBean> productList;
    private UserBean user;
    private BigDecimal price;
    private Date dateCreate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ProductBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductBean> productList) {
        this.productList = productList;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }
}
