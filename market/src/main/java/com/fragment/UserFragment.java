package com.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.adapter.UserAdapter;
import com.bean.UserBean;
import com.dataBase.UserDB;
import com.viniciusnaka.marketintegration.R;
import com.viniciusnaka.marketintegration.UserCrudActivity;

import java.util.List;


public class UserFragment extends Fragment implements View.OnClickListener {

    private List<UserBean> userBeanList;
    private ListView listViewUsers;
    private UserDB userDB;
    private Button btnAddUser;
    private static final int PAGE_ADD = 1;
    private static final int PAGE_EDIT = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        userDB = new UserDB(rootView.getContext());
        userBeanList = userDB.getUsers();

        listViewUsers = (ListView) rootView.findViewById(R.id.listViewUsers);
        btnAddUser = (Button) rootView.findViewById(R.id.btnAddUser);

        UserAdapter adapter = new UserAdapter(getActivity(), userBeanList);

        adapter.setChangeUser(new UserAdapter.ChangeUser(){

            @Override
            public void onDeleteUser(UserBean userBeanLoad) {
                if(userDB.delete(userBeanLoad)){
                    userBeanList.remove(userBeanLoad);
                    UserAdapter adapter = (UserAdapter) listViewUsers.getAdapter();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Usuário "+userBeanLoad.getUserName() + " deletado com sucesso!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Erro ao deletar o Usuário "+userBeanLoad.getUserName()+ ".",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onEditUser(UserBean userBean) {
                Intent it = new Intent(getActivity(), UserCrudActivity.class);
                it.putExtra("user", userBean);
                startActivityForResult(it, PAGE_EDIT);
            }
        });

        listViewUsers.setAdapter(adapter);

        btnAddUser.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onClick(View v) {
        Intent it = new Intent(getActivity(), UserCrudActivity.class);
        startActivityForResult(it, PAGE_ADD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent it) {
        Bundle param = it != null ? it.getExtras() : null;
        if(param != null) {
            UserBean userBean = (UserBean) param.get("user");
            UserAdapter adapter = (UserAdapter) listViewUsers.getAdapter();
            if (requestCode == PAGE_ADD) {
                userBeanList.add(userBean);
            } else {
                int indexUserBean = userBeanList.indexOf(userBean);
                // removendo o objeto antigo
                userBeanList.remove(indexUserBean);
                // adicionando o novo objeto(antigo alterado)
                userBeanList.add(indexUserBean, userBean);
            }
            adapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), param.get("msg").toString(), Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
