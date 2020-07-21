package music.example.com.musicplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.media.MediaRouter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class playeractivity extends AppCompatActivity {
    Button btn_next,btn_previous,btn_pause;
    TextView songtext;
    SeekBar songseekbar;
    static MediaPlayer mymediaplayer;
    int position ;
    ArrayList<File> mysongs;
    Thread updateseekbar;
    String Name;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playeractivity);
        btn_next=(Button)findViewById(R.id.next);
        btn_previous=(Button)findViewById(R.id.previous);
        btn_pause=(Button)findViewById(R.id.pause);
        songtext=(TextView)findViewById(R.id.textview);
        songseekbar=(SeekBar)findViewById(R.id.seekbar);
        getSupportActionBar().setTitle(("Now Playing"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        updateseekbar=new Thread(){
            @Override
            public void run() {
               int totalduration=mymediaplayer.getDuration();
               int currentduration=0;
               while(currentduration<totalduration){
                   try{
                       sleep(500);
                       currentduration=mymediaplayer.getCurrentPosition();
                       songseekbar.setProgress(currentduration);

                   }
                   catch (InterruptedException e){
                       e.printStackTrace();
                   }
               }
            }
        };

        if(mymediaplayer!=null){
            mymediaplayer.stop();
            mymediaplayer.release();
        }
        Intent i =getIntent();
        Bundle bundle=i.getExtras();
        mysongs=(ArrayList)  bundle.getParcelableArrayList("songs");
        Name=mysongs.get(position).getName().toString();
        String songName=i.getStringExtra("songName");
        songtext.setText(songName);
        songtext.setSelected(true);
        position = bundle.getInt("pos",0);
        Uri  u = Uri.parse(mysongs.get(position).toString());
        mymediaplayer = MediaPlayer.create(getApplicationContext(),u);
        mymediaplayer.start();
        songseekbar.setMax(mymediaplayer.getDuration());
        updateseekbar.start();
        songseekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        songseekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);
        songseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mymediaplayer.seekTo(seekBar.getProgress());

            }
        });

btn_pause.setOnClickListener(new View.OnClickListener(){
    @Override
    public void onClick(View v) {
        songseekbar.setMax(mymediaplayer.getDuration());
        if(mymediaplayer.isPlaying()){
            btn_pause.setBackgroundResource(R.drawable.icon_play);
            mymediaplayer.pause();

        }else
        {
            btn_pause.setBackgroundResource(R.drawable.icon_pause);
            mymediaplayer.start();
        }
    }
});
btn_next.setOnClickListener(new View.OnClickListener(){
    public void onClick(View view){
        mymediaplayer.stop();
        mymediaplayer.release();
        position=(position+1);
        mysongs.size();
        Uri u =Uri.parse(mysongs.get(position).toString());
        mymediaplayer=MediaPlayer.create(getApplicationContext(),u);//meaning
        Name=mysongs.get(position).getName().toString();
        songtext.setText(Name);
        mymediaplayer.start();


    }
});
btn_previous.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mymediaplayer.stop();
        mymediaplayer.release();
        position=(position-1)<0?(mysongs.size()):(position-1);
        Uri u = Uri.parse(mysongs.get(position).toString());
        mymediaplayer=MediaPlayer.create(getApplicationContext(),u);//meaning
        Name=mysongs.get(position).getName().toString();
        songtext.setText(Name);
        mymediaplayer.start();


    }
});
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();

        }
        return super.onOptionsItemSelected(item);
    }
}
