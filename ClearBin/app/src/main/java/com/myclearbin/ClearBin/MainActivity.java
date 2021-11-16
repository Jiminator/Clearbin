package com.myclearbin.ClearBin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseLocalModel;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int IMAGE_CAPTURE_CODE = 1001;

    Button mCaptureBtn;
    ImageView mImageView;
    ArrayList<TextView> classification = new ArrayList<>();
    ArrayList<ProgressBar> progressBars = new ArrayList<>();
    int[] index;

    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.imageview);
        mCaptureBtn = findViewById(R.id.button_image);
        classification.add((TextView)findViewById(R.id.textView6));
        classification.add((TextView)findViewById(R.id.textView7));
        classification.add((TextView)findViewById(R.id.textView8));
        classification.add((TextView)findViewById(R.id.textView9));
        classification.add((TextView)findViewById(R.id.textView10));
        progressBars.add((ProgressBar)findViewById(R.id.progressBar1));
        progressBars.add((ProgressBar)findViewById(R.id.progressBar2));
        progressBars.add((ProgressBar)findViewById(R.id.progressBar3));
        progressBars.add((ProgressBar)findViewById(R.id.progressBar4));
        progressBars.add((ProgressBar)findViewById(R.id.progressBar5));

        FirebaseLocalModel localModel = new FirebaseLocalModel.Builder("my_local_model")
                .setAssetFilePath("FBAutoML/manifest.json")
                .build();
        FirebaseModelManager.getInstance().registerLocalModel(localModel);


        try {
            ItemList = toItemList(FileToJSONArray("items.txt"));
            System.out.println(ItemList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        for (Item item:ItemList) {
//            System.out.println(item);
//        }

//        InputStream is = getResources().openRawResource(R.raw.bottle);
//        Bitmap bm = BitmapFactory.decodeStream(is);

//        System.out.println("hi");
//        for (confLabel conf:confidenceSort(labelList)) {
//            System.out.println(conf);
//        }
//        System.out.println("bye");
//        System.out.println("-----------------------------------");
//        DownloadTask task= new DownloadTask(this);
//        task.execute(getMaterials());
    }

    public void mCaptureBtnOnClick (View view){
        int PERMISSION_ALL = 1;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            String[] PERMISSIONS = {
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
            };
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public void onClick(View view){
        setText(index[Integer.parseInt(view.getTag().toString())]);
    }

    public void setText(int i){
        Item item = ItemList.get(i);
        TextView title = findViewById(R.id.textView3);
        TextView info = findViewById(R.id.textView4);
        title.setText(item.name.replaceAll("_" ," "));
        info.setText(item.special);
    }

    public void getPolicy(View view){
        Intent broswerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.iubenda.com/privacy-policy/16316243"));
        startActivity(broswerIntent);
    }
    public static class confLabel implements Comparable<confLabel>{
        FirebaseVisionImageLabel label;
        double confidence;
        int cnt;

        public confLabel(FirebaseVisionImageLabel label, double confidence, int cnt){
            this.label = label;
            this.confidence = confidence;
            this.cnt = cnt;
        }
        @Override
        public int compareTo(confLabel conf) {
            return Double.compare(conf.confidence,this.confidence);
        }

        @Override
        public String toString() {
            return "Orig: "+label.getText()+"| New: "+ItemList.get(cnt).name+"| Confidence: "+confidence;
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, Boolean> {

        public DownloadTask(Activity activity) {
            dialog = new ProgressDialog(activity);
        }

        /** progress dialog to show user that the backup is processing. */
        private ProgressDialog dialog;
        /** application context. */
        protected void onPreExecute() {
            this.dialog.setMessage("Progress start");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }


            if (success) {
                Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        }

        protected Boolean doInBackground(final String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Connection failed...", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(hasPermissions(this,permissions)){
            openCamera();
        } else {
            Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            Bitmap bitmap= null;
            // add check api 29 later
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),image_uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mImageView.setImageBitmap(bitmap);
            FirebaseVisionOnDeviceAutoMLImageLabelerOptions labelerOptions =
                    new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder()
                            .setLocalModelName("my_local_model")
                            .setConfidenceThreshold(0)
                            .build();
            FirebaseVisionImageLabeler labeler = null;
            try {
                labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(labelerOptions);
                System.out.println("noice");
            } catch (FirebaseMLException e) {
                e.printStackTrace();
            }
            final ArrayList<FirebaseVisionImageLabel> labelList = new ArrayList<>();
            FirebaseVisionImage image= null;
            try {
                 image = FirebaseVisionImage.fromBitmap(handleSamplingAndRotationBitmap(this, image_uri));
            } catch (Exception e){
            }
            labeler.processImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                            if (labels.size()>0) {
                                System.out.println("hi");
                                String stg = "";

                                double sum = 0;
                                for (int j = 0; j < 5 && j<labels.size(); j++) { sum+= labels.get(j).getConfidence();}

                                index = new int[5];
                                for (int j = 0; j<5; j++){
                                    int i;
                                    for (i = 0; i < ItemList.size() && !ItemList.get(i).name.equals(labels.get(j).getText()); i++)
                                    index[j]=i+1;
                                }

                                for (int j = 0; j < 5 && j<labels.size(); j++) {
                                    classification.get(j).setText(" "+ ItemList.get(index[j]).name.replaceAll("_"," "));
                                    progressBars.get(j).setProgress((int)(labels.get(j).getConfidence()*100/sum));
                                }
                                setText(index[0]);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                            // ...
                        }
                    });
        }
    }

    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            final float totalPixels = width * height;

            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }


    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public static ArrayList<confLabel> confidenceSort(ArrayList<FirebaseVisionImageLabel> labelList){
        ArrayList<confLabel> confLabels = new ArrayList<>();
        for (FirebaseVisionImageLabel label:labelList) {
            int cnt = semanticMatch(label.getText());
            confLabels.add( new confLabel(label,averageSemantic(ItemList.get(cnt).name,label.getText()),cnt) );
        }
        Collections.sort(confLabels);
        return confLabels;
    }

    public static int semanticMatch(String s){
        int cnt=0;
        double conf=-1;
        for (int i = 0; i < ItemList.size(); i++) {
            String name = ItemList.get(i).name;
            double nConf = averageSemantic(name,s);
            if (nConf>conf){
                conf = nConf;
                cnt=i;
            }
        }
        return cnt;
    }

    public static double averageSemantic(String s1, String s2){
        return cosine.similarity(s1,s2);
    }
    private static JaroWinkler jaroWinkler = new JaroWinkler();
    private static Cosine cosine = new Cosine();
    public static ArrayList<Item> ItemList = new ArrayList<>();
    public static String baseUrl = "http://api.earth911.com/earth911.";
    public static File categoryFile = new File("/Users/5apphire/Desktop/Files/fireBase/app/src/main/java/com/example/firebase/items.txt");


//    public static String getMaterials(){
//        return baseUrl+"getMaterials"+keyString;
//    } //array
//
//    public static String getFamilies(){
//        return baseUrl+"getFamilies"+keyString;
//    } // array
//
//    public static String getLocationDetails(String id){
//        return baseUrl+"getLocationDetails"+keyString+"&location_id="+id;
//    }
//
//    public static String getLocationRestrictions(String id){
//        return baseUrl+"getLocationRestrictions"+keyString+"&location_id="+id;
//    }
//
//    public static String getLocationTypes(){
//        return baseUrl+"getLocationTypes"+keyString;
//    } //array
//
//    public static String getProgramDetails(String id){
//        return baseUrl+"getProgramDetails"+keyString+"&program_id="+id;
//    }
//
//    public static String getProgramRestrictions(String id){
//        return baseUrl+"getProgramRestrictions"+keyString+"&program_id="+id;
//    }
//
//    public static String getProgramTypes(){ //array
//        return baseUrl+"getProgramTypes"+keyString;
//    }

//    public String searchMaterials(int max_results, String query){ //array
//        return baseUrl+"searchMaterials"+keyString+"&max_results="+max_results+"&query="+query;
//    }

//    public static String searchListings(boolean business, boolean residential, boolean hideEvent, double latitude, double longitude, int material_id, int max_distance, int max_results){//array
//        return baseUrl+"searchListings"+keyString+"&business_only="+business+"&residential_only="+residential+"&hide_event_only="+hideEvent+"&latitude="+latitude+"&longitude="+longitude+"&material_id="+material_id+"&max_distance="+max_distance+"&max_results="+max_results;
//    }

//    -------------------------------------------------------------------

    public static void JSONArraytoFile(JSONArray List, File file){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            JSONObject items = new JSONObject();

            items.put("result",List);

            writer.write(items.toString());
            writer.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public  JSONArray FileToJSONArray(String s){
        try {
            InputStream is = getAssets().open(s);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer);
            System.out.println(text);
            return new JSONObject(text).getJSONArray("result");
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<Category> toCategoryList(JSONArray families){
        ArrayList<Category> categoryList = new ArrayList<>();

        try {
            for (int i = 0; i < families.length(); i++) {
                JSONObject item = families.getJSONObject(i);
                if (item.get("description").toString().equals("Reuse")) {
                    break;
                } else if (!item.get("description").toString().equals("All Materials")) {
                    categoryList.add(new Category(
                            item.get("description").toString(),
                            Integer.parseInt(item.get("family_id").toString()),
                            null,
                            null,
                            null,
                            null,
                            0
                    ));
//               String name, int id, String description, double[] stats, String reduce, String reuse, long searches) {
                } else {

                }

            }
            return categoryList;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<Item> toItemList(JSONArray materials) throws Exception {
        ArrayList<Item> itemList = new ArrayList<>();

        for (int i = 0; i < materials.length(); i++) {
            JSONObject item = materials.getJSONObject(i);
            itemList.add(new Item(
                    item.get("Name").toString(),
                    Integer.parseInt(item.get("Id").toString()),
                    -1,
                    item.get("Special").toString(),
                    true,
                    0,
                    item.get("Src").toString(),
                    item.get("Query").toString()));
        }

        return itemList;
    }

    public  JSONArray toItemJSONArray(ArrayList<Item> List){
        JSONArray result = new JSONArray();
        try {
            for (Item item : List) {
                result.put(item.toJSONObject());
            }

            return result;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public  JSONArray toCategoryJSONArray(ArrayList<Category> List){
        JSONArray result = new JSONArray();

        for (Category category: List) {
            result.put(category.toJSONObject());
        }

        return result;
    }

}
