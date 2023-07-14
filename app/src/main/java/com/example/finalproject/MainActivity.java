package com.example.finalproject;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private EditText editText1;
    private EditText editText2;
    private Button copyButton;
    private Button pasteButton;
    private Button displayButton;

    private ClipboardManager clipboardManager;

    private static final String FILE_NAME = "clipboard_data.txt";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        copyButton = findViewById(R.id.button);
        pasteButton = findViewById(R.id.button2);
        displayButton = findViewById(R.id.button3);

        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToCopy = editText1.getText().toString();
                if (textToCopy.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No content to copy", Toast.LENGTH_SHORT).show();
                } else {
                    copyToClipboard(textToCopy);
                }
            }
        });

        pasteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clipboardContent = getClipboardContent();
                if (clipboardContent.isEmpty()) {
                    Toast.makeText(MainActivity.this, "There is no content copied to paste", Toast.LENGTH_SHORT).show();
                } else {
                    editText2.setText(clipboardContent);
                }
            }
        });

        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileContent = getFileContent();
                Toast.makeText(MainActivity.this, fileContent, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void copyToClipboard(String text) {
        ClipData clipData = ClipData.newPlainText("Text", text);
        clipboardManager.setPrimaryClip(clipData);
        saveClipboardDataToFile(text);
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    private String getClipboardContent() {
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData != null && clipData.getItemCount() > 0) {
            ClipData.Item item = clipData.getItemAt(0);
            if (item.getText() != null) {
                return item.getText().toString();
            }
        }
        return "";
    }

    private void saveClipboardDataToFile(String text) {
        try {
            File file = new File(getExternalFilesDir(null), FILE_NAME);
            FileWriter writer = new FileWriter(file, true);
            writer.append(text);
            writer.append("\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileContent() {
        StringBuilder fileContent = new StringBuilder();
        try {
            File file = new File(getExternalFilesDir(null), FILE_NAME);
            if (!file.exists() || file.length() == 0) {
                return "No content to display";
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent.toString();
    }
}