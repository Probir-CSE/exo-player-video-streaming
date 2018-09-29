package com.androidwave.exoplayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.buttonPlayUrlVideo)
    Button buttonPlayUrlVideo;
    @BindView(R.id.buttonPlayDefaultVideo)
    Button buttonPlayDefaultVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.buttonPlayUrlVideo, R.id.buttonPlayDefaultVideo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonPlayUrlVideo:
                showDialogPrompt();
                break;
            case R.id.buttonPlayDefaultVideo:
                Intent mIntent = ExoPlayerActivity.getStartIntent(this, VideoPlayerConfig.DEFAULT_VIDEO_URL);
                startActivity(mIntent);
                break;
        }
    }

    private void showDialogPrompt() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInputURL = (EditText) promptsView
                .findViewById(R.id.editTextDialogUrlInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                boolean isURL = Patterns.WEB_URL.matcher(userInputURL.getText().toString().trim()).matches();
                                if (isURL) {
                                    Intent mIntent = ExoPlayerActivity.getStartIntent(MainActivity.this, userInputURL.getText().toString().trim());
                                    startActivity(mIntent);
                                } else {
                                    Toast.makeText(MainActivity.this, getString(R.string.error_message_url_not_valid), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
}
