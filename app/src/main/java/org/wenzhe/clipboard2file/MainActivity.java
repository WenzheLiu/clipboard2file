package org.wenzhe.clipboard2file;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * author: liuwenzhe2008@qq.com
 */
public class MainActivity extends AppCompatActivity {

    private static final int TITLE_MAX_CHAR = 30;
    private static final String PATTERN_FOR_FILE_NAME_RESERVED_CHAR =
            "[" + Pattern.quote("/\\?%*:|\"<>. ") + "]";

    private TextView titleView;
    private TextView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleView = (TextView) findViewById(R.id.titleView);
        contentView = (TextView) findViewById(R.id.contentView);

        handlePasteToTitle();
        handlePasteToContent();
        handleClean();
        handleSave();
        handleOpen();
    }

    private void handleOpen() {
        View openBtn = findViewById(R.id.openBtn);
        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasTitle()) {
                    Toast.makeText(MainActivity.this, "No title specified", Toast.LENGTH_LONG).show();
                    return;
                }
                File targetFile = getFileToSave();
                if (!targetFile.exists() && !save(targetFile) ) {
                    return;
                }
                Uri path = Uri.fromFile(targetFile);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path, "text/plain");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this,
                            "No Application Available to View File " + targetFile.getPath(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void handleSave() {
        View saveBtn = findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasTitle()) {
                    Toast.makeText(MainActivity.this, "No title specified", Toast.LENGTH_LONG).show();
                    return;
                }
                File targetFile = getFileToSave();
                save(targetFile);
            }
        });
    }

    private void handlePasteToContent() {
        View pasteToContentBtn = findViewById(R.id.pasteToContentBtn);
        pasteToContentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clipText = getPasteContent();
                if (clipText != null) {
                    contentView.setText(clipText.trim());
                }
            }
        });
    }

    private void handleClean() {
        View cleanBtn = findViewById(R.id.cleanBtn);
        cleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleView.setText("");
                contentView.setText("");
            }
        });
    }

    private void handlePasteToTitle() {
        View pasteToTitleBtn = findViewById(R.id.pasteToTitleBtn);
        pasteToTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clipText = getPasteContent();
                if (clipText == null) {
                    return;
                }
                clipText = clipText.trim();
                if (clipText.length() > TITLE_MAX_CHAR) {
                    clipText = clipText.substring(0, TITLE_MAX_CHAR);
                }
                int lineSep = clipText.indexOf("\n");
                if (lineSep != -1) {
                    clipText = clipText.substring(0, lineSep);
                }
                titleView.setText(clipText);
            }
        });
    }

    private boolean save(File targetFile) {
        try {
            if (!targetFile.exists()) {
                targetFile.getParentFile().mkdirs();
                targetFile.createNewFile();
            }
            FileWriter out = new FileWriter(targetFile);
            try {
                out.write(contentView.getText().toString());
            } finally {
                out.close();
            }
            String msg = MessageFormat.format("Save to {0}", targetFile.getPath());
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            return true;
        } catch (IOException e) {
            Log.e("save file failed", e.getMessage(), e);
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private File getFileToSave() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String datePath = df.format(Calendar.getInstance().getTime());

        File extStorageDir = Environment.getExternalStorageDirectory();
        File targetDir = new File(extStorageDir, "clipboard2file/" + datePath);

        String targetFileName = convertToValidFileName(titleView.getText().toString());
        return new File(targetDir, targetFileName + ".txt");
    }

    private boolean hasTitle() {
        return !titleView.getText().toString().trim().isEmpty();
    }

    /**
     * valid file name see: https://en.wikipedia.org/wiki/Filename
     */
    private String convertToValidFileName(String str) {
        return str.trim().replaceAll(PATTERN_FOR_FILE_NAME_RESERVED_CHAR, "_");
    }

    /**
     * @return null when clipboard is empty
     */
    @Nullable
    private String getPasteContent() {
        // Gets a handle to the clipboard service.
        ClipboardManager mClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        StringBuilder sb = new StringBuilder();
        if (!mClipboard.hasPrimaryClip()) {
            Toast.makeText(this,
                    "Clipboard is empty", Toast.LENGTH_SHORT).show();
            return null;
        }
        ClipData clipData = mClipboard.getPrimaryClip();
        int count = clipData.getItemCount();

        for (int i = 0; i < count; ++i) {
            ClipData.Item item = clipData.getItemAt(i);
            CharSequence str = item.coerceToText(this);
            Log.i("pasteToResult", "item : " + i + ": " + str);
            sb.append(str);
        }
        return sb.toString();
    }
}
