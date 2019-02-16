package com.example.rucha.pocketbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    static final private String DATABASE_NAME = "receipts.db";
    static final private int DATABASE_VERSION = 1;
    static final private String LOG = "DBHelper";

    //User Table
    private static final String TABLE_USER = "user";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    //Category Table
    static final String TABLE_CATEGORIES = "Categories";
    static final String COLUMN_CATEGORY_ID = "category_id";
    static final String COLUMN_CATEGORY = "category_name";

    //Payment Methods Table
    static final String TABLE_PAYMENT_METHODS = "PaymentMethods";
    static final String COLUMN_PAYMENT_METHOD_ID = "payment_method_id";
    static final String COLUMN_PAYMENT_METHOD = "payment_method";

    //Receipts Table
    static final private String TABLE_RECEIPTS = "Receipts";
    static final private String COLUMN_ID = "receipt_id";
    static final private String COLUMN_NAME = "name";
    static final private String COLUMN_PRICE = "price";
    static final private String COLUMN_DATE = "date";
    static final private String COLUMN_YEAR = "year";
    static final private String COLUMN_MONTH = "month";
    static final private String COLUMN_STORE = "store";
    static final private String COLUMN_COMMENT = "comment";
    static final private String COLUMN_RECEIPT_IMAGE = "image";
    static final private String COLUMN_USER_ID_FK = "user_id";
    static final private String COLUMN_RECEIPT_TYPE = "receipt_type";
    static final private String COLUMN_CATEGORY_ID_FK = COLUMN_CATEGORY_ID;
    static final private String COLUMN_PAYMENT_METHOD_ID_FK = COLUMN_PAYMENT_METHOD_ID;

    Context ctx;

    //queries
    // create user table sql query
    private String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    //create receipts table sql query
    static final private String CREATE_TABLE_RECEIPTS = "CREATE TABLE "+ TABLE_RECEIPTS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT DEFAULT \"New Receipt\","
            + COLUMN_PRICE + " DECIMAL DEFAULT 0.00,"
            + COLUMN_DATE + " DATE DEFAULT (DATE('now', 'localtime')),"
            + COLUMN_MONTH + " INTEGER,"
            + COLUMN_YEAR + " INTEGER,"
            + COLUMN_STORE + " TEXT,"
            + COLUMN_COMMENT + " TEXT,"
            + COLUMN_RECEIPT_IMAGE + " TEXT,"
            + COLUMN_RECEIPT_TYPE + " TEXT,"
            + COLUMN_USER_ID_FK + " INTEGER REFERENCES "+ TABLE_USER + " ON DELETE NO ACTION,"
            + COLUMN_CATEGORY_ID_FK + " INTEGER REFERENCES " + TABLE_CATEGORIES + " ON DELETE NO ACTION,"
            + COLUMN_PAYMENT_METHOD_ID_FK + " INTEGER REFERENCES " + TABLE_PAYMENT_METHODS + " ON DELETE NO ACTION"
            + ");";

    //create categories table sql query
    static final private String CREATE_TABLE_CATEGORIES = "CREATE TABLE "+ TABLE_CATEGORIES + "("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CATEGORY + " TEXT"
            + ");";

    //create PaymentMethods table sql query
    static final private String CREATE_TABLE_PAYMENT_METHODS = "CREATE TABLE "+ TABLE_PAYMENT_METHODS + "("
            + COLUMN_PAYMENT_METHOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_PAYMENT_METHOD + " TEXT"
            + ");";

    public DBHelper(Context ct)
    {
        super(ct,DATABASE_NAME,null,DATABASE_VERSION);
        ctx=ct;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(CREATE_TABLE_CATEGORIES);
        sqLiteDatabase.execSQL(CREATE_TABLE_PAYMENT_METHODS);
        sqLiteDatabase.execSQL(CREATE_TABLE_RECEIPTS);

        //category defaults
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORIES + "(" + COLUMN_CATEGORY + ") values('" + ctx.getResources().getString(R.string.accessories) + "');" );
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORIES + "(" + COLUMN_CATEGORY + ") values('" + ctx.getResources().getString(R.string.beauty) + "');" );
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORIES + "(" + COLUMN_CATEGORY + ") values('" + ctx.getResources().getString(R.string.education) + "');" );
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORIES + "(" + COLUMN_CATEGORY + ") values('" + ctx.getResources().getString(R.string.electronics) + "');" );
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORIES + "(" + COLUMN_CATEGORY + ") values('" + ctx.getResources().getString(R.string.entertainment) + "');" );
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORIES + "(" + COLUMN_CATEGORY + ") values('" + ctx.getResources().getString(R.string.footwear) + "');" );
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORIES + "(" + COLUMN_CATEGORY + ") values('" + ctx.getResources().getString(R.string.home) + "');" );
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORIES + "(" + COLUMN_CATEGORY + ") values('" + ctx.getResources().getString(R.string.garments) + "');" );
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORIES + "(" + COLUMN_CATEGORY + ") values('" + ctx.getResources().getString(R.string.grocery) + "');" );
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORIES + "(" + COLUMN_CATEGORY + ") values('" + ctx.getResources().getString(R.string.health) + "');" );
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORIES + "(" + COLUMN_CATEGORY + ") values('" + ctx.getResources().getString(R.string.hotel) + "');" );
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORIES + "(" + COLUMN_CATEGORY + ") values('" + ctx.getResources().getString(R.string.others) + "');" );
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORIES + "(" + COLUMN_CATEGORY + ") values('" + ctx.getResources().getString(R.string.stationery) + "');" );
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORIES + "(" + COLUMN_CATEGORY + ") values('" + ctx.getResources().getString(R.string.subscriptions) + "');" );
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORIES + "(" + COLUMN_CATEGORY + ") values('" + ctx.getResources().getString(R.string.travel) + "');" );
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORIES + "(" + COLUMN_CATEGORY + ") values('" + ctx.getResources().getString(R.string.trip) + "');" );

        //payment methods defaults
        sqLiteDatabase.execSQL("insert into "+ TABLE_PAYMENT_METHODS + "(" + COLUMN_PAYMENT_METHOD + ") values('" + ctx.getResources().getString(R.string.cash) + "');" );
        sqLiteDatabase.execSQL("insert into "+ TABLE_PAYMENT_METHODS + "(" + COLUMN_PAYMENT_METHOD + ") values('" + ctx.getResources().getString(R.string.credit) + "');" );
        sqLiteDatabase.execSQL("insert into "+ TABLE_PAYMENT_METHODS + "(" + COLUMN_PAYMENT_METHOD + ") values('" + ctx.getResources().getString(R.string.debit) + "');" );
        sqLiteDatabase.execSQL("insert into "+ TABLE_PAYMENT_METHODS + "(" + COLUMN_PAYMENT_METHOD + ") values('" + ctx.getResources().getString(R.string.e_wallets) + "');" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop table if exists "+ TABLE_USER);
        sqLiteDatabase.execSQL("Drop table if exists "+ TABLE_PAYMENT_METHODS);
        sqLiteDatabase.execSQL("Drop table if exists "+ TABLE_CATEGORIES);
        sqLiteDatabase.execSQL("Drop table if exists "+ TABLE_RECEIPTS);
        //create tables again
        onCreate(sqLiteDatabase);
    }


    /** USER TABLE OPERATIONS **/

    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    /**
     * This method is to fetch all users and return the list of user records
     *
     * @return list
     */
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check whether user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /** RECEIPTS TABLE OPERATIONS **/
    /**
     *  Creating a receipt
     */
    public long createReceipt(Receipts receipt){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, receipt.getReceiptName());
        contentValues.put(COLUMN_PRICE, receipt.getPrice());
        contentValues.put(COLUMN_STORE, receipt.getReceiptStore());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(receipt.getReceiptDate());
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_MONTH, receipt.getMonth());
        contentValues.put(COLUMN_YEAR, receipt.getYear());
        contentValues.put(COLUMN_COMMENT, receipt.getComment());
        contentValues.put(COLUMN_RECEIPT_IMAGE, receipt.getRImage());
        contentValues.put(COLUMN_USER_ID_FK, receipt.getUserID());
        contentValues.put(COLUMN_CATEGORY_ID_FK, receipt.getCategoryID());
        contentValues.put(COLUMN_RECEIPT_TYPE, receipt.getReceiptType());
        contentValues.put(COLUMN_PAYMENT_METHOD_ID_FK, receipt.getPaymentMethodID());

        //insert row
        long receipt_id = db.insert(TABLE_RECEIPTS, null, contentValues);

        return receipt_id;
    }

    /**
     * Get single Receipt
     */
    public Receipts getReceipt(int receipt_id){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_RECEIPTS + " where " + COLUMN_ID + " = " + receipt_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if(c!=null)
           c.moveToFirst();
        Receipts receipt = new Receipts();
        receipt.setID(c.getInt(c.getColumnIndex(COLUMN_ID)));
        receipt.setReceiptName(c.getString(c.getColumnIndex(COLUMN_NAME)));
        receipt.setPrice(c.getFloat(c.getColumnIndex(COLUMN_PRICE)));
        receipt.setReceiptStore(c.getString(c.getColumnIndex(COLUMN_STORE)));
        receipt.setReceiptDate(new Date(c.getString(c.getColumnIndex(COLUMN_DATE))));
        receipt.setRImage(c.getString(c.getColumnIndex(COLUMN_RECEIPT_IMAGE)));
        receipt.setComment(c.getString(c.getColumnIndex(COLUMN_COMMENT)));
        receipt.setUserID(c.getInt(c.getColumnIndex(COLUMN_USER_ID_FK)));
        receipt.setCategoryID(c.getInt(c.getColumnIndex(COLUMN_CATEGORY_ID_FK)));
        receipt.setPaymentMethodID(c.getInt(c.getColumnIndex(COLUMN_PAYMENT_METHOD_ID_FK)));
        return receipt;
    }

    /**
     *  Get all receipts
     */
    public List<Receipts> getAllReceipts(){
        List<Receipts> receipts = new ArrayList<Receipts>();
        String selectQuery = "SELECT * FROM " + TABLE_RECEIPTS;
        Log.e(LOG,selectQuery);
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c!=null && c.moveToFirst()){
            do{
                Receipts receipt = new Receipts();
                receipt.setID(c.getInt(c.getColumnIndex(COLUMN_ID)));
                receipt.setReceiptName(c.getString(c.getColumnIndex(COLUMN_NAME)));
                receipt.setPrice(c.getFloat(c.getColumnIndex(COLUMN_PRICE)));
                receipt.setReceiptStore(c.getString(c.getColumnIndex(COLUMN_STORE)));
                receipt.setReceiptDate(new Date(c.getString(c.getColumnIndex(COLUMN_DATE))));
                receipt.setRImage(c.getString(c.getColumnIndex(COLUMN_RECEIPT_IMAGE)));
                receipt.setComment(c.getString(c.getColumnIndex(COLUMN_COMMENT)));
                receipt.setUserID(c.getInt(c.getColumnIndex(COLUMN_USER_ID_FK)));
                receipt.setCategoryID(c.getInt(c.getColumnIndex(COLUMN_CATEGORY_ID_FK)));
                receipt.setPaymentMethodID(c.getInt(c.getColumnIndex(COLUMN_PAYMENT_METHOD_ID_FK)));
                receipts.add(receipt);
            } while(c.moveToNext());
        }
        return receipts;
    }

    /**
     *  Get all receipts for recycler view
     */
    public List<ReceiptCard> getAllReceiptCards( int user_id){
        List<ReceiptCard> receipts = new ArrayList<ReceiptCard>();
        String selectQuery = "SELECT * FROM " + TABLE_RECEIPTS + " WHERE "+COLUMN_USER_ID_FK+" ="+user_id+";";
        Log.e(LOG,selectQuery);
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c!=null && c.moveToFirst()){
            do{
                ReceiptCard receiptCard = new ReceiptCard();
                receiptCard.setReceiptCardId(c.getInt(c.getColumnIndex(COLUMN_ID)));
                receiptCard.setReceiptCardTitle(c.getString(c.getColumnIndex(COLUMN_NAME)));
                receiptCard.setReceiptCardPrice(c.getFloat(c.getColumnIndex(COLUMN_PRICE)));
                receiptCard.setReceiptCardStore(c.getString(c.getColumnIndex(COLUMN_STORE)));
                receiptCard.setReceiptCardType(c.getString(c.getColumnIndex(COLUMN_RECEIPT_TYPE)));
                //fetching date and parsing it
                String dt = c.getString(c.getColumnIndex(COLUMN_DATE));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                try {
                    date =  dateFormat.parse(dt);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                receiptCard.setReceiptCardDate(date);
                receiptCard.setReceiptCardComment(c.getString(c.getColumnIndex(COLUMN_COMMENT)));
                //getting category from category id
                int categoryId = c.getInt(c.getColumnIndex(COLUMN_CATEGORY_ID_FK));
                String category = getCategoryNameFromID(categoryId);
                receiptCard.setReceiptCardCategory(category);
                //getting payment method from payment method id
                int paymentMethodId = c.getInt(c.getColumnIndex(COLUMN_PAYMENT_METHOD_ID_FK));
                String paymentMethod = getPaymentMethodFromID(paymentMethodId);
                receiptCard.setReceiptCardPaymentMethod(paymentMethod);
                //getting image from database as BLOB and converting to bitmap

//                byte[] blob = c.getBlob(c.getColumnIndex(COLUMN_RECEIPT_IMAGE));
//                ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
//                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                receiptCard.setReceiptCardImage(bitmap);

                String imagepath = c.getString(c.getColumnIndex(COLUMN_RECEIPT_IMAGE));
//                File image = new File(imagepath);
//                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                Bitmap bmp = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);

//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                Bitmap bmp = BitmapFactory.decodeFile(imagepath, options);
                String type = c.getString(c.getColumnIndex(COLUMN_RECEIPT_TYPE));
                if(type.equalsIgnoreCase("WITH PICTURE")) {
                    Bitmap bmp = BitmapFactory.decodeFile(imagepath);
                    receiptCard.setReceiptCardImage(bmp);
                }
                receiptCard.setReceiptCardImagePath(imagepath);


                receiptCard.setReceiptCardUserID(c.getInt(c.getColumnIndex(COLUMN_USER_ID_FK)));
                receipts.add(receiptCard);
            }while(c.moveToNext());
        }
        return receipts;
    }
    /**
     * Updating a receipt
     */
    public int updateReceipt(Receipts receipt){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, receipt.getReceiptName());
        contentValues.put(COLUMN_PRICE, receipt.getPrice());
        contentValues.put(COLUMN_STORE, receipt.getReceiptStore());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(receipt.getReceiptDate());
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_COMMENT, receipt.getComment());
        contentValues.put(COLUMN_RECEIPT_IMAGE, receipt.getRImage());
        contentValues.put(COLUMN_RECEIPT_TYPE, receipt.getReceiptType());
        contentValues.put(COLUMN_USER_ID_FK, receipt.getUserID());
        contentValues.put(COLUMN_CATEGORY_ID_FK, receipt.getCategoryID());
        contentValues.put(COLUMN_PAYMENT_METHOD_ID_FK, receipt.getPaymentMethodID());
        //updating row
        return db.update(TABLE_RECEIPTS, contentValues,COLUMN_ID + "=?", new String[] { String.valueOf(receipt.getID())});
    }


//    /**
//     * Deleting a Receipt
//     */
//    public void deleteReceipt(Receipts receipt){
//        SQLiteDatabase db = getWritableDatabase();
//        db.delete(TABLE_RECEIPTS, COLUMN_ID + "=?", new String[]{String.valueOf(receipt.getID())});
//    }

    /**
     * Deleting a Receipt
     */
    public void deleteReceipt(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM "+ TABLE_RECEIPTS + " WHERE "+ COLUMN_ID + " = " + id + ";";
        db.execSQL(query);
    }

    /**
     * getting category id from category name
     */
    public int getCategoryIDFromName(String category){
        int id = 0;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT "+ COLUMN_CATEGORY_ID + " FROM " + TABLE_CATEGORIES + " WHERE " + COLUMN_CATEGORY + " = '" + category + "';";
        Cursor c = db.rawQuery(query, null);
        if(c!=null && c.moveToFirst()) {
            Log.d("spinn","is not null");
            id = c.getInt(c.getColumnIndex(COLUMN_CATEGORY_ID));
            c.close();
        }
        return id;
    }

    /**
     * getting category name from category id
     */
    public String getCategoryNameFromID(int categoryID){
        String category="";
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT "+ COLUMN_CATEGORY + " FROM " + TABLE_CATEGORIES + " WHERE " + COLUMN_CATEGORY_ID + " = " + categoryID + ";";
        Cursor c = db.rawQuery(query, null);
        if(c!=null && c.moveToFirst()) {
            Log.d("spinn","is not null");
            category = c.getString(c.getColumnIndex(COLUMN_CATEGORY));
            c.close();
        }
        return category;
    }

    /**
     * getting payment method id from payment method
     */
    public int getPaymentMethodIDFromMethod(String paymentMethod){
        int id = 0;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT "+ COLUMN_PAYMENT_METHOD_ID + " FROM " + TABLE_PAYMENT_METHODS + " WHERE " + COLUMN_PAYMENT_METHOD + " = '" + paymentMethod + "';";
        Cursor c = db.rawQuery(query,null);
        if(c!=null && c.moveToFirst()){
            id = c.getInt(c.getColumnIndex(COLUMN_PAYMENT_METHOD_ID));
            c.close();
        }
        return id;
    }

    /**
     * getting payment method name from payment method id
     */
    public String getPaymentMethodFromID(int paymentID){
        String method ="";
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT "+ COLUMN_PAYMENT_METHOD + " FROM " + TABLE_PAYMENT_METHODS + " WHERE " + COLUMN_PAYMENT_METHOD_ID + " = " + paymentID + ";";
        Cursor c = db.rawQuery(query, null);
        if(c!=null && c.moveToFirst()) {
            Log.d("spinn","is not null");
            method = c.getString(c.getColumnIndex(COLUMN_PAYMENT_METHOD));
            c.close();
        }
        return method;
    }

    /**
     * getting user id from email
     */
    public int getUserIDFromEmail(String email)
    {
        int id = 0;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT "+ COLUMN_USER_ID + " FROM " + TABLE_USER + " WHERE " + COLUMN_USER_EMAIL + " = '" + email + "';";
        Cursor c = db.rawQuery(query, null);
        if(c!=null && c.moveToFirst()){
            id = c.getInt(c.getColumnIndex(COLUMN_USER_ID));
            c.close();
        }
        return id;
    }

    /**
     * CLOSING DATABASE
     */
    public void closeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!= null && db.isOpen()){
            db.close();
        }
    }
    public int getIdFromEmail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+COLUMN_USER_ID+" FROM "+TABLE_USER+" WHERE "+COLUMN_USER_EMAIL+" = '"+email+"';";
        Cursor c = db.rawQuery(query, null);
        int user_id = 0;
        if (c != null && c.moveToFirst()){
            //c.moveToNext();
            user_id = c.getInt(c.getColumnIndex(COLUMN_USER_ID));
        }
        return user_id;
    }
}
