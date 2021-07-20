package com.example.expensify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class dbHelper extends SQLiteOpenHelper
{
    private static final String dbname = "expensify_db.db";

    public dbHelper(Context context) {
        super(context, dbname, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS addtransaction(transactionid INTEGER PRIMARY KEY AUTOINCREMENT,date DATE,amount INTEGER,typeoftransaction TEXT,categroryoftrans TEXT,note Text,paymentmode Text,CID INTEGER ,BID INTEGER,FOREIGN KEY (CID) REFERENCES addcategory(categoryid),FOREIGN KEY (BID) REFERENCES addbudget(budgetid))";
        String category_query = "CREATE TABLE IF NOT EXISTS addcategory(categoryid INTEGER PRIMARY KEY AUTOINCREMENT ,cname TEXT NOT NULL,type TEXT NOT NULL,note Text NOT NULL,image INTEGER NOT NULL)";
        String budget_query = "CREATE TABLE IF NOT EXISTS addbudget(budgetid INTEGER PRIMARY KEY AUTOINCREMENT,date DATE NOT NULL ,amount INTEGER NOT NULL ,total_amount INTEGER NOT NULL)";
        db.execSQL(query);
        db.execSQL(category_query);
        db.execSQL(budget_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String AddRecord(String date, int amount,String typeoftransaction, String categroryoftrans,String note,String paymentmode,int cid,int bid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("amount", amount);
        contentValues.put("typeoftransaction",typeoftransaction);
        contentValues.put("categroryoftrans",categroryoftrans);
        contentValues.put("note", note);
        contentValues.put("paymentmode", paymentmode);
        contentValues.put("CID",cid);
        contentValues.put("BID",bid);
        Long res = db.insert("addtransaction", null, contentValues);
        if (res == -1) { return "failed";}
        else { return "SuccessFully Inserted";
        }
    }

    public Long AddCategory(String cname, String type, String note, int image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cname", cname);
        contentValues.put("type", type);
        contentValues.put("note", note);
        contentValues.put("image",image);
        Long res = db.insert("addcategory", null, contentValues);
        return res;
    }
    public Boolean AddBudget(String date, int amount, int i) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("amount", amount);
        contentValues.put("total_amount", i);
        Long resbudget = db.insert("addbudget", null, contentValues);
        if (resbudget == -1) {return false;}
        else { return true; }
    }

    public Cursor GetWeekTransaction(String startingdate, String endingdate){
        SQLiteDatabase db = this.getWritableDatabase();
        String type = "Expense";
        //SELECT sum(amount),categroryoftrans,date FROM addtransaction WHERE typeoftransaction="Expense" AND (date <="2021/07/18" AND date >= "2021/06/25") GROUP BY categroryoftrans;
        Cursor cursor = db.rawQuery("select categroryoftrans,sum(amount) FROM addtransaction a WHERE (a.typeoftransaction='"+type+"') AND (a.date>='"+startingdate+"' AND a.date<='"+endingdate+"') GROUP BY categroryoftrans",null);
        return cursor;
    }

    public Cursor GetWeekIncome(String startingdate, String endingdate){
        SQLiteDatabase db = this.getWritableDatabase();
        String type = "Income";
        Cursor cursor = db.rawQuery("select categroryoftrans,amount FROM addtransaction a WHERE (a.typeoftransaction='"+type+"') AND (a.date>='"+startingdate+"' AND a.date<='"+endingdate+"') GROUP BY categroryoftrans",null);
        return cursor;
    }

    public Cursor GetCategory(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM addcategory",null);
        return cursor;
    }

    public Cursor GetTransaction(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select t.transactionid ,t.date,t.amount,t.typeoftransaction,t.categroryoftrans,t.note,t.paymentmode,c.image,t.bid  from addtransaction t,addcategory c where t.cid=c.categoryid ORDER BY transactionid DESC",null);
        return cursor;
    }

    public  Cursor GetDateByTransaction(String startdate, String endate){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select t.date,t.amount,c.image from addtransaction t,addcategory c " +
                "where (t.date>='"+startdate+"' AND t.date<='"+endate+"') AND t.cid=c.categoryid ORDER BY t.date DESC",null);
        return cursor;
    }

    public Cursor GetMonthTransaction(String startdate, String endate) {
        String type="Expense";
        SQLiteDatabase db = this.getWritableDatabase();
//      SELECT amount,transactionid,BID FROM addtransaction WHERE typeoftransaction="Expense" AND (date <="2021/07/10" AND date >= "2021/07/01");
        Cursor cursor = db.rawQuery("SELECT amount,transactionid,BID FROM addtransaction a WHERE a.typeoftransaction='"+type+"' AND (a.date>='"+startdate+"' AND a.date<='"+endate+"')",null);
        return cursor;
    }
    public Cursor GetDayView(String daydate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select t.transactionid,t.date,t.amount,t.typeoftransaction,t.categroryoftrans,t.note,t.paymentmode,c.image from addtransaction t,addcategory c where (t.cid=c.categoryid) AND (t.date = '"+daydate+"') ORDER BY transactionid DESC",null);
        return cursor;
    }
    public Cursor GetExpenseMonthView(String startingdate, String endingdate){
        SQLiteDatabase db = this.getWritableDatabase();
        String type = "Expense";
        //SELECT sum(amount),categroryup_idoftrans,date FROM addtransaction WHERE typeoftransaction="Expense" AND (date <="2021/07/18" AND date >= "2021/06/25") GROUP BY categroryoftrans;
        Cursor cursor = db.rawQuery("select categroryoftrans,sum(amount),c.image FROM addtransaction a,addcategory c WHERE c.categoryid = a.CID AND (a.typeoftransaction='"+type+"') AND (a.date>='"+startingdate+"' AND a.date<='"+endingdate+"') GROUP BY categroryoftrans",null);
        return cursor;
    }
    public Cursor GetIncomeMonthView(String startingdate, String endingdate){
        SQLiteDatabase db = this.getWritableDatabase();
        String type = "Income";
        //SELECT sum(amount),categroryoftrans,date FROM addtransaction WHERE typeoftransaction="Expense" AND (date <="2021/07/18" AND date >= "2021/06/25") GROUP BY categroryoftrans;
        Cursor cursor = db.rawQuery("select categroryoftrans,sum(amount),c.image FROM addtransaction a,addcategory c WHERE c.categoryid = a.CID AND (a.typeoftransaction='"+type+"') AND (a.date>='"+startingdate+"' AND a.date<='"+endingdate+"') GROUP BY categroryoftrans",null);
        return cursor;
    }

    long updateData(String row_id, String catname,String cattype, String catnote,int image)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("cname",catname);
        cv.put("type",cattype);
        cv.put("note",catnote);
        cv.put("image",image);
        long result = db.update("addcategory",cv,"categoryid="+row_id,null);
        return result;
    }

    long updateTrans(String row_id,String date, int amount,String typeoftransaction, String cattrans, String note,String paymentmode,int cid,int bid )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date",date);
        cv.put("amount",amount);
        cv.put("typeoftransaction",typeoftransaction);
        cv.put("categroryoftrans",cattrans);
        cv.put("note",note);
        cv.put("paymentmode",paymentmode);
        cv.put("CID",cid);
        cv.put("BID",bid);
        long result = db.update("addtransaction",cv,"transactionid="+row_id,null);
        return result;
    }

    long updatebudg_id(int i, int row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("BID" ,row_id);
        long result = db.update("addtransaction", cv ,"transactionid="+i ,null);
        return result;
    }

    public Cursor GetBudget(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM addbudget",null);
        return cursor;
    }

    public Cursor GetSelecteddata(String startingdt, String endingdate){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM addbudget WHERE (date<='"+endingdate+"') AND (date>='"+startingdt+"')",null);
        return cursor;
    }

    long UpdateBudget(int b_id,int amount,int amount1) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("total_amount",amount1);
        cv.put("amount",amount);
        long result = db.update("addbudget",cv,"budgetid="+b_id,null);
        return result;
    }

    long deductBudget(int row_id, int amount)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("amount",amount);
        long result = db.update("addbudget",cv,"budgetid="+row_id,null);
        return result;
    }
    public Cursor ExpenseSumAmount(){
        String expense = "Expense";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT (sum(amount)) FROM addtransaction WHERE typeoftransaction='"+expense+"' ",null);
        return cursor;
    }

    public Cursor IncomeSumAmount() {
        String expense = "Income";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT (sum(amount)) FROM addtransaction WHERE typeoftransaction='"+expense+"' ",null);
        return cursor;
    }

    public Cursor FetchCustomTransaction(String expenselist, String transactionlist, String date1, String date2) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select t.date,t.amount,c.image " +
                "from addtransaction t,addcategory c where t.categroryoftrans='"+expenselist+"' AND t.paymentmode='"+transactionlist+"' AND " +
                " (t.date>='"+date1+"' AND t.date<='"+date2+"') AND t.cid=c.categoryid ORDER BY transactionid DESC",null);
        return cursor;
    }

    //    SELECT a.date ,a.amount ,t.image, a.typeoftransaction from addtransaction a, addcategory t WHERE a.CID = t.categoryid AND (a.date>="2021/07/09" AND a.date<="2021/07/22");
    public Cursor FetchAllTransaction(String date1, String date2) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select t.date,t.amount,c.image" +
                " from addtransaction t,addcategory c where t.cid=c.categoryid AND " +
                " (t.date>='"+date1+"' AND t.date<='"+date2+"') ORDER BY t.transactionid DESC",null);
        return cursor;
    }

//    SELECT a.date ,a.amount ,t.image, a.typeoftransaction, a.paymentmode from addtransaction a, addcategory t WHERE a.CID = t.categoryid AND a.paymentmode="Cash" AND (a.date>="2021/07/09" AND a.date<="2021/07/22");

    public Cursor FetchOnlyPaymentTransaction(String mode ,String date1, String date2) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select t.date,t.amount,c.image" +
                " from addtransaction t,addcategory c where t.cid=c.categoryid AND " +
                " t.paymentmode='"+mode+"' AND (t.date>='"+date1+"' AND t.date<='"+date2+"') ORDER BY t.transactionid DESC",null);
        return cursor;
    }

    public Cursor FetchOnlyTransactionByPayment(String mode ,String date1, String date2) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select t.date,t.amount,c.image" +
                " from addtransaction t,addcategory c where t.cid=c.categoryid AND " +
                " t.categroryoftrans='"+mode+"' AND (t.date>='"+date1+"' AND t.date<='"+date2+"') ORDER BY t.transactionid DESC",null);
        return cursor;
    }

    long DeleteTransaction(long trans_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("addtransaction","transactionid=" +trans_id,null);
        return result;
    }

    public void AddCategoryAll(){
        int j = 1;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ArrayList<ShowCategoryItem> categoryItems = new ArrayList<>();
        categoryItems.add(new ShowCategoryItem("Food","Expense","Food",R.drawable.fast_food));
        categoryItems.add(new ShowCategoryItem("Entertainment","Expense","Entertainment",R.drawable.entertainment));
        categoryItems.add(new ShowCategoryItem("Interest","Expense","Interest",R.drawable.financial));
        categoryItems.add(new ShowCategoryItem("Travelling","Expense","Travelling",R.drawable.travel));
        categoryItems.add(new ShowCategoryItem("Shopping","Expense","Shopping",R.drawable.shopping_cart));
        categoryItems.add(new ShowCategoryItem("Cloth","Expense","Cloth",R.drawable.cloth));
        categoryItems.add(new ShowCategoryItem("Salary","Income","Salary",R.drawable.wallet));
        categoryItems.add(new ShowCategoryItem("SoldItems","Income","SoldItems",R.drawable.investment));
        categoryItems.add(new ShowCategoryItem("Coupons","Income","Coupons",R.drawable.income));
        categoryItems.add(new ShowCategoryItem("Others","Income","Other",R.drawable.ic_baseline_more));
        for (int i = 0; i < categoryItems.size() ; i++) {
            sqLiteDatabase.execSQL("insert into addcategory values("+j+",'"+categoryItems.get(i).CategoryName+"', '"+categoryItems.get(i).Categorytype+"' , '"+categoryItems.get(i).CatgeoryNote+"' , "+categoryItems.get(i).image+");");
            j+=1;
        }
    }
}
