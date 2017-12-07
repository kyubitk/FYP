package com.example.mcnoir.qna;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    DBManager helper = new DBManager(this);
    SQLiteDatabase db;
    MyListAdapter adapterHere = null;
    public ArrayList<String> strArr2 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateListV2();

        Button btnNxt = findViewById(R.id.tofilter);
        btnNxt.setOnClickListener(this);

        ListView lvl = findViewById(R.id.listViewM);
        //lv.setAdapter(new MyListAdapter(this, R.layout.qv_list, strArr));
        adapterHere = new MyListAdapter(this, R.layout.activitylist, strArr2);
        lvl.setAdapter(adapterHere);

        lvl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Toast.makeText(MainActivity.this, "Clicked ", Toast.LENGTH_SHORT).show();

            }

        });


    }

    public void onClick(View view) {
        if (view.getId() == R.id.tofilter)
        {
            Intent nextScreen1 = new Intent(MainActivity.this, Qlist.class);
            startActivity(nextScreen1);
        }

    }

    public void populateListV2()
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
        if(Double.parseDouble(c.getString(2))>= 5)
        {
            String newLine = System.getProperty("line.separator"); // add new line
            strArr2.add("Q" + c.getString(0) + ":  " + c.getString(1) + " SCORE-->" + c.getString(2));
        }

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

            viewHolder.button = (Button) convertView.findViewById(R.id.tofilter);

            viewHolder.title = (TextView) convertView.findViewById(R.id.ofQuest);

            adapterHere.notifyDataSetChanged();



            convertView.setTag(viewHolder);


            //else

            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.title.setText(getItem(position));




            return convertView;

        }
    }

    public class ViewHolder {
        Button button;
        TextView title;
    }
}

