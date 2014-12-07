package com.example.mitchtz.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mitchtz.webview.Assignment;
import com.example.mitchtz.webview.EventDBHlpr;

import java.util.ArrayList;
import java.util.List;

public class Assignment_List extends Activity {

    //EventDBHlpr db=new EventDBHlpr(this);
   // List<Assignment> AssignList=db.getAssignments();  //List of ID's to assignments ID's will act as a handle to the data
    List<Assignment> AssignList=new ArrayList<Assignment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Assignment test_assignment_1= new Assignment("Test 1","CSCI3155","testing","12-5-14");
        AssignList.add(test_assignment_1);
        AssignList.add(test_assignment_1);
        AssignList.add(test_assignment_1);
        AssignList.add(test_assignment_1);
        //setContentView(R.layout.activity_assignment__list);
        setContentView(R.layout.assignment_list);
        final Context context = this;
        ListView listview = (ListView) findViewById(R.id.assignments);
        EventAdapter adapter= new EventAdapter(this,R.layout.event_item,AssignList.toArray());
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long ID){
                AlertDialog.Builder DialogBldr = new AlertDialog.Builder(context);

               /* onItemClick will use position value to pull the assignment info by way of the
               the value stored in the array passed to the list view by way of an adapter
               (AdapterView) from there the assignment info can be used to set the alertDialog info
                */

                //Dialog info to be displayed currently holder information
                DialogBldr.setTitle(AssignList.get(position).Name+" Due: "+AssignList.get(position).Date);
                DialogBldr.setMessage(AssignList.get(position).Description);


                DialogBldr.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked,  close the dialog box and return to listview
                        dialog.cancel();
                    }
                });
                AlertDialog Dialog = DialogBldr.create();
                Dialog.show();
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.assignment__list, menu);
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

}
