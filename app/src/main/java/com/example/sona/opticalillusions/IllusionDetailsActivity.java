package com.example.sona.opticalillusions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.sona.opticalillusions.model.Illusion;

import java.util.Stack;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class IllusionDetailsActivity extends AppCompatActivity {

    private Realm realm;
    private RealmHelper realmHelper;
    private Illusion currentIllusion;
    private ImageView imageView;
    private TextView category;
    private TextView title;
    private ImageButton setFavourite;
    private TextView textView;
    private VideoView videoView;
    private Stack stack;
    private GridElementAdapter adapter;
    private HorizontalGridView horizontalGridView;
    private boolean isVideoBeingTouched = false;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illusion_details);

        stack = new Stack();
        currentIllusion = getIntent().getExtras().getParcelable("item");

        Realm.init(this);
        final RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        realmHelper = new RealmHelper(realm);

//               // 1. Create a default TrackSelector
//        Handler mainHandler = new Handler();
//        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//        TrackSelection.Factory videoTrackSelectionFactory =
//                new AdaptiveTrackSelection.Factory(bandwidthMeter);
//        TrackSelector trackSelector =
//                new DefaultTrackSelector(videoTrackSelectionFactory);
//
//        // 2. Create the player
//        SimpleExoPlayer player =
//                ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        int height = display.heightPixels;
        int statusBarHeight = getStatusBarHeight();
        int toolbarHeight = (height / 13);
        int bottomHeight = height - width - toolbarHeight - statusBarHeight;
        int buttonSize = (bottomHeight / 10) * 3;

        Toolbar toolbar = (Toolbar) findViewById(R.id.top_toolbar_details);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        setCustomParams(toolbar, width, toolbarHeight);

        ImageView logo = (ImageView) findViewById(R.id.ib_logo);
        setCustomParams(logo, toolbarHeight, toolbarHeight);
        int p = toolbarHeight / 10;
        logo.setPadding(p, p, p, p);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IllusionDetailsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Giorgio.ttf");
        title = (TextView) findViewById(R.id.tv_title);
        title.setTypeface(type);

        category = (TextView) findViewById(R.id.tv_category);
        category.setVisibility(View.VISIBLE);
        category.setTypeface(type);

        RelativeLayout.LayoutParams rlTitle = new RelativeLayout.LayoutParams(width - 4 * toolbarHeight / 3, 2 * toolbarHeight / 3 - toolbarHeight);
        rlTitle.addRule(RelativeLayout.ALIGN_PARENT_END);
        title.setLayoutParams(rlTitle);
        title.requestLayout();

        RelativeLayout.LayoutParams rlCategory = new RelativeLayout.LayoutParams(width - 4 * toolbarHeight / 3, toolbarHeight / 3 - toolbarHeight);
        rlCategory.addRule(RelativeLayout.ALIGN_PARENT_END);
        rlCategory.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rlCategory.addRule(RelativeLayout.BELOW, R.id.tv_title);
        category.setLayoutParams(rlCategory);
        category.requestLayout();

        imageView = (ImageView) findViewById(R.id.iv_view_illusion);
        videoView = (VideoView) findViewById(R.id.vv_video);
        textView = (TextView) findViewById(R.id.tv_description);

        imageView.setLayoutParams(new RelativeLayout.LayoutParams(width, width));
        videoView.setLayoutParams(new RelativeLayout.LayoutParams(width, width));
        textView.setLayoutParams(new RelativeLayout.LayoutParams(width, width));

//        videoView.setPlayer(player);
//
//        // Measures bandwidth during playback. Can be null if not required.
//        bandwidthMeter = new DefaultBandwidthMeter();
//// Produces DataSource instances through which media data is loaded.
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
//                Util.getUserAgent(context, "yourApplicationName"), bandwidthMeter);
//// Produces Extractor instances for parsing the media data.
//        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//// This is the MediaSource representing the media to be played.
//        MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri,
//                dataSourceFactory, extractorsFactory, null, null);
//// Prepare the player with the source.
//        player.prepare(videoSource);




       videoView.setVideoPath(currentIllusion.getAnimation());
//        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), currentIllusion.getPicture());
//        videoView.setBackground(drawable);

        handler = new Handler();

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isVideoBeingTouched) {
                    isVideoBeingTouched = true;
                    if (videoView.isPlaying()) {
                        videoView.pause();
                    } else {
                        videoView.start();
                    }
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            isVideoBeingTouched = false;
                        }
                    }, 100);
                }
                return true;
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.setDisplay(null);
                mp.reset();
                mp.setDisplay(videoView.getHolder());
                videoView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }
        });

        imageView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                textView.setVisibility(View.VISIBLE);
                textView.setMovementMethod(new ScrollingMovementMethod());
                ScrollingMovementMethod.getInstance();
            }

            @Override
            public void onClick() {
                if (haveNetworkConnection()) {
                    imageView.setVisibility(View.GONE);
                    videoView.setVisibility(View.VISIBLE);
                    Toast.makeText(IllusionDetailsActivity.this, R.string.animation_loading, Toast.LENGTH_SHORT).show();
                    videoView.start();
                    videoView.setBackground(null);
                } else {
                    Toast.makeText(IllusionDetailsActivity.this, R.string.connect_to_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });

        textView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeRight() {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(currentIllusion.getPicture());
                textView.setVisibility(View.GONE);
            }
        });

        RelativeLayout ll = (RelativeLayout) findViewById(R.id.linearLayout2);
        ll.requestLayout();
        ll.getLayoutParams().width = width;
        ll.getLayoutParams().height = 2 * bottomHeight / 5;

        ImageButton back = (ImageButton) findViewById(R.id.b_last_viewed);

        setCustomParams(back, buttonSize, buttonSize);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.gv_favourites_grid) != null) {
                    GridView gv = (GridView) findViewById(R.id.gv_favourites_grid);
                    gv.invalidateViews();
                }
                if (stack.isEmpty()) {
                    finish();
                } else {
                    updateActivity((Illusion) stack.pop());
                }
            }
        });

        ImageButton toAll = (ImageButton) findViewById(R.id.b_to_all);
        setCustomParams(toAll, buttonSize, buttonSize);
        toAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                stack.clear();
            }
        });

        setFavourite = (ImageButton) findViewById(R.id.b_to_favourites);
        setCustomParams(setFavourite, buttonSize, buttonSize);
        setFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realmHelper.setFavourite(realm, v, currentIllusion);
            }
        });

        horizontalGridView = (HorizontalGridView) findViewById(R.id.gv_small_preview);
        horizontalGridView.setLayoutParams(new LinearLayout.LayoutParams(width, 3 * bottomHeight / 5));
        adapter = new GridElementAdapter(this, realm.where(Illusion.class).findAll(), 3 * bottomHeight / 5);
        horizontalGridView.setAdapter(adapter);

        updateActivity(currentIllusion);
    }

    public void updateActivity(Illusion illusion) {
        currentIllusion = illusion;
        title.setText(illusion.getName());
        category.setText(illusion.getCategory());
        imageView.setImageResource(illusion.getPicture());
        videoView.setVideoPath(illusion.getAnimation());

        if (illusion.isfavourite()) {
            setFavourite.setImageResource(R.drawable.ic_unfavourite);
        } else {
            setFavourite.setImageResource(R.drawable.ic_favourite);
        }
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).getName().equals(illusion.getName())) {
                horizontalGridView.smoothScrollToPosition(i);
                break;
            }
        }

        textView.setMovementMethod(new ScrollingMovementMethod());
        ScrollingMovementMethod.getInstance();
        textView.setText(currentIllusion.getDescription());

        imageView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        videoView.stopPlayback();
        videoView.setVisibility(View.GONE);
    }

    /**
     * Adds illusion to stack.
     */
    public void addIllusionToStack() {
        stack.push(currentIllusion);
    }

    /**
     * Sets custom paramaters of a view.
     * @param v view
     * @param width width
     * @param height height
     */
    public void setCustomParams(View v, int width, int height) {
        v.requestLayout();
        v.getLayoutParams().width = width;
        v.getLayoutParams().height = height;
    }

    /**
     * Checks if network connections exists.
     * @return true/false
     */
    private boolean haveNetworkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    /**
     * Gets current status bar height.
     * @return int
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
