package com.example.niriko.spritztestapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
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
    int pivot = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                wordsPerMinute.setEnabled(false);
                String text = String.valueOf(editText.getText().toString());
                final String[] words = text.split("\\s+");

                if (words.length>0) {
                    String wordsPerM = String.valueOf(wordsPerMinute.getText().toString());
                    if (wordsPerM!=null && wordsPerM.length()>0) {
                        final int wpm = Integer.parseInt(wordsPerM);

                        final Handler handler = new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                count++;
                                handler.postDelayed(this, 1000 * 60 / wpm);

                                final String[] word = words[count].split("(?!^)");

                                switch (word.length) {
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
                                }
                                String newString = words[count].replaceFirst(word[pivot], "<font color='#ff0000'>" + word[pivot] + "</font>");
                                textspace.setText(Html.fromHtml(newString));
                            }
                        });
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
