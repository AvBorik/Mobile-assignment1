package pro.avborik.assignment1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import static pro.avborik.assignment1.R.layout.main;


public class mpaint extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(main);

        final mpaintAction action1 = (mpaintAction) findViewById(R.id.customView);
        Button buttonClear = (Button) findViewById(R.id.buttonClear);
        Button buttonUndo = (Button) findViewById(R.id.buttonUndo);
        buttonClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                action1.Clear();
            }
        });
        buttonUndo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                action1.Undo();
            }
        });
    }
}
