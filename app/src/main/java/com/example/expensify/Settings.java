package com.example.expensify;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class Settings extends AppCompatActivity {
    DrawerLayout dl;
    Button btnbackup, btnrestore, btnexport, aboutapp;
    ArrayList<String> transactionid, dates, amount, typeoftransaction, spinner, note, paymentmode;
    ArrayList<Integer> image;
    private static final int OPEN_DOCUMENT_REQUEST_CODE = 0x43;
    private static final int STORAGE_REQUEST_CODE_EXPORT = 1;
    private static final int STORAGE_REQUEST_CODE_IMPORT = 2;
    private String[] storage_permission;
    private Context context;
    ParcelFileDescriptor inputPFD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        dl = findViewById(R.id.drawer_layout);
        transactionid = new ArrayList<>();
        dates = new ArrayList<>();
        amount = new ArrayList<>();
        typeoftransaction = new ArrayList<>();
        spinner = new ArrayList<>();
        note = new ArrayList<>();
        paymentmode = new ArrayList<>();
        image = new ArrayList<>();

        storage_permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        btnbackup = findViewById(R.id.btnbackup);
        btnrestore = findViewById(R.id.btnrestore);
        btnexport = findViewById(R.id.btnexport);
        aboutapp = findViewById(R.id.btnabout);
        btnbackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        btnrestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        btnexport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        aboutapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Settings.this, AboutApp.class);
                startActivity(intent);
            }
        });
    }

    public void ClickMenu(View v) {
        MainActivity.openDrawer(dl);
    }

    public void ClickLogo(View v) {
        MainActivity.closeDrawer(dl);
    }

    public void Settings(View v) {
        recreate();
    }

    public void Home(View v) {
        MainActivity.redirectActivity(this, MainActivity.class);
    }

    public void AllTransactions(View v) { MainActivity.redirectActivity(this, AllTransactions.class);    }

    public void DayView(View v) {
        MainActivity.redirectActivity(this, DayView.class);
    }

    public void MonthView(View v) {
        MainActivity.redirectActivity(this, MonthView.class);
    }

    public void CustomView(View v) {
        MainActivity.redirectActivity(this, CustomView.class);
    }

    public void Budget(View v) {
        MainActivity.redirectActivity(this, Budget.class);
    }

    public void Category(View v) {
        MainActivity.redirectActivity(this, Category.class);
    }

    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(dl);
    }

    private boolean checkstoragepermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStorageExport() {
        ActivityCompat.requestPermissions(this, storage_permission, STORAGE_REQUEST_CODE_EXPORT);
    }

    private void requestStorageImport() {
        ActivityCompat.requestPermissions(this, storage_permission, STORAGE_REQUEST_CODE_IMPORT);
    }

    private void exportCSV() {
        try {
            dbHelper db = new dbHelper(this);
            Cursor cursor = db.GetTransaction();
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No data present! Enter Atleast 1 Transaction. ", Toast.LENGTH_LONG).show();
            } else {
                final String inFileName = "/data/data/com.example.expensify/databases/expensify_db.db";
                File dbFile = new File(inFileName);
                if (!dbFile.exists()) {
                    Toast.makeText(this, "No data present! Enter Atleast 1 Transaction. ", Toast.LENGTH_SHORT).show();
                } else {
                    FileInputStream fis = new FileInputStream(dbFile);
                    File folder = new File(String.valueOf(getExternalFilesDir("ExpensifyBackup")));
                    boolean isFolderCreated = false;
                    if (!folder.exists()) {
                        isFolderCreated = folder.mkdir();
                    }
                    Log.d("CSC___TAG", "export CSV" + isFolderCreated);
                    String csvFileName = "expensify_db.db";
                    String filepathandName = folder.toString() + "/" + csvFileName;
                    OutputStream output = new FileOutputStream(filepathandName);
                    // Transfer bytes from the inputfile to the outputfile
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        output.write(buffer, 0, length);
                    }
                    output.flush();
                    output.close();
                    fis.close();
                    Toast.makeText(this, "Backup exported to :" + filepathandName, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Backed up successfully", Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse(filepathandName);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    sendIntent.setType("text/csv");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCSV() {
        try {
            File Folder = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Expensify_data");
            Log.d("d", "1");
            boolean isFolderCreated = true;

            Log.d("d", "3");
            if (isFolderCreated) {
                isFolderCreated = Folder.mkdir();
                Log.d("d", "4");
            }
            Log.d("d", "5");
            String csvFileName = "expensify.csv";
            String filePathAndName = Folder.toString() + "/" + csvFileName;
            dbHelper helper = new dbHelper(this);
            Cursor cursor = helper.GetTransaction();
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    transactionid.add(cursor.getString(0));
                    dates.add(cursor.getString(1));
                    amount.add(cursor.getString(2));
                    typeoftransaction.add(cursor.getString(3));
                    spinner.add(cursor.getString(4));
                    note.add(cursor.getString(5));
                    paymentmode.add(cursor.getString(6));
                    image.add(cursor.getInt(7));
                }
                FileWriter file = new FileWriter(filePathAndName);
                for (int i = 0; i < transactionid.size(); i++) {
                    file.append("" + transactionid.get(i));
                    file.append(",");
                    file.append("" + dates.get(i));
                    file.append(",");
                    file.append("" + amount.get(i));
                    file.append(",");
                    file.append("" + typeoftransaction.get(i));
                    file.append(",");
                    file.append("" + spinner.get(i));
                    file.append(",");
                    file.append("" + note.get(i));
                    file.append(",");
                    file.append("" + paymentmode.get(i));
                    file.append(",");
                    file.append("" + image.get(i));
                    file.append("\n");
                }
                file.flush();
                file.close();
                Toast.makeText(Settings.this, "Exporting data to SpreadSheet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "" + data.getData().toString(), Toast.LENGTH_SHORT).show();
        if (resultCode != RESULT_OK) {
            return;
        } else {
            Uri returnUri = data.getData();
            try {
                inputPFD = getContentResolver().openFileDescriptor(returnUri, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("MainActivity", "File not found.");
                return;
            }
            FileDescriptor fd = inputPFD.getFileDescriptor();
            importCSV(fd);
        }
}

//    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
//            new ActivityResultCallback<Uri>() {
//                @Override
//                public void onActivityResult(Uri uri) {
//                    // Handle the returned Uri
//                    Toast.makeText(Settings.this, "Hello"+uri.toString(), Toast.LENGTH_SHORT).show();
//                    importCSV(uri.da);
//                }
//            });
    private void importCSV(FileDescriptor url) {
        try {
            final String outFileName = "/data/data/com.example.expensify/databases/expensify_db.db";
            File dbFile = new File(outFileName);
                FileInputStream fileInputStream = new FileInputStream(url);
                OutputStream outputStream = new FileOutputStream(dbFile);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fileInputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                outputStream.close();
                fileInputStream.close();
                Toast.makeText(Settings.this, "Restored Successfully", Toast.LENGTH_SHORT).show();

                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
        } catch (Exception e) {
            Toast.makeText(this, "You Haven't Taken the backup of your data", Toast.LENGTH_LONG).show();
            Log.d("datatatatatattatatatata",e.getMessage());
            e.printStackTrace();
        }
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);
        LinearLayout backup = dialog.findViewById(R.id.layout1);
        LinearLayout backup2 = dialog.findViewById(R.id.layout2);
        LinearLayout backup3 = dialog.findViewById(R.id.layout3);
        if(btnbackup.isPressed()) {
            backup3.setVisibility(View.GONE);
            backup2.setVisibility(View.GONE);
        } else if(btnrestore.isPressed()) {
            backup.setVisibility(View.GONE);
            backup3.setVisibility(View.GONE);
        } else {
            backup.setVisibility(View.GONE);
            backup2.setVisibility(View.GONE);
        }
        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                if (checkstoragepermission()){
//
//                } else {
//                    requestStorageExport();
//                }
                exportCSV();
            }
        });

        backup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                if (checkstoragepermission()){
//                } else {
//                    requestStorageImport();
//                }
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("application/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 0);
            }
        });

        backup3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fetchCSV();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case STORAGE_REQUEST_CODE_EXPORT:{
                if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    exportCSV();
                } else {
                    Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
                }
            }
            break;
//            case STORAGE_REQUEST_CODE_IMPORT:{
//                if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    importCSV();
//                } else {
//                    Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
//                }
//            }
//            break;
        }
    }
}
