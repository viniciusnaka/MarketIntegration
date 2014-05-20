package com.viniciusnaka.marketintegration;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.bean.UserBean;
import com.dataBase.UserDB;
import com.helper.AppHelper;
import com.rest.Http;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;


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
        private static final String PAGE_MAPS = "http://maps.googleapis.com/maps/api/geocode/json?address=";
        private TextView txtLatitude, txtLongitude;
        private EditText editTextUserName, editTextEmail, editTextLogin, editTextPassword, editTextConfirmPassword,
            editTextAddress, editTextNumberAddress, editTextComplement, editTextZipCode, editTextNeighborhood,
            editTextCity;
        private RadioGroup radioGroupGender;
        private RadioButton radioButtonGender;
        private Spinner spinnerStates;
        private Button btnSearchZipCode, btnSaveUser;
        private ProgressDialog progressDialog;
        private static List<String> states = AppHelper.getStates();
        private UserBean userBean = new UserBean();

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
            txtLatitude = (TextView) rootView.findViewById(R.id.txtLatitude);
            txtLongitude = (TextView) rootView.findViewById(R.id.txtLongitude);
            radioGroupGender = (RadioGroup) rootView.findViewById(R.id.radioGender);
            spinnerStates = (Spinner) rootView.findViewById(R.id.spinnerStates);
            btnSearchZipCode = (Button) rootView.findViewById(R.id.btnSearchZipCode);
            btnSaveUser = (Button) rootView.findViewById(R.id.btnSaveUser);

            ArrayAdapter arrayAdapterStates = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, states);
            spinnerStates.setPrompt("Estados");
            spinnerStates.setAdapter(arrayAdapterStates);
            spinnerStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        String state = (String) spinnerStates.getItemAtPosition(position);
                        spinnerStates.setSelection(states.indexOf(state));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(getActivity(), "É necessário selecionar uma Categoria", Toast.LENGTH_SHORT).show();
                }

            });

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
                if(userBeanLoad.getGender().equals(UserBean.Gender.MALE.getSex())){
                    radioButtonGender = (RadioButton) rootView.findViewById(R.id.radioMale);
                    radioButtonGender.setChecked(true);
                } else {
                    radioButtonGender = (RadioButton) rootView.findViewById(R.id.radioFemale);
                    radioButtonGender.setChecked(true);
                }

                spinnerStates.setSelection(states.indexOf(userBeanLoad.getState()));
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                btnSaveUser.setTag(userBeanLoad);
            }

            btnSaveUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog = ProgressDialog.show(getActivity(), "Processando", "Salvando usuário...");

                    if(view.getTag() != null){
                        userBean = (UserBean) view.getTag();
                    } else {
                        userBean = new UserBean();
                    }

                    int selectedId = radioGroupGender.getCheckedRadioButtonId();
                    switch (selectedId){
                        case R.id.radioFemale:
                            userBean.setGender(UserBean.Gender.FEMALE.getSex());
                            break;
                        case R.id.radioMale:
                            userBean.setGender(UserBean.Gender.MALE.getSex());
                            break;
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

                    userBean.setState(spinnerStates.getSelectedItem().toString());

                    AsyncTask<Void, Void, JSONObject> task = new AsyncTask<Void, Void, JSONObject>() {

                        @Override
                        protected JSONObject doInBackground(Void... voids) {
                            Http http = new Http();
                            try {
                                StringBuilder strAddress = new StringBuilder();
                                strAddress.append(editTextAddress.getText().toString()).append(" ");
                                strAddress.append(editTextNumberAddress.getText().toString()).append(" ");
                                strAddress.append(editTextCity.getText().toString()).append(" ");
                                strAddress.append(spinnerStates.getSelectedItem().toString());

                                String result = http.doGet(PAGE_MAPS + URLEncoder.encode(strAddress.toString(), "UTF-8") + "&sensor=true");

                                JSONObject jsonObject = new JSONObject(result);

                                if(jsonObject.getString("status").equals("OK")){
                                    JSONArray jsonArrayResults = jsonObject.getJSONArray("results");
                                    for(int i=0; i< jsonArrayResults.length(); i++){
                                        JSONObject jsonObjectResults = jsonArrayResults.getJSONObject(i);
                                        if(jsonObjectResults.get("geometry") != null){
                                            JSONObject jsonObjectGeometry = jsonObjectResults.getJSONObject("geometry");
                                            return (JSONObject) jsonObjectGeometry.get("location");
                                        }
                                    }
                                }

                            } catch (IOException e1) {
                                Log.e(AppHelper.getClassError(UserCrudActivity.class), e1.getMessage());
                            } catch (JSONException e2) {
                                Log.e(AppHelper.getClassError(UserCrudActivity.class), e2.getMessage());
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(JSONObject jsonObject) {
                            super.onPostExecute(jsonObject);

                            boolean cepOK = true;
                            if(jsonObject == null || jsonObject.isNull("lng")){
                                cepOK = false;
                                Toast.makeText(getActivity(), "CEP inexistente.", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    userBean.setLatitude(jsonObject.get("lat").toString());
                                    userBean.setLongitude(jsonObject.get("lng").toString());

                                    boolean create = userBean.getId() == null ? true : false;

                                    UserDB userDB = new UserDB(getActivity());
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

                                } catch (JSONException e3) {
                                    Log.e(AppHelper.getClassError(UserCrudActivity.class), e3.getMessage());
                                }
                            }

                            if(cepOK){
                                UserDB userDB = new UserDB(getActivity());
                                userBean = userDB.salvar(userBean);
                                if(userBean != null){
                                    Intent it = new Intent();
                                    it.putExtra("msg", userBean.getId() == null ? "Usuário salvo com sucesso!" : "Usuário atualizado com sucesso!");
                                    it.putExtra("user", userBean);
                                    getActivity().setResult(RESULT_OK, it);
                                    getActivity().finish();
                                } else {
                                    Intent it = new Intent();
                                    it.putExtra("msg", userBean.getId() == null ? "Falha ao cadastrar o Usuário!" : "Falha ao atualizar Usuário!");
                                    getActivity().setResult(RESULT_CANCELED, it);
                                    getActivity().finish();
                                }
                            }

                        }
                    };

                    if(validateFields(userBean)){
                        task.execute();
                    }

                    progressDialog.dismiss();
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
                                if(!jsonObject.has("message")){
                                    return jsonObject;
                                }

                            } catch (IOException e1) {
                                Log.e(AppHelper.getClassError(UserCrudActivity.class), e1.getMessage());
                            } catch (JSONException e2) {
                                Log.e(AppHelper.getClassError(UserCrudActivity.class), e2.getMessage());
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
                                    spinnerStates.setSelection(states.indexOf(jsonObject.get("estado").toString()));
                                } catch (JSONException e3) {
                                    Log.e(AppHelper.getClassError(UserCrudActivity.class), e3.getMessage());
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
            alert.setNegativeButton("OK", null);
            // DADOS PESSOAIS
            
            if(validate && (userBean.getUserName().equals("")) || (userBean.getEmail().equals("")) || (userBean.getLogin().equals(""))){
                validate = false;
                Log.e(AppHelper.getClassError(UserCrudActivity.class), "Favor preencher todos os dados Pessoais");
                alert.setTitle("Erro - Cadastro de Usuário").setMessage("Favor preencher todos os dados Pessoais");
                alert.show();
            }

            if(validate && (userBean.getGender() == null || userBean.getGender().equals(""))){
                validate = false;
                Log.e(AppHelper.getClassError(UserCrudActivity.class), "Favor selecionar o Gênero");
                alert.setTitle("Erro - Cadastro de Usuário").setMessage("Favor preencher todos os dados Pessoais");
                alert.show();
            }

            // só valido a senha se for cadastro novo
            if(userBean.getId() == null && validate && (userBean.getPassword().equals("") || editTextConfirmPassword.getText().toString().equals(""))){
                validate = false;
                Log.e(AppHelper.getClassError(UserCrudActivity.class), "Favor preencher os campos da Senha");
                alert.setTitle("Erro - Cadastro de Usuário").setMessage("Favor preencher os campos da Senha");
                alert.show();
            } else if(userBean.getId() == null && validate && !userBean.getPassword().equals(editTextConfirmPassword.getText().toString())){
                validate = false;
                Log.e(AppHelper.getClassError(UserCrudActivity.class), "As Senhas não coincidem");
                alert.setTitle("Erro - Cadastro de Usuário").setMessage("As Senhas não coincidem");
                alert.show();
            }

            // ENDERECO
            if(validate && (userBean.getZipCode().equals("") || userBean.getAddress().equals("") || userBean.getNumberAddress().equals("")
                    || userBean.getNeighborhood().equals("") || userBean.getCity().equals(""))){
                validate = false;
                Log.e(AppHelper.getClassError(UserCrudActivity.class), "Favor preencher todos os dados de Endereço");
                alert.setTitle("Erro - Cadastro de Usuário").setMessage("Favor preencher todos os dados de Endereço");
                alert.show();
            }

            return validate;
        }
    }
}
