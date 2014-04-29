package com.br.bean;

import java.io.Serializable;

/**
 * Created by vinicius on 4/12/14.
 */
public class UserBean implements Serializable {

	private static final long serialVersionUID = -5781253066480278231L;
	private Integer id;
    private String userName;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
