package com.example.aceptaelreto;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	
	//ATRIBUTOS PARA MANEJO DEL MENU.
	private CharSequence mTitle;
	private String[] opcionesMenu;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private GridView tablaPerfil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		tablaPerfil=(GridView) findViewById(R.id.gridView1);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		  // update the main content by replacing fragments
		  FragmentManager fragmentManager = getSupportFragmentManager();
		  switch (position+1) {
		    case 1:
		      fragmentManager.beginTransaction().replace(R.id.container,
		      PlaceholderFragment.newInstance(position + 1)).commit();
		      break;
		    case 2:
		      fragmentManager.beginTransaction().replace(R.id.container,
		      Layout2Fragment.newInstance(position + 1)).commit();
		      break;
		    case 3:
		      fragmentManager.beginTransaction().replace(R.id.container,
		      Layout3Fragment.newInstance(position + 1)).commit();
		      break;
		    case 4:
			  fragmentManager.beginTransaction().replace(R.id.container,
			  Layout4Fragment.newInstance(position + 1)).commit();
			  break;
		    case 5:
			  fragmentManager.beginTransaction().replace(R.id.container,
			  Layout5Fragment.newInstance(position + 1)).commit();
			  break;
		    case 6:
			  fragmentManager.beginTransaction().replace(R.id.container,
			  Layout6Fragment.newInstance(position + 1)).commit();
			  break;
		  }
		}

	public void onSectionAttached(int number) {
		Fragment fragment = null;
		switch (number) {
		//Aqui es donde tenemos que mandar ejecutar los nuevos fragment
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
			//A�ADIDO NUEVOS APARTADOS DE MENU
		case 4:
			mTitle = getString(R.string.title_section4);
			break;
		case 5:
			mTitle = getString(R.string.title_section5);
			break;
		case 6:
			mTitle = getString(R.string.title_section6);
			break;
		}
	/*	 FragmentManager fragmentManager =getSupportFragmentManager();
	 
	     fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
	 
	     drawerList.setItemChecked(position, true);
	 
	     tituloSeccion = opcionesMenu[position];
	     getSupportActionBar().setTitle(tituloSeccion);
	     
	     drawerLayout.closeDrawer(drawerList);*/
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
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
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

}
class VivzAdapter extends BaseAdapter{
	int idImage;
	ArrayList<String> atb;
	ArrayList<String> values;
	
	public VivzAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.atb=new ArrayList<String>();
		this.values=new ArrayList<String>();
		Resources res = context.getResources();
		String[] temp = res.getStringArray(R.array.perfil_atb);	
		for(int i=0;i<this.atb.size();i++){
			this.atb.add(temp[i]);
		}
		this.idImage=-1;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return atb.size()+values.size()+1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(position>=atb.size()){
			return values.get(position-atb.size());
		}else{
		return atb.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
