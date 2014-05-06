package com.br.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.br.bean.ProductBean;
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
    private TextView txtEmail, txtLogin;

    public UserAdapter(Context context, List<UserBean> userBeanList){
        this.context = context;
        this.userBeanList = userBeanList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private ChangeUser changeUser;
    
    public interface ChangeUser{
        public void onDeleteUser(UserBean userBean);
        public void onEditUser(UserBean userBean);
    }

    public void setChangeUser(ChangeUser changeUser) {
        this.changeUser = changeUser;
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

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_user, null);
        }

        txtEmail = (TextView) convertView.findViewById(R.id.txtEmail);
        txtLogin = (TextView) convertView.findViewById(R.id.txtLogin);

        imgEditUser = (ImageButton) convertView.findViewById(R.id.imgBtnEditUser);
        imgDeleteButton = (ImageButton) convertView.findViewById(R.id.imgBtnDeleteUser);

        UserBean userBean = userBeanList.get(position);

        txtEmail.setText(userBean.getEmail());
        txtLogin.setText(userBean.getLogin());

        imgEditUser.setTag(userBean);
        imgEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(changeUser != null){
                    UserBean userBeanLoad = (UserBean) view.getTag();
                    changeUser.onEditUser(userBeanLoad);
                }
            }
        });

        imgDeleteButton.setTag(userBean);
        imgDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(changeUser != null) {
                    UserBean userBeanLoad = (UserBean) view.getTag();
                    changeUser.onDeleteUser(userBeanLoad);
                }
            }
        });

        return convertView;
    }
}
