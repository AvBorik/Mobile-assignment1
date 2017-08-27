package pro.avborik.assignment1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import static pro.avborik.assignment1.R.layout.main;


public class mpaint extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(main);

        final mpaintAction action1 = (mpaintAction) findViewById(R.id.customView);
        Button buttonClear = (Button) findViewById(R.id.buttonClear);
        Button buttonUndo = (Button) findViewById(R.id.buttonUndo);
        Button buttonRedo = (Button) findViewById(R.id.buttonRedo);
        final EditText text = (EditText) findViewById(R.id.editText);

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
        buttonRedo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                action1.Redo();
            }
        });
        text.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                int x = Integer.parseInt(text.getText().toString());
                action1.ChangeBrush(x);

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });
    }
}
