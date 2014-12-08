package com.example.mitchtz.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mitchtz.webview.Assignment;
import com.example.mitchtz.webview.EventDBHlpr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
//import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
//ical4j

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.PeriodRule;
import net.fortuna.ical4j.filter.Rule;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.component.VEvent;


public class Assignment_List extends Activity {

    //EventDBHlpr db=new EventDBHlpr(this);
   // List<Assignment> AssignList=db.getAssignments();  //List of ID's to assignments ID's will act as a handle to the data
    List<Assignment> AssignList=new ArrayList<Assignment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Change title bar
        getActionBar().setTitle("Moodle Events");
        //Create file path to ical file
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, "/Download/icalexport.ics");
        //File file = new File(Environment.getExternalStorageDirectory(), "/icalexport.ics");
        /*
        if(file.exists()){
            myCalendarString = "EXISTS";
        }
        else {
            myCalendarString = "!EXISTS";
        }
        */
        FileInputStream fin = null;
        Calendar calendar = null;
        try {
            fin = new FileInputStream(file);
            CalendarBuilder builder = new CalendarBuilder();
            //Calendar calendar = null;
            calendar = builder.build(fin);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Iterate through calender object
        String title = null;
        String classname = null;
        String description = null;
        String duedate = null;
        //String[][] events = new String[4][];
        //Number of events
        int eventNum = 0;
        ArrayList<Assignment> events = new ArrayList<Assignment>();
        if (calendar != null) {
            //Jagged array, had to flip 90 degrees. events[0][event#] = Summary, [1] = Description, [2] = Categories, [3] = Dtstart

            //myCalendarString = "OPENED";
            for (Iterator i = calendar.getComponents().iterator(); i.hasNext(); ) {
                Component component = (Component) i.next();

                for (Iterator j = component.getProperties().iterator(); j.hasNext(); ) {
                    Property property = (Property) j.next();
                    //myCalendarString += (property.getName() + ", " + property.getValue() + "|");
                    if (property.getName() == "SUMMARY"){
                        //property.getValue() gets the summary of the event
                        title = property.getValue();
                    }
                    if (property.getName() == "DESCRIPTION"){
                        //property.getValue() gets the description
                        description = property.getValue();
                    }
                    if (property.getName() == "CATEGORIES"){
                        //property.getValue() gets the name of the class
                        classname = property.getValue();
                    }
                    if (property.getName() == "DTSTART"){
                        //Time in format ("yyyyMMdd'T'HHmmss")
                        duedate = property.getValue();
                        int duehour = Integer.parseInt(duedate.substring(9, 11)) - 7;
                        if (duehour < 0){
                            duehour = 24 + duehour;
                        }

                        duedate = (duedate.substring(0, 4) + "-" + duedate.substring(4,6) + "-" + duedate.substring(6,8) + " at " + Integer.toString(duehour) + ":" + duedate.substring(11,13));
                    }
                }
                events.add(new Assignment(classname, title, description, duedate));
            }
        }
        else {
            classname = "No ical file found";
            duedate = "Never";
            events.add(new Assignment(classname, title, description, duedate));
        }

        ////Assignment test_assignment_1= new Assignment(title, classname, description, duedate);
        ////AssignList.add(test_assignment_1);
        for (Assignment event : events){
            AssignList.add(event);
        }

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
