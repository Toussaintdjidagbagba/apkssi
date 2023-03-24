package mdp.dste.lassi;

import mdp.dste.lassi.Common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity2 extends AppCompatActivity {
    Common checkIn = new Common();
    SwipeRefreshLayout swipere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        swipere = findViewById(R.id.swipere);

        swipere.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(checkIn.isConnectedToInternet(MainActivity2.this)){
                    Intent ne = new Intent(MainActivity2.this,MainActivity.class);
                    startActivity(ne);
                }
                swipere.setRefreshing(false);
            }
        });

    }

}