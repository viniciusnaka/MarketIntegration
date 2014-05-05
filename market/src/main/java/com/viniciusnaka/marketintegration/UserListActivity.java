package com.viniciusnaka.marketintegration;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.br.adapter.UserAdapter;
import com.br.bean.CartBean;
import com.br.bean.UserBean;
import com.br.dataBase.CartDB;
import com.br.dataBase.UserDB;

import java.util.List;


public class UserListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {

        private List<UserBean> userBeanList;
        private ListView listViewCarts;
        private UserDB userDB;
        private Button btnAddUser;
        private static final int PAGE_ADD = 1;


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_user_list, container, false);

            userDB = new UserDB(rootView.getContext());
            userBeanList = userDB.getUsers();

            listViewCarts = (ListView) rootView.findViewById(R.id.listViewUsers);
            btnAddUser = (Button) rootView.findViewById(R.id.btnAddUser);

            UserAdapter adapter = new UserAdapter(getActivity(), userBeanList);
            listViewCarts.setAdapter(adapter);

            btnAddUser.setOnClickListener(this);

            return rootView;
        }


        @Override
        public void onClick(View v) {
            Intent it = new Intent(getActivity(), UserCrudActivity.class);
            startActivityForResult(it, PAGE_ADD);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {

        }
    }
}
