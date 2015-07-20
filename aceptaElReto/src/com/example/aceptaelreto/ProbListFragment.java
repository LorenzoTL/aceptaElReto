package com.example.aceptaelreto;

import java.util.ArrayList;
import java.util.List;

import ws.CallerWS;
import ws.Traductor;
import ws.WSquery;
import ws.WSquery.type;
import acr.estructuras.CategoryWSType;
import acr.estructuras.ListWSType;
import acr.estructuras.ProblemDetailsList;
import acr.estructuras.ProblemWSType;
import acr.estructuras.UserWSType;
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
    int[] ids = new int[10];
    TextView pb;
    private CallerWS ws;
    private WSquery path;
    Bundle token;
    
    public static ProbListFragment newInstance(int sectionNumber, String tk) {
    	ProbListFragment fragment = new ProbListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString("TOKEN", tk);
        fragment.setArguments(args);
        return fragment;
    }
    
    public ProbListFragment() {
 
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        View rootView = inflater.inflate(R.layout.lista_prob, container, false);
        token = this.getArguments();
        list = (ListView)rootView.findViewById(R.id.listap);
        pb = (TextView)rootView.findViewById(R.id.pb);
        this.ws= new CallerWS();
        path = this.ws.getPath();
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
    	
    	this.path.addType(type.cat);
    	
    	if (click != 0){
    		path.addID(click);
        	path.addNumSubCat(1);
    	}

    	this.ws.setPath(path);
    	String respuesta = ws.getCall(getActivity(),token.getString("TOKEN"));
    	Traductor tradu = new Traductor(respuesta);
    	ArrayList<CategoryWSType> arrayCat = null;
    	CategoryWSType cat = null;
    	
    	try{
    		if (tradu.getJSON().startsWith("{"+"subcats")) arrayCat = tradu.getCategorias();
    		else cat = tradu.getCategoria();
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    	if (cat != null){
    		arrayCat = (ArrayList<CategoryWSType>) cat.subcats;
    	}
    	//if (arrayCat == null) getListaProblemas();
    	
    		
    	CategoryWSType aux = null;
    	
        for(int i=0;i<arrayCat.size();i++){
        	aux = arrayCat.get(i);
        	this.etiq.add(aux.name);
        	ids[i]=aux.id;
        }	
        	
    	    	
		
        if (click == 0){
        	adapter = new MyArrayAdapter<String>(getActivity(),etiq);
        	list.setAdapter(adapter);
            list.setOnItemClickListener(this);
        }else adapter.notifyDataSetChanged();
     
        pb.setText("Buscar por: ");
    }
    
    public void getListaProblemas(){
		
    	path.cleanQuery();
		path.addType(type.cat);
		path.addType(type.allproblems);
		this.ws.setPath(path);
    	String respuesta = ws.getCall(getActivity(),token.getString("TOKEN"));
		Traductor tradu = new Traductor(respuesta);
    	ArrayList<ProblemWSType> arrayCat = null;
    	try{
    		arrayCat = tradu.getProblemas();		
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    	for(int i=0;i<arrayCat.size();i++){
    		this.etiq.add(arrayCat.get(i).title);
    	}	
    	adapter.notifyDataSetChanged();
		
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    	String aux = (String) adapter.getItem(position);
    	adapter.clear();
    	path.cleanQuery();
    	setLista(ids[position]);
    	
    	// Falta terminar el resto
    	
        /*String msg = "Elegiste la tarea:\n"+aux;
        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();*/

    }

}
