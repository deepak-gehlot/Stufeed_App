package com.stufeed.android.view.fragment.audioplayer;

import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.databinding.RowAudioPlayerBinding;

/**
 * Created by Deepak Gehlot on 2/7/2018.
 */

public class PlayerDialogFragment extends DialogFragment {

    Handler seekHandler = new Handler();
    private MediaPlayer mp;
    private String audioUrl = "";
    private RowAudioPlayerBinding binding;
    Runnable run = new Runnable() {
        @Override
        public void run() {
            seekUpdate();
        }
    };

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
        audioUrl = getArguments().getString("url");
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
    }

    /**
     * Create Audio player
     */
    private void audioPlayer() {
        //set up MediaPlayer
        mp = new MediaPlayer();
        try {
            mp.setDataSource(audioUrl);
            mp.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle Audio player play pause
     */
    public void playPauseAudio() {
        if (mp != null) {
            binding.audioSeekBar.setMax(mp.getDuration());
            if (mp.isPlaying()) {
                mp.pause();
                seekHandler.removeCallbacks(run);
                binding.audioStartStopImg.setImageResource(R.drawable.ic_video_play);
            } else {
                mp.start();
                binding.audioStartStopImg.setImageResource(R.drawable.ic_video_pause);
                seekUpdate();
            }
        }
    }

    /**
     * Handle Audio Player seek bar
     */
    public void seekUpdate() {
        binding.audioSeekBar.setProgress(mp.getCurrentPosition());
        seekHandler.postDelayed(run, 1000);
    }
}
