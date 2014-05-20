package com.viniciusnaka.marketintegration;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.bean.UserBean;
import com.dataBase.UserDB;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class UserMapsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_maps);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_maps, menu);
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
    public static class PlaceholderFragment extends Fragment implements OnMapLongClickListener {

        public PlaceholderFragment() {
        }

        private GoogleMap map;
        private LocationManager locManager;
        private List<UserBean> userBeanList;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_user_maps, container, false);

            //puxo o Fragment do mapa da tela
            MapFragment frag =
                    (MapFragment)getActivity().getFragmentManager().findFragmentById(R.id.fragmentUserMaps);
            map = frag.getMap();

            //pego a informa‹o de onde o usuario esteve
            locManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
            Location localNet = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location localGPS = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(localNet != null){
                Log.e("LOC", "NETWORK " + localNet.getLatitude() + " - " + localNet.getLongitude());
            }
            if(localGPS != null){
                Log.e("LOC","GPS "+localGPS.getLatitude()+ " - "+localGPS.getLongitude());
            }
            Location local = localGPS;
            if(localNet !=null){
                local = localNet;
            }
            //levo o usuario para o local encontrado
            if(local != null){

                //levo o mapa para esse local(com zoom mais proximo)
                //map.animateCamera(CameraUpdateFactory.newLatLngZoom(ponto, 15));

                UserDB userDB = new UserDB(getActivity());
                userBeanList = userDB.getUsers();

                for(UserBean userBeanLoad : userBeanList){
                    LatLng userLocation = new LatLng(Double.parseDouble(userBeanLoad.getLatitude()),
                            Double.parseDouble(userBeanLoad.getLongitude()));
                    MarkerOptions pin = new MarkerOptions();
                    StringBuilder strAddress = new StringBuilder();
                    strAddress.append(userBeanLoad.getAddress()).append(", " + userBeanLoad.getNumberAddress() + "\n");
                    strAddress.append(userBeanLoad.getCity()).append(" - " + userBeanLoad.getState());
                    strAddress.append(", " + userBeanLoad.getZipCode());
                    pin.title(userBeanLoad.getUserName()).
                            snippet(strAddress.toString()).
                            position(userLocation);
                    if(userBeanLoad.getGender().equals(UserBean.Gender.MALE.getSex())){
                        pin.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_male));
                    } else {
                        pin.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_female));
                    }
                    //adiciono um alfinete no mapa
                    map.addMarker(pin);
                }

            }
            //trato o clique longo sobre o mapa
            map.setOnMapLongClickListener(this);

            return rootView;
        }

        @Override
        public void onMapLongClick(final LatLng point) {
            AlertDialog.Builder message =  new AlertDialog.Builder(getActivity());
            message.setTitle("Dados para o ponto");
            //crio caixas de texto para o usuario digitar os dados
            final EditText txtTitle = new EditText(getActivity());
            final EditText txtSubtitle = new EditText(getActivity());
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(txtTitle);
            layout.addView(txtSubtitle);
            message.setView(layout);
            //trato o botao de adicionar o item no mapa
            message.setPositiveButton("Add", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //pego os texto digitados no alerta
                    String title = txtTitle.getText().toString();
                    String subtitle = txtSubtitle.getText().toString();
                    //crio um alfinete com os dados digitados
                    MarkerOptions alfinete = new MarkerOptions();
                    alfinete.title(title).snippet(subtitle).position(point);
                    alfinete.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
                    //adiciono um alfinete no mapa
                    map.addMarker(alfinete);

                }
            });
            message.show();
        }
    }
}
