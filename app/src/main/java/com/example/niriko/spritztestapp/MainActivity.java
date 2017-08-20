package com.example.niriko.spritztestapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import com.example.niriko.spritztestapp.views.CustomView;

public class MainActivity extends AppCompatActivity {

    CustomView customView;
    TextView textspace;
    EditText editText;
    Button button;
    Button button2;
    Button button3;
    EditText wordsPerMinute;
    ClipboardManager paste;

    int count=-1;
    int pivot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomView customView = new CustomView(this);
        final TextView textspace = (TextView)findViewById(R.id.textspace);
        final EditText editText = (EditText) findViewById(R.id.editText);
        final Button button = (Button)findViewById(R.id.button);
        final Button buttonPaste = (Button)findViewById(R.id.button2);
        final Button buttonErase = (Button)findViewById(R.id.button3);
        final EditText wordsPerMinute = (EditText) findViewById(R.id.wordsPerMinute);

        paste = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        buttonPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonErase.setEnabled(true);
                buttonPaste.setEnabled(false);
                button.setEnabled(true);
                ClipData pasteData = paste.getPrimaryClip();
                ClipData.Item item = pasteData.getItemAt(0);
                String wholeText = item.getText().toString();
                editText.setText(wholeText);
            }
        });

        buttonErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonErase.setEnabled(false);
                buttonPaste.setEnabled(true);
                button.setEnabled(false);
                editText.getText().clear();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = String.valueOf(editText.getText().toString());
                final String[] words = text.split("\\s+");

                for (int i = 0; i < words.length; i++) {
                    words[i] = words[i].replaceAll("[^\\w]", "");
                    words[i] = words[i].toLowerCase();
                }

                if (words!=null && words.length>0) {
                    final int wordNumber = words.length;
                    String wordsPerM = String.valueOf(wordsPerMinute.getText().toString());
                    if (wordsPerM!=null && wordsPerM.length()>0) {
                        final int wpm = Integer.parseInt(wordsPerM);
                        Thread t = new Thread() {
                            @Override
                            public void run() {
                                while (!isInterrupted()) {
                                    try {
                                        Thread.sleep(1000 * 60 / wpm);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                count++;

                                                switch (words[count].length())
                                                {
                                                    case 0:
                                                    case 1:
                                                        pivot = 0;
                                                        break;
                                                    case 2:
                                                    case 3:
                                                    case 4:
                                                    case 5:
                                                        pivot = 1;
                                                        break;
                                                    case 6:
                                                    case 7:
                                                    case 8:
                                                    case 9:
                                                        pivot = 2;
                                                        break;
                                                    case 10:
                                                    case 11:
                                                    case 12:
                                                    case 13:
                                                        pivot = 3;
                                                        break;
                                                    default:
                                                        pivot = 4;
                                                };
                                                String coloredWord = Integer.toString(pivot);

                                                textspace.setText(words[count]);
                                            }
                                        });
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                        t.start();
                    }else {
                        textspace.setText("?words per minute?");
                    }
                }else{
                    textspace.setText("nothing to read");
                }
            }
        });


    }
}
