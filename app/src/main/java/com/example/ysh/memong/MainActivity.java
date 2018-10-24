package com.example.ysh.memong;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Realm realm;
    private RecyclerView rcv;
    private RcvAdapter rcvAdapter;
    private Memo memo_Main;
    public List<Memo> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textView = findViewById(R.id.mContextTextView);
        rcv = findViewById(R.id.rcvMain);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
        rcv.setLayoutManager(linearLayoutManager);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        RealmResults<Memo> realmResults = realm.where(Memo.class)
                .findAllAsync();

        for(Memo memo : realmResults) {
            list.add(new Memo(memo.getText()));
            rcvAdapter = new RcvAdapter(MainActivity.this,list);
            rcv.setAdapter(rcvAdapter);
        }

        FloatingActionButton button = findViewById(R.id.floating);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == RESULT_OK) {

            String title = data.getStringExtra("title");
            String time = data.getStringExtra("time");
            Toast.makeText(this,title + "," + time,Toast.LENGTH_SHORT).show();

            realm.beginTransaction();
            memo_Main = realm.createObject(Memo.class);
            memo_Main.setText(title);

            realm.commitTransaction();

            RealmResults<Memo> realmResults = realm.where(Memo.class)
                    .equalTo("text",title)
                    .findAllAsync();

            list.add(new Memo(title));
            rcvAdapter = new RcvAdapter(MainActivity.this,list);
            rcv.setAdapter(rcvAdapter);

        }
    }
}
