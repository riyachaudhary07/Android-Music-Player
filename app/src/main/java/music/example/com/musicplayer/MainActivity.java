package music.example.com.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView myListview;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myListview=(ListView)findViewById(R.id.mySongListView);
        runtimepermission();
    }
    public void runtimepermission(){
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                Display();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();

            }
        }).check();
    }
    public ArrayList<File> findSong(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();
        for (File singlefile : files) {
            if (singlefile.isDirectory() && !singlefile.isHidden()) {
                arrayList.addAll(findSong(singlefile));


            } else {
                if (singlefile.getName().endsWith(".mp3") ||
                        singlefile.getName().endsWith(".wav")) {
                    arrayList.add(singlefile);


                }
            }
        }

        return arrayList;
    }
    void Display(){
        final ArrayList<File> mysongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[mysongs.size()];
        for(int i=0;i<mysongs.size();i++){
            items[i]=mysongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");

        }

        ArrayAdapter<String> myadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        myListview.setAdapter(myadapter);
        myListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                String songName=myListview.getItemAtPosition(i).toString();
                startActivity(new Intent(getApplicationContext(),playeractivity.class).putExtra("songs",mysongs).putExtra("songName",songName).putExtra("pos",i));


            }
        });

    }

}
