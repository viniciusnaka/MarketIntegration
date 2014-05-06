package com.viniciusnaka.marketintegration;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.br.bean.UserBean;
import com.br.dataBase.UserDB;
import com.br.rest.Http;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class UserCrudActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_crud);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_crud, menu);
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
    public static class PlaceholderFragment extends Fragment {

        private static final String PAGE_CORREIOS = "http://correiosapi.apphb.com/cep/";
        private EditText editTextUserName, editTextEmail, editTextLogin, editTextPassword, editTextConfirmPassword,
            editTextAddress, editTextNumberAddress, editTextComplement, editTextZipCode, editTextNeighborhood,
            editTextCity;
        private Spinner spinnerStates;
        private Button btnSearchZipCode, btnSaveUser;
        private ProgressDialog progressDialog;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_user_crud, container, false);

            editTextUserName = (EditText) rootView.findViewById(R.id.editTextUserName);
            editTextEmail = (EditText) rootView.findViewById(R.id.editTextEmail);
            editTextLogin = (EditText) rootView.findViewById(R.id.editTextLogin);
            editTextPassword = (EditText) rootView.findViewById(R.id.editTextPassword);
            editTextConfirmPassword = (EditText) rootView.findViewById(R.id.editTextConfirmPassword);
            editTextZipCode = (EditText) rootView.findViewById(R.id.editTextZipCode);
            editTextAddress = (EditText) rootView.findViewById(R.id.editTextAddress);
            editTextNumberAddress = (EditText) rootView.findViewById(R.id.editTextNumberAddress);
            editTextComplement = (EditText) rootView.findViewById(R.id.editTextComplement);
            editTextNeighborhood = (EditText) rootView.findViewById(R.id.editTextNeighborhood);
            editTextCity = (EditText) rootView.findViewById(R.id.editTextCity);
            spinnerStates = (Spinner) rootView.findViewById(R.id.spinnerStates);
            btnSearchZipCode = (Button) rootView.findViewById(R.id.btnSearchZipCode);
            btnSaveUser = (Button) rootView.findViewById(R.id.btnSaveUser);

            if(getActivity().getIntent().getSerializableExtra("user") != null){
                UserBean userBeanLoad = (UserBean) getActivity().getIntent().getSerializableExtra("user");
                editTextUserName.setText(userBeanLoad.getUserName());
                editTextEmail.setText(userBeanLoad.getEmail());
                editTextLogin.setText(userBeanLoad.getLogin());
                editTextPassword.setVisibility(View.INVISIBLE);
                editTextConfirmPassword.setVisibility(View.INVISIBLE);
                editTextZipCode.setText(userBeanLoad.getZipCode());
                editTextAddress.setText(userBeanLoad.getAddress());
                editTextNumberAddress.setText(userBeanLoad.getNumberAddress());
                editTextComplement.setText(userBeanLoad.getComplement());
                editTextNeighborhood.setText(userBeanLoad.getNeighborhood());
                editTextCity.setText(userBeanLoad.getCity());
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                btnSaveUser.setTag(userBeanLoad);
            }

            btnSaveUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean create = true;
                    UserBean userBean;

                    if(view.getTag() != null){
                        create = false;
                        userBean = (UserBean) view.getTag();
                    } else {
                        userBean = new UserBean();
                    }

                    userBean.setUserName(editTextUserName.getText().toString());
                    userBean.setEmail(editTextEmail.getText().toString());
                    userBean.setLogin(editTextLogin.getText().toString());
                    userBean.setPassword(editTextPassword.getText().toString());
                    userBean.setZipCode(editTextZipCode.getText().toString());
                    userBean.setAddress(editTextAddress.getText().toString());
                    userBean.setNumberAddress(editTextNumberAddress.getText().toString());
                    userBean.setComplement(editTextComplement.getText().toString());
                    userBean.setNeighborhood(editTextNeighborhood.getText().toString());
                    userBean.setCity(editTextCity.getText().toString());

                    userBean.setState("SP"); // teste

                    UserDB userDB = new UserDB(getActivity());

                    if(validateFields(userBean)){
                        userBean = userDB.salvar(userBean);
                        if(userBean != null){
                            Intent it = new Intent();
                            it.putExtra("msg", create ? "Usuário salvo com sucesso!" : "Usuário atualizado com sucesso!");
                            it.putExtra("user", userBean);
                            getActivity().setResult(RESULT_OK, it);
                            getActivity().finish();
                        } else {
                            Intent it = new Intent();
                            it.putExtra("msg", create ? "Falha ao cadastrar o Usuário!" : "Falha ao atualizar Usuário!");
                            getActivity().setResult(RESULT_CANCELED, it);
                            getActivity().finish();
                        }
                    }

                }
            });

            btnSearchZipCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog = ProgressDialog.show(getActivity(), "Pesquisando", "Carregando...");
                    AsyncTask<Void, Void, JSONObject> task = new AsyncTask<Void, Void, JSONObject>() {
                        @Override
                        protected JSONObject doInBackground(Void... params) {

                            Http http = new Http();
                            try {
                                String result = http.doGet(PAGE_CORREIOS + editTextZipCode.getText().toString());
                                JSONObject jsonObject = new JSONObject(result);
                                if(jsonObject.isNull("message")){
                                    return jsonObject;
                                }

                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(JSONObject jsonObject) {
                            if(jsonObject == null){
                                Toast.makeText(getActivity(), "CEP inexistente.", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    editTextAddress.setText(jsonObject.get("tipoDeLogradouro") + " " + jsonObject.get("logradouro"));
                                    editTextNeighborhood.setText(jsonObject.get("bairro").toString());
                                    editTextCity.setText(jsonObject.get("cidade").toString());
                                } catch (JSONException e3) {
                                    e3.printStackTrace();
                                }

                            }
                            progressDialog.dismiss();
                        }

                    };
                    task.execute();
                }
            });

            return rootView;
        }

        private boolean validateFields(UserBean userBean){
            boolean validate = true;
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

            // DADOS PESSOAIS
            /*if(userBean == null){
                validate = false;
                alert.setTitle("Erro - Cadastro de Usuário").setMessage("Favor preencher os campos obrigatórios");
                alert.setNegativeButton("OK", null);
                alert.show();
            }*/

            if(validate && (userBean.getUserName().equals("")) || (userBean.getEmail().equals("")) || (userBean.getLogin().equals(""))){
                validate = false;
                alert.setTitle("Erro - Cadastro de Usuário").setMessage("Favor preencher todos os dados Pessoais");
                alert.setNegativeButton("OK", null);
                alert.show();
            }

            // só valido a senha se for cadastro novo
            if(userBean.getId() != null && validate && (userBean.getPassword().equals("")) || (editTextConfirmPassword.getText().toString().equals(""))){
                validate = false;
                alert.setTitle("Erro - Cadastro de Usuário").setMessage("Favor preencher os campos da Senha");
                alert.setNegativeButton("OK", null);
                alert.show();
            } else if(userBean.getId() != null && validate && !userBean.getPassword().equals(editTextConfirmPassword.getText().toString())){
                validate = false;
                alert.setTitle("Erro - Cadastro de Usuário").setMessage("As Senhas não coincidem");
                alert.setNegativeButton("OK", null);
                alert.show();
            }

            // ENDERECO
            if(validate && (userBean.getZipCode().equals("") || userBean.getAddress().equals("") || userBean.getNumberAddress().equals("")
                    || userBean.getNeighborhood().equals("") || userBean.getCity().equals(""))){
                validate = false;
                alert.setTitle("Erro - Cadastro de Usuário").setMessage("Favor preencher todos os dados de Endereço");
                alert.setNegativeButton("OK", null);
                alert.show();
            }

            return validate;
        }
    }
}
