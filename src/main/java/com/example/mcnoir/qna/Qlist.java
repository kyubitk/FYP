package com.example.mcnoir.qna;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by McNoir on 27/11/2017.
 */
public class Qlist extends Activity implements View.OnClickListener  {

    DBManager helper = new DBManager(this);
    SQLiteDatabase db;
    MyListAdapter adapterHere = null;
    public ArrayList<String> strArr = new ArrayList<>();
    private double resetInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlist);


        populateListV();

        Button btnMsg = findViewById(R.id.QuestButton);
        btnMsg.setOnClickListener(this);

        ListView lv = findViewById(R.id.listView);
        //lv.setAdapter(new MyListAdapter(this, R.layout.qv_list, strArr));
        adapterHere = new MyListAdapter(this, R.layout.qv_list, strArr);
        lv.setAdapter(adapterHere);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent nextScreen = new Intent(Qlist.this, Comments.class);
                startActivity(nextScreen);
                Toast.makeText(Qlist.this, "Clicked ", Toast.LENGTH_SHORT).show();

            }

        });



    }

    public void onClick(View v) {

        if (v.getId() == R.id.QuestButton)
        {
            EditText message_txt = findViewById(R.id.Ent_quest);
            String message_txtstr = message_txt.getText().toString();

            if (message_txtstr.isEmpty())
            {
                Toast.makeText(Qlist.this, "You Entered Nothing", Toast.LENGTH_LONG).show();

            }
            else
            {

                DBManager helper = new DBManager(this);
                try {
                    db = helper.open();
                }
                catch(SQLException e){
                    //
                    e.printStackTrace();
                }


                double filler_num = 1.0;
                helper.insertMSG(message_txtstr, filler_num);

                helper.close();
                Intent nextScreen1 = new Intent(Qlist.this, Qlist.class);
                startActivity(nextScreen1);
                Toast.makeText(Qlist.this, "Question Added", Toast.LENGTH_LONG).show();


            }

        }else {

            if (v.getId() == R.id.OQuest) {
                Intent nextScreen1 = new Intent(Qlist.this, MainActivity.class);
                startActivity(nextScreen1);
                Toast.makeText(Qlist.this, "Clicked ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void populateListV()
    {
        try {
            db = helper.open();
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        Cursor c = helper.getAllMsgs();
        if(c.moveToFirst())
        {
            do
            {
                DisplayMsg(c);
                //DisplayPos(c);
            }
            while(c.moveToNext());
        }
        helper.close();

    }

    public void DisplayMsg(Cursor c)
    {
        String newLine = System.getProperty("line.separator"); // add new line
        strArr.add("Q" + c.getString(0) + ":  " + c.getString(1) + " SCORE-->" + c.getString(2));
    }


    private void updateMsg2(int position, double message_p)
    {
        try {
            db = helper.open();
        }
        catch(SQLException e){
            //
            e.printStackTrace();
        }

        if (helper.updatedMsg(position, message_p)) {
            Toast.makeText(this, "Updated Added!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Update NOT Changed!", Toast.LENGTH_LONG).show();
        }
        helper.close();
    }


    public class MyListAdapter extends ArrayAdapter<String> {

        private  int layout;
        public MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder mainViewholder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
            }
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.arrow = (ImageButton) convertView.findViewById(R.id.liked_btn);
                viewHolder.fav = (Button) convertView.findViewById(R.id.likedstar);
                viewHolder.title = (TextView) convertView.findViewById(R.id.questinfo);
                viewHolder.arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        int newPos = position+1;
                        //double value = 0;
                        try {
                            db = helper.open();
                        }
                        catch(SQLException e){
                            e.printStackTrace();
                        }

                        //Cursor c = helper.getAllMsgs();
                        //if(c != null)
                        //{

                          //  resetInt = Double.parseDouble(c.getString(2));
                            //c.moveToNext();

                        //}

                        Cursor c1 = helper.getPoints(newPos);

                        //double swap = Double.parseDouble(String.valueOf(c1.getDouble(0)))+1;
                        double swap = c1.getDouble(0)+1;
                        updateMsg2(newPos,swap);


                        //double vote = resetInt + 1;
                        //updateMsg2(newPos, vote);
                        //resetInt =0;

                        adapterHere.notifyDataSetChanged();
                        helper.close();

                        Toast.makeText(getContext(), "Button Clicked ", Toast.LENGTH_SHORT).show();

                    }
                });
                viewHolder.fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getContext(), "Button" + position + " Clicked ", Toast.LENGTH_SHORT).show();

                }
            });
                convertView.setTag(viewHolder);


            //else

                mainViewholder = (ViewHolder) convertView.getTag();
                mainViewholder.title.setText(getItem(position));




            return convertView;

        }
    }

    public class ViewHolder {

        ImageButton arrow;
        Button fav;
        TextView title;
    }

}