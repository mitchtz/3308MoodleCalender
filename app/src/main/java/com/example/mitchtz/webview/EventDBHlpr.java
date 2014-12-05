package com.example.mitchtz.webview;

import java.util.ArrayList;
import java.util.List;

import com.example.mitchtz.webview.Assignment;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EventDBHlpr extends SQLiteOpenHelper {
	private Context con;
    public final static int DATABASE_VERSION=1;
	public EventDBHlpr(Context context) {
		super(context, "Assignments", null, DATABASE_VERSION);
		con=context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE Assignments (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, DESCRIPTION TEXT, DUE_DATE TEXT, CLASS TEXT");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	
	public void addRow(String Table,String Name,String Description,String DD,String course){
		SQLiteDatabase db=getWritableDatabase();
		Log.i("Custom","Attempting to insert row");
		Log.i("Custom",db.getPath());
		ContentValues cv=new ContentValues();
		cv.put("NAME", Name);
		cv.put("DESCRIPTION", Description);
		cv.put("DUE_DATE", DD);
		cv.put("CLASS", course);
		db.insert(Table, null, cv);
		
	}
	public void removeRow(String Table, String Name, String Description,String date){
		SQLiteDatabase db=getWritableDatabase();
		String[] Args={Name,Description,date};
		db.delete(Table,"NAME=? AND DESCRIPTION=? AND DUE_DATE=?",Args);
	}

	// Log.i lines are to provide insight into the stack trace
		
	public List<Assignment> getAssignments(){
		List<Assignment> events = new ArrayList<Assignment>();
		Assignment A;
		Cursor c;
		SQLiteDatabase db=getReadableDatabase();
		Log.i("Custom",db.getPath());
		c=db.rawQuery("SELECT * from Local_Tasks ",null);
		if(c.moveToFirst()){
			Log.i("Custom","Populating assignments");
			do{;
				A= new Assignment(c.getString(1),			//Name
						c.getString(4),				//Course
						c.getString(2),				//Description
						c.getString(3)              //
						);
				events.add(A);
				
		}while(c.moveToNext());
		}
		
		return events;
	}
	
}


