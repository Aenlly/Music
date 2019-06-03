/*
 *  Copyright (C) 2015, Jhuster, All Rights Reserved
 *
 *  Author:  Jhuster(lujun.hust@gmail.com)
 *  
 *  https://github.com/Jhuster/JNote
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 */
package com.example.music;

import java.util.Calendar;
import com.example.music.db.NoteDB;
import com.example.music.db.NoteDB.Note;
import com.example.music.markdown.MDWriter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class NoteActivity extends Activity {
        
    private Note mNote = new Note();
    private MDWriter mMDWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getActionBar().setDisplayHomeAsUpEnabled(true);  
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);        
        mMDWriter = new MDWriter((EditText)findViewById(R.id.NoteEditText));
        mNote.key = getIntent().getLongExtra("NoteId",-1);
        if(mNote.key!=-1) {
            Note note = NoteDB.getInstance().get(mNote.key);
            if(note!=null) {
                mMDWriter.setContent(note.content);
                mNote = note;
            }      
            else {
                mNote.key=-1;   
            }            
        }
    }
    
    @Override
    protected void onDestroy() {        
        super.onDestroy();        
    }
    
    @Override 
    protected void onPause() {
        onSaveNote();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_display) {
            onSaveNote();
            Intent intent = new Intent(this,DisplayActivity.class);
            intent.putExtra("Content",mMDWriter.getContent());
            startActivity(intent);            
            return true;
        }
        else if(id == android.R.id.home) {       
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void onClickHeader(View v) {
        mMDWriter.setAsHeader();
    }
    
    public void onClickCenter(View v) {
        mMDWriter.setAsCenter();
    }
    
    public void onClickList(View v) {
        mMDWriter.setAsList();
    }
    
    public void onClickBold(View v) {
        mMDWriter.setAsBold();
    }
    
    public void onClickQuote(View v) {
        mMDWriter.setAsQuote();
    }
    
    public void onSaveNote() {        
        mNote.title = mMDWriter.getTitle();
        mNote.content = mMDWriter.getContent();        
        if(mNote.key==-1) {
            if(!"".equals(mNote.content)) {
                mNote.date = Calendar.getInstance().getTimeInMillis();
                NoteDB.getInstance().insert(mNote);   
            }            
        }
        else {
            NoteDB.getInstance().update(mNote);
        }
    }
}
