package com.ntou.locker;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class CategoryFrag  extends Fragment {
	
	private List<String> names;
    private List<String> paths;
    private ListView listView;
    private List<Map<String, Object>> filesList;
    private int[] fileImg = {
        R.drawable.directory, R.drawable.file
    };
    private final static String[] editor ={
        "Rename","Delete","Lock it!"
    };
    private String nowPath;
    private SimpleAdapter simpleAdapter;
	private View v;
	    
	@Override
	  public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 v =  inflater.inflate(R.layout.categorylayout, container, false);
		 filesList = new ArrayList<Map<String,Object>>();
         listView = (ListView)v.findViewById(R.id.listView);
	        setList("/");
	        nowPath = "/";
	        
	        listView.setOnItemClickListener(new OnItemClickListener(){
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                File file = new File(paths.get(position));
	                nowPath = new File(paths.get(position)).getParent();
	                if(file.canRead()){
	                    if(file.isDirectory()){
	                        setList(paths.get(position));
	                    }
	                }
	                
	            }            
	        });
	        
	        listView.setOnItemLongClickListener(new OnItemLongClickListener(){

	            @Override
	            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	                // TODO Auto-generated method stub
	                final int position = arg2;
	                new AlertDialog.Builder(getActivity())
	                    .setTitle(names.get(arg2))
	                    .setItems(editor, new DialogInterface.OnClickListener() {
	                        
	                        @Override
	                        public void onClick(DialogInterface arg0, int which) {
	                            // TODO Auto-generated method stub
	                            nowPath = new File(paths.get(position)).getParent();
	                            switch(which){
	                                case 0:
	                                    rename(position);
	                                    break;
	                                case 1:
	                                    delete(position);
	                                    break;
	                                case 2:
	                                	lock(position);
	                                	break;
	                            }
	                            
	                        }
	                    })
	                    .show();
	                return true;
	            }
	            
	        });
	        
	        return v;
	}
	
private void lock(final int which){
        
        new AlertDialog.Builder(getActivity())
        .setTitle("Lock option")
        .setMessage("Setting your password!")
        .setView(v)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                File f = new File(paths.get(which));
                if(f.canWrite()){
                    
                    setList(f.getParent());
                }
                else{
                    new AlertDialog.Builder(getActivity())
                        .setMessage("Can not lock it...")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub                             
                            }               
                        })
                        .show();
                }
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                
            }
        })
        .show();
    }	
	
private void delete(final int which){
        
        new AlertDialog.Builder(getActivity())
        .setTitle("Delete")
        .setMessage("Are you sure to Delete?")
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                File f = new File(paths.get(which));
                if(f.canWrite()){
                	f.delete();													//file delete
                    setList(f.getParent());
                }
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                
            }
        })
        .show();
    }
    private void rename(final int which){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View v = inflater.inflate(R.layout.editlist, null);
        
        new AlertDialog.Builder(getActivity())
            .setTitle("Rename")
            .setView(v)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    File f = new File(paths.get(which));
                    if(f.canWrite()){
                        EditText editText = (EditText)v.findViewById(R.id.edittext);
                        String newPath = f.getParent()+"/"+editText.getText().toString();
                        f.renameTo(new File(newPath));
                        setList(f.getParent());
                    }
                    else{
                        new AlertDialog.Builder(getActivity())
                            .setMessage("Can not Rename.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    
                                }
                                
                            })
                            .show();
                    }
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    
                }
            })
            .show();
    }
    private void setList(String filePath){
        filesList.clear();
        names = new ArrayList<String>();
        paths = new ArrayList<String>();
        
        File[] files = new File(filePath).listFiles();
        Map<String, Object> filesMap;
        
        if(!filePath.equals("/")){
            filesMap = new HashMap<String, Object>();
            names.add(".");
            paths.add("/");
            filesMap.put("image", fileImg[0]);
            filesMap.put("text", ".");
            filesList.add(filesMap);
            
            filesMap = new HashMap<String, Object>();
            names.add("..");
            paths.add(new File(filePath).getParent());
            filesMap.put("image", fileImg[0]);
            filesMap.put("text", "..");
            filesList.add(filesMap);
        }
        
        for(int i=0; i<files.length; i++){
            filesMap = new HashMap<String, Object>();
            names.add(files[i].getName());
            paths.add(files[i].getPath());
            if(files[i].isDirectory()){
                filesMap.put("image", fileImg[0]);
            }
            else if(files[i].isFile()){
                filesMap.put("image", fileImg[1]);
            }
            filesMap.put("text", files[i].getName());
            filesList.add(filesMap);
        }
        simpleAdapter = new SimpleAdapter(getActivity(), 
                filesList, R.layout.simple_adapter, new String[]{"image", "text"},
                new int[]{R.id.image, R.id.text});
        listView.setAdapter(simpleAdapter);
        
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "Create Folder");
        menu.add(Menu.NONE, Menu.FIRST+1, Menu.NONE, "Exit");
        return super.getActivity().onCreateOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View v = inflater.inflate(R.layout.editlist, null);
        switch(item.getItemId()){
            case 1:
                new AlertDialog.Builder(getActivity())
                    .setTitle("Input name.")
                    .setView(v)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            EditText editText = (EditText)v.findViewById(R.id.edittext);
                            String dirName = editText.getText().toString();
                            new File(nowPath+"/"+dirName).mkdir();
                            setList(nowPath);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            
                        }
                    })
                    .show();
                break;
            case 2:
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
	
	
}

