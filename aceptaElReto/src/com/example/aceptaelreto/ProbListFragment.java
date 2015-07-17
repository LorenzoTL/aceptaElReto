package com.example.aceptaelreto;

import java.util.ArrayList;
import java.util.List;

import extras.MyArrayAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;

 /*
  * clase que genera el fragment de 
  */
public class ProbListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    ListView list;
    
    ArrayAdapter adapter;
    ArrayList<String> etiq = new ArrayList<String>();
    TextView pb;
    
    
    public static ProbListFragment newInstance(int sectionNumber) {
    	ProbListFragment fragment = new ProbListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    
    public ProbListFragment() {
 
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        View rootView = inflater.inflate(R.layout.lista_prob, container, false);
        
        list = (ListView)rootView.findViewById(R.id.listap);
        pb = (TextView)rootView.findViewById(R.id.pb);
        setLista(0);
        
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(
                ARG_SECTION_NUMBER));
    }
    
    public void setLista(int click){
    	
    	String[] temp = null;
    	Resources res = getActivity().getApplicationContext().getResources();
    	
    	switch(click){
    	case 0:
    		temp = res.getStringArray(R.array.list_prob);
    		break;
    	case 2:
    		temp = res.getStringArray(R.array.list_prob2);
    		break;
    	}	
    	
		for(int i=0;i<temp.length;i++){
			this.etiq.add(temp[i]);
		}
        if (click == 0){
        	adapter = new MyArrayAdapter<String>(getActivity(),etiq);
        	list.setAdapter(adapter);
            list.setOnItemClickListener(this);
        }else adapter.notifyDataSetChanged();
        
        pb.setText("Buscar por: ");
    }


    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear) {

            adapter.clear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    	String aux = (String) adapter.getItem(position);
    	adapter.clear();
    	//if (aux.equals("Volúmenes")) lista de problemas;
    	if (aux.equals("Categorías")) setLista(2);
    	if (aux.equals("Programación")) setLista(21);
    	if (aux.equals("Exámenes")) setLista(22);
    	if (aux.equals("Concursos")) setLista(23);
    	if (aux.equals("Temática")) setLista(24);
    	if (aux.equals("Recopilaciones")) setLista(25);
    	
    	// Falta terminar el resto
    	
        /*String msg = "Elegiste la tarea:\n"+aux;
        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();*/

    }

}
