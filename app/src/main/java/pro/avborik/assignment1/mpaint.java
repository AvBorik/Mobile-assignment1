package pro.avborik.assignment1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import static pro.avborik.assignment1.R.layout.main;


public class mpaint extends AppCompatActivity implements OnClickListener {

    Button clear;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(main);

        clear = (Button) findViewById(R.id.buttonClear);
        clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
