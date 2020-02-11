package com.androidwave.exoplayer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.buttonPlayUrlVideo)
    Button buttonPlayUrlVideo;
    @BindView(R.id.buttonPlayDefaultVideo)
    Button buttonPlayDefaultVideo;
    @BindView(R.id.VV_videoFromRaw)
    PlayerView VVVideoFromRaw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
    }

    @OnClick({R.id.buttonPlayUrlVideo, R.id.buttonPlayDefaultVideo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonPlayUrlVideo:
                showDialogPrompt();
                break;
            case R.id.buttonPlayDefaultVideo:
//                Intent mIntent = ExoPlayerActivity.getStartIntent(this, VideoPlayerConfig.DEFAULT_VIDEO_URL);
//                startActivity(mIntent);
                playVideoFromRaw();
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

        final EditText userInputURL =  promptsView
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

    private void playVideoFromRaw() {
        VVVideoFromRaw.setVisibility(View.VISIBLE);

//        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(
//                new DefaultRenderersFactory(this),
//                new DefaultTrackSelector(), new DefaultLoadControl());
//
//        String videoPath = RawResourceDataSource.buildRawResourceUri(R.raw.sample_video).toString();
//
//        Uri uri = RawResourceDataSource.buildRawResourceUri(R.raw.sample_video);
//
//        ExtractorMediaSource audioSource = new ExtractorMediaSource(
//                uri,
//                new DefaultDataSourceFactory(this, "MyExoplayer"),
//                new DefaultExtractorsFactory(),
//                null,
//                null
//        );
//
//        player.prepare(audioSource);
//        VVVideoFromRaw.setPlayer(player);
//        VVVideoFromRaw.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
//        player.setPlayWhenReady(true);
//
//----------------------------------------------------------------------------------
        // Setup Exoplayer instance
        SimpleExoPlayer exoPlayer = ExoPlayerFactory
                .newSimpleInstance(new DefaultRenderersFactory(this)
                        , new DefaultTrackSelector()
                        , new DefaultLoadControl());
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "simpleExoPlayer"));

        //Getting media from raw resource
        MediaSource firstSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(RawResourceDataSource
                        .buildRawResourceUri(R.raw.sample_video));
        //Prepare the exoPlayer with the source
        exoPlayer.prepare(firstSource);

        VVVideoFromRaw.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(true);

    }
}
