package com.viniciusnaka.marketintegration;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends ActionBarActivity {

    private DrawerLayout drawerLayout;
    private ListView listViewMenu;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private String[] menu;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);

        title = drawerTitle = getTitle();
        menu = getResources().getStringArray(R.array.menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listViewMenu = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        listViewMenu.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menu));
        listViewMenu.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(title);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
	}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(listViewMenu);
        menu.findItem(R.id.action_websearch);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
	}

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putInt(MenuFragment.ARG_MENU_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        listViewMenu.setItemChecked(position, true);
        setTitle(menu[position]);
        drawerLayout.closeDrawer(listViewMenu);
    }

    @Override
    public void setTitle(CharSequence title) {
        title = title;
        getActionBar().setTitle(title);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class MenuFragment extends Fragment {

        public static final String ARG_MENU_NUMBER = "menu_number";
		private Button btnCartList, btnProductList, btnShopping, btnUserList, btnUsersMap;

		public MenuFragment() {
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
            btnUsersMap = (Button) rootView.findViewById(R.id.btnUsersMap);

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

            btnUsersMap.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(getActivity(), UserMapsActivity.class);
                    startActivity(it);
                }
            });

            return rootView;
		}
	}

}
