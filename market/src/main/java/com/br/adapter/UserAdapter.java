package com.br.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.br.bean.UserBean;
import com.viniciusnaka.marketintegration.R;

import java.util.List;

/**
 * Created by vinicius on 03/05/14.
 */
public class UserAdapter extends BaseAdapter {

    private List<UserBean> userBeanList;
    private Context context;
    private LayoutInflater layoutInflater;
    private ImageButton imgEditUser, imgDeleteButton;
    private TextView txtUserName, txtEmail, txtLogin, txtDateCreate;

    public UserAdapter(Context context, List<UserBean> userBeanList){
        this.context = context;
        this.userBeanList = userBeanList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return userBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return userBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        txtUserName = (TextView) convertView.findViewById(R.id.txtUserName);
        txtEmail = (TextView) convertView.findViewById(R.id.txtEmail);
        txtLogin = (TextView) convertView.findViewById(R.id.txtLogin);
        txtDateCreate = (TextView) convertView.findViewById(R.id.txtDateCreate);

        imgEditUser = (ImageButton) convertView.findViewById(R.id.imgBtnEditUser);
        imgDeleteButton = (ImageButton) convertView.findViewById(R.id.imgBtnDeleteUser);

        imgEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imgDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }
}
