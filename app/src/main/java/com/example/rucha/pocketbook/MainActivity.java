package com.example.rucha.pocketbook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton fabPlus, fabText, fabGallery, fabCamera;
    Animation fabOpen, fabClose, fabRClockwise, fabRAnticlockwise;
    LinearLayout lCamera, lGallery, lText;
    TextView headerEmail;
    //boolean variable indicating whether the FAB is open or closed
    boolean isOpen = false;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    static final int PICK_IMAGE_FROM_GALLERY = 100;

    DBHelper database;
    RecyclerView recyclerView;
    ReceiptCardAdapter receiptCardAdapter;
    List<ReceiptCard> receiptCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //for recycler view
        recyclerView = (RecyclerView) findViewById(R.id.rv_receipts);
        receiptCards = new ArrayList<ReceiptCard>();
        database = new DBHelper(this);
        int id = database.getIdFromEmail(SaveSharedPreferences.getEmail(getApplicationContext()));
        receiptCards = database.getAllReceiptCards(id);
        receiptCards = reverse(receiptCards);
        receiptCardAdapter = new ReceiptCardAdapter(MainActivity.this, receiptCards);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(receiptCardAdapter);
        recyclerView.setVisibility(View.VISIBLE);

        //instantiating the three FABs
        fabPlus = (FloatingActionButton) findViewById(R.id.fab_plus);
        fabGallery = (FloatingActionButton) findViewById(R.id.fab_gallery);
        fabCamera = (FloatingActionButton) findViewById(R.id.fab_camera);
        fabText = (FloatingActionButton) findViewById(R.id.fab_text_only);
        //animations instantiation
        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabRClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clockwise);
        fabRAnticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlockwise);

        lCamera = (LinearLayout) findViewById(R.id.ll_camera);
        lGallery = (LinearLayout) findViewById(R.id.ll_gallery);
        lText = (LinearLayout) findViewById(R.id.ll_text_only);

        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if FAB plus is open, on next click close other FABs and rotate the FAB anticlockwise, disable clicking of other 2 FABs
                if (isOpen) {
                    lCamera.startAnimation(fabClose);
                    lGallery.startAnimation(fabClose);
                    lText.startAnimation(fabClose);
                    fabPlus.startAnimation(fabRAnticlockwise);
                    fabGallery.setClickable(false);
                    fabCamera.setClickable(false);
                    fabText.setClickable(false);
                    isOpen = false;
                }
                //if FAB is closed, on next click open other FABs and rotate the FAB clockwise, set the 2 FABs as clickable.
                else {
                    lCamera.startAnimation(fabOpen);
                    lGallery.startAnimation(fabOpen);
                    lText.startAnimation(fabOpen);
                    fabPlus.startAnimation(fabRClockwise);
                    fabGallery.setClickable(true);
                    fabCamera.setClickable(true);
                    fabText.setClickable(true);
                    isOpen = true;
                }
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                // .setAction("Action", null).show();
            }
        });

        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        fabText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(MainActivity.this, TextOnlyReceipt.class);
                startActivity(i1);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        headerEmail = headerView.findViewById(R.id.tv_email);
        headerEmail.setText(SaveSharedPreferences.getEmail(getApplicationContext()));
    }

    //open camera for taking receipt picture
    public void dispatchTakePictureIntent() {
        //if permission is not granted ask for permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        }
        //intent for capturing image to camera
        else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    //open gallery for importing an image
    public void openGallery() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_FROM_GALLERY);
        } else {
            /*Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent,SELECT_FILE);
            //startActivityForResult(galleryIntent, SELECT_FILE);*/
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE_FROM_GALLERY);
        }
    }

    //method to do the action after camera and gallery intents return results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                //inserting image to gallery
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, "image", null);
                //converting bitmap to uri so that large images can also be sent in the intent
                Uri imageUri = Uri.parse(path);
                //String pathNew = imageUri.getPath();
                String pathNew = null;
                try {
                    pathNew = getFilePath(this, imageUri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                Intent addReceiptIntent = new Intent(this, AddReceipt.class);
                addReceiptIntent.putExtra("Receipt Image", imageUri);
                addReceiptIntent.putExtra("Image path", pathNew);
                startActivity(addReceiptIntent);
            }
            if (requestCode == PICK_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && data != null) {
                Toast.makeText(this, "result phase", Toast.LENGTH_LONG);
                Uri imageUri = data.getData();
                //String path = getRealPathFromURI(imageUri);
               // String path = imageUri.getPath();
                String path = null;
                try {
                    path = getFilePath(this,imageUri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                Intent addReceiptIntent = new Intent(this, AddReceipt.class);
                addReceiptIntent.putExtra("Receipt Image", imageUri);
                addReceiptIntent.putExtra("Image path", path);
                startActivity(addReceiptIntent);
            }
        }
    }

    /**
     * method for getting image path of selected image from gallery
     */
    public String getRealPathFromURI(Uri uri) {
//        String[] projection = { MediaStore.Images.Media.DATA };
//        @SuppressWarnings("deprecation")
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        int column_index = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
        String result;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            SaveSharedPreferences.clear(getApplicationContext());
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        // Handle navigation view item clicks here.
        switch(item.getItemId())
        {
            case R.id.nav_receipts:
                intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
//            case R.id.nav_subscriptions:
//                intent = new Intent(MainActivity.this, SubscriptionsActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.nav_graphs:
//                intent = new Intent(MainActivity.this, GraphsActivity.class);
//                startActivity(intent);
//                finish();
//                break;
            case R.id.nav_statistics:
                intent = new Intent(MainActivity.this, StatisticsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.logout:
                SaveSharedPreferences.clear(getApplicationContext());
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public List<ReceiptCard> reverse(List<ReceiptCard> list) {
        for(int i = 0, j = list.size() - 1; i < j; i++) {
            list.add(i, list.remove(j));
        }
        return list;
    }
}