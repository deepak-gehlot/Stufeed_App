package com.stufeed.android.view.fragment.audioplayer;

import android.app.DialogFragment;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.stufeed.android.R;
import com.stufeed.android.databinding.RowAudioPlayerBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Deepak Gehlot on 2/7/2018.
 */

public class PlayerDialogFragment extends DialogFragment implements ExoPlayer.EventListener {

    Handler seekHandler = new Handler();
    private String audioUrl = "";
    private RowAudioPlayerBinding binding;
    Runnable run = new Runnable() {
        @Override
        public void run() {
            seekUpdate();
        }
    };

    private Handler mainHandler;
    private RenderersFactory renderersFactory;
    private BandwidthMeter bandwidthMeter;
    private LoadControl loadControl;
    private DataSource.Factory dataSourceFactory;
    private ExtractorsFactory extractorsFactory;
    private MediaSource mediaSource;
    private TrackSelection.Factory trackSelectionFactory;
    private SimpleExoPlayer player;
    private String streamUrl = "http://bbcwssc.ic.llnwd.net/stream/bbcwssc_mp1_ws-einws"; //bbc world service url
    private TrackSelector trackSelector;

    public static PlayerDialogFragment newInstance(String url) {
        Bundle args = new Bundle();
        PlayerDialogFragment fragment = new PlayerDialogFragment();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog_Alert);
        streamUrl = getArguments().getString("url");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.row_audio_player,
                container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        audioPlayer();

        binding.audioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            seekHandler.removeCallbacks(run);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create Audio player
     */
    private void audioPlayer() {
        renderersFactory = new DefaultRenderersFactory(getApplicationContext());
        bandwidthMeter = new DefaultBandwidthMeter();
        trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        loadControl = new DefaultLoadControl();

        player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
        player.addListener(this);

        dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), "ExoplayerDemo");
        extractorsFactory = new DefaultExtractorsFactory();
        mainHandler = new Handler();
        mediaSource = new ExtractorMediaSource(Uri.parse(streamUrl),
                dataSourceFactory,
                extractorsFactory,
                mainHandler,
                null);

        player.prepare(mediaSource);
        binding.audioStartStopImg.setImageResource(R.drawable.ic_video_pause);
    }

    /**
     * Handle Audio player play pause
     */
    public void playPauseAudio() {
        if (player != null) {
            if (player.getPlayWhenReady()) {
                seekHandler.removeCallbacks(run);
                binding.audioStartStopImg.setImageResource(R.drawable.ic_video_play);
            } else {
                binding.audioStartStopImg.setImageResource(R.drawable.ic_video_pause);
                seekUpdate();
            }
            player.setPlayWhenReady(!player.getPlayWhenReady());
        }
    }

    /**
     * Handle Audio Player seek bar
     */
    public void seekUpdate() {
        if (player != null) {
            binding.audioSeekBar.setProgress((int) player.getCurrentPosition());
            seekHandler.postDelayed(run, 1000);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        player.setPlayWhenReady(true);

    }

    @Override
    public void onPause() {
        super.onPause();
        player.setPlayWhenReady(false);

    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        if (!isLoading) {
            Log.e("player", "is loading false");
            binding.audioSeekBar.setMax((int) player.getDuration());
            seekUpdate();
            binding.progressBar.setVisibility(View.GONE);
        } else {
            Log.e("player", "is loading true");
            binding.progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
}
