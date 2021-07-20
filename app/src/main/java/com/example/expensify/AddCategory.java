package com.example.expensify;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AddCategory extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    FloatingActionButton fb1;
    EditText et1,et2;
    TextView textView;
    RadioButton rb;
    RadioGroup rg1;
    Spinner spn1;
    int catimage,amount,store;
    String id,catname,cattype,catnote;
    AddCategoryicon addCategoryicon;
    AddiconAdapter addiconAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        fb1 = findViewById(R.id.save);
        et1 = findViewById(R.id.edtcatname);
        et2 = findViewById(R.id.edtnote_ic);
        spn1 = findViewById(R.id.spinner_icon);

        Bundle bundle = getIntent().getExtras();
        if (bundle!= null) {
            boolean value = bundle.getBoolean("key");
            if (value)
            {
                textView = findViewById(R.id.tvcategory);
                textView.setText("Add Category");
                ArrayList<AddCategoryicon> customList = new ArrayList<>();
                customList.add(new AddCategoryicon(R.drawable.fast_food));
                customList.add(new AddCategoryicon(R.drawable.entertainment));
                customList.add(new AddCategoryicon(R.drawable.financial));
                customList.add(new AddCategoryicon(R.drawable.travel));
                customList.add(new AddCategoryicon(R.drawable.shopping_cart));
                customList.add(new AddCategoryicon(R.drawable.cloth));
                customList.add(new AddCategoryicon(R.drawable.wallet));
                customList.add(new AddCategoryicon(R.drawable.investment));
                customList.add(new AddCategoryicon(R.drawable.income));
                customList.add(new AddCategoryicon(R.drawable.ic_baseline_more));

                addiconAdapter = new AddiconAdapter(this,customList);
                if(spn1!=null)
                {
                    spn1.setAdapter(addiconAdapter);
                    spn1.setOnItemSelectedListener(this);
                }
                fb1.setOnClickListener(this);
            }
            else {
                textView = findViewById(R.id.tvcategory);
                textView.setText("Update Category");
                rg1 = findViewById(R.id.catgroup);
                int radioId = rg1.getCheckedRadioButtonId();
                rb = findViewById(radioId);

                if(getIntent().hasExtra("Id") && getIntent().hasExtra("Name") &&  getIntent().hasExtra("Type") && getIntent().hasExtra("Note") && getIntent().hasExtra("Image"))
                {
                    //Getting Data from Intent
                    id = getIntent().getStringExtra("Id");
                    catname = getIntent().getStringExtra("Name");
                    cattype = getIntent().getStringExtra("Type");
                    catnote = getIntent().getStringExtra("Note");
                    catimage = getIntent().getIntExtra("Image",0);
                    //Setting Intent Data
                    et1.setText(catname);
                    et2.setText(catnote);
                    if(cattype.equals("Expense"))
                    {
                        rg1.check(R.id.rbexpense);
                    }
                    else {
                        rg1.check(R.id.rdincome);
                    }
                    ArrayList<AddCategoryicon> customList = new ArrayList<>();
                    customList.add(new AddCategoryicon(R.drawable.fast_food));
                    customList.add(new AddCategoryicon(R.drawable.entertainment));
                    customList.add(new AddCategoryicon(R.drawable.financial));
                    customList.add(new AddCategoryicon(R.drawable.travel));
                    customList.add(new AddCategoryicon(R.drawable.shopping_cart));
                    customList.add(new AddCategoryicon(R.drawable.cloth));
                    customList.add(new AddCategoryicon(R.drawable.wallet));
                    customList.add(new AddCategoryicon(R.drawable.investment));
                    customList.add(new AddCategoryicon(R.drawable.income));
                    customList.add(new AddCategoryicon(R.drawable.ic_baseline_more));
                    addiconAdapter = new AddiconAdapter(this,customList);
                    for (int i = 0; i <customList.size() ; i++) {
                        amount = customList.get(i).getSpinner_icon();
                        if(amount == catimage)
                        {
                            store = i;
                        }
                    }
                    if(spn1!=null)
                    {
                        spn1.setAdapter(addiconAdapter);
                        spn1.setSelection(store);
                        spn1.setOnItemSelectedListener(this);
                    }
                }
                else {
                    Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
                fb1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(et1.getText().toString().isEmpty() || et2.getText().toString().isEmpty() || rg1.getCheckedRadioButtonId() == -1){
                               Toast.makeText(AddCategory.this, "You have not Entered proper data", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            dbHelper updateDB = new dbHelper(AddCategory.this);
                            String catname = et1.getText().toString();
                            String catnote = et2.getText().toString();
                            rg1 = findViewById(R.id.catgroup);
                            int radioId = rg1.getCheckedRadioButtonId();
                            rb = findViewById(radioId);
                            String cattype = rb.getText().toString();
                            long result = updateDB.updateData(id, catname, cattype, catnote, addCategoryicon.getSpinner_icon());

                            if (result == -1) {
                                Toast.makeText(AddCategory.this, "Update Failed!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddCategory.this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent();
                                i.setClass(AddCategory.this, Category.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    }
                });
            }
        }
        //End of OnCreate
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        addCategoryicon = (AddCategoryicon) parent.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    public void onClick(View v) {
        if(et1.getText().toString().isEmpty() || et2.getText().toString().isEmpty() ){
            Toast.makeText(this, "You have not Entered proper data", Toast.LENGTH_SHORT).show();
            et1.setText(null);
            et2.setText(null);
        }
        else {

            String cat = et1.getText().toString();
            String not = et2.getText().toString();

            rg1 = findViewById(R.id.catgroup);
            int radioId = rg1.getCheckedRadioButtonId();
            rb = findViewById(radioId);
            dbHelper db = new dbHelper(this);
            long result = db.AddCategory(cat, rb.getText().toString(), not, addCategoryicon.getSpinner_icon());
            if (result == -1) {
                Toast.makeText(AddCategory.this, "Add Failed!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddCategory.this, "Successfully Added!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent();
                i.setClass(AddCategory.this, Category.class);
                startActivity(i);
                finish();
            }
        }
    }
}