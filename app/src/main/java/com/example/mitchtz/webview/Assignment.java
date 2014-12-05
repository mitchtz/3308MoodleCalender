package com.example.mitchtz.webview;

/**
 * Created by Chris_000 on 12/3/2014.
 */
public class Assignment {
    public int ID; //optional variable for potential reference
    public String list_val;
    public void Create_list_val(){
        list_val=Class+" - "+Name+ " Due: "+ Date;
    }

    private String Name;
    private String Description;
    private String Class;
    private String Date;

    public Assignment(String name,String course, String description, String date){
        Date=date;
        Name=name;
        Class=course;
        Description=description;
        Create_list_val();
    }

}
