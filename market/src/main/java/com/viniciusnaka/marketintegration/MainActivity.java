package com.viniciusnaka.marketintegration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
		
		private Button btnCartList, btnProductList, btnShopping, btnUserList;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);

            btnCartList = (Button) rootView.findViewById(R.id.btnCartList);
            btnProductList = (Button) rootView.findViewById(R.id.btnProductList);
            btnShopping = (Button) rootView.findViewById(R.id.btnShopping);
            btnUserList = (Button) rootView.findViewById(R.id.btnUserList);

            btnCartList.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent it = new Intent(getActivity(), CartListActivity.class);
					startActivity(it);					
				}
			});
			
			btnProductList.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent it = new Intent(getActivity(), ProductActivity.class);
					startActivity(it);										
				}
			});

            btnShopping.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(getActivity(), ProductsCartActivity.class);
                    startActivity(it);
                }
            });

            btnUserList.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                   Intent it = new Intent(getActivity(), UserActivity.class);
                    startActivity(it);
                }
            });

            return rootView;
		}
	}

}
