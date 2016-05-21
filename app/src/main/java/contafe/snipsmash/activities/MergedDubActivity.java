package contafe.snipsmash.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import contafe.snipsmash.R;
import cz.msebera.android.httpclient.Header;

public class MergedDubActivity extends AppCompatActivity {

    private RecyclerView snipView;
    private Button playButton;
    private Button saveButton;

    private String ffmpegBinary;
    private File outputDir;
    Integer counter;
    Integer numUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merged_dub);
        snipView = (RecyclerView) findViewById(R.id.recyclerView);
        snipView.setHasFixedSize(true);
        Intent intent = getIntent();
        ArrayList<String> urls = intent.getStringArrayListExtra("data");
        playButton = (Button) findViewById(R.id.playButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        Handler handler = new Handler();

        outputDir = this.getApplicationContext().getCacheDir();
        if (!outputDir.exists()) {
            System.err.println("THE CACHE DIR DOESN'T EXIST!!!");
        }

        counter = 0;
        numUrls = urls.size();
        final ArrayList<File> aacFiles = downloadUrls(urls);
        final File[] outputFiles = new File[1];
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    outputFiles[0] = mergeWithFFMpeg(aacFiles);
                } catch (MergeWithFFMpegException e) {
                    System.err.println("We got a boo-boo!!! :( " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, 3000);

        File outputFile = outputFiles[0];

    }

    private ArrayList<File> downloadUrls(ArrayList<String> urls) {
        final ArrayList<File> outputFiles = new ArrayList<>();
        for (String urlString : urls) {

            AsyncHttpClient dlSnip = new AsyncHttpClient();
            dlSnip.get(
                urlString,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        File outputFile = null;
                        try {
                            outputFile = File.createTempFile("soundFile", ".aac", outputDir);
                            FileOutputStream output = new FileOutputStream(outputFile);
                            output.write(responseBody);
                            outputFiles.add(outputFile);
                            counter++;

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        System.err.println("I'm so sad now: " + statusCode);
                    }
                }
            );
        }
        return outputFiles;
    }

    private File mergeWithFFMpeg(ArrayList<File> aacFiles) throws MergeWithFFMpegException {
        installFFMpeg();

        try {
            File listOfFiles = File.createTempFile("inputFile", ".txt", outputDir);
            FileOutputStream listOfFileOut = new FileOutputStream(listOfFiles);
            for (File aacFile : aacFiles) {
                listOfFileOut.write(aacFile.getAbsolutePath().getBytes());
            }
            File outputAacFile = File.createTempFile("merged", ".aac", outputDir);
            String command = ffmpegBinary + " -f concat -i " + listOfFiles.getAbsolutePath() + " -c copy " + outputAacFile.getAbsolutePath();

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(outputDir);

            Process proc = pb.start();

            int exitVal = proc.waitFor();

            System.out.println("Exit val is: " + String.valueOf(exitVal));

            return outputAacFile;
        } catch (IOException e) {
            System.err.println("I'm sad: " + e.getMessage());
            e.printStackTrace();
            throw(new MergeWithFFMpegException());
        } catch (InterruptedException e) {
            System.err.println("I'm super sad: " + e.getMessage());
            e.printStackTrace();
            throw(new MergeWithFFMpegException());
        }
    }

    private void installFFMpeg() {
        Context ctx = this.getApplicationContext();
        try {
            File f = new File(ctx.getDir("bin", 0), "ffmpeg");

            final FileOutputStream out = new FileOutputStream(f);
            final InputStream is = ctx.getResources().openRawResource(R.raw.ffmpeg);
            byte buf[] = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            ffmpegBinary = f.getAbsolutePath();
            Runtime.getRuntime().exec("chmod 755 "+ffmpegBinary).waitFor();
        } catch (FileNotFoundException e) {
            System.err.println("OH MY GOD NOOOOO, WE CAN'T FIND THE FILE.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("OH MY GOD NOOOOO, WE Have an IOException.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("OH MY GOD NOOOOO, WE are interrupted.");
            e.printStackTrace();
        }
    }

    private class MergeWithFFMpegException extends Throwable {
    }
}