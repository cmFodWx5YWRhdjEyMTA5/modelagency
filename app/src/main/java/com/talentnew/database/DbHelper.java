package com.talentnew.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.talentnew.models.InfoItem;
import com.talentnew.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shweta on 6/9/2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = Constants.APP_NAME+".db";
    public static final String ALBUM = "Album";
    public static final String INFO_ITEM = "INFO_ITEM";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String IMAGE_URL = "imageUrl";
    public static final String LABEL = "label";
    public static final String SHOW_LABEL = "showLabel";
    public static final String VALUE = "value";
    public static final String TYPE = "type";
    public static final String UPDATED_AT = "updatedAt";
    public static final String CREATED_AT = "createdAt";
    private Context context;

    public static final String CREATE_ALBUM_TABLE = "create table "+ALBUM +
            "("+NAME+" TEXT NOT NULL, " +
            " "+IMAGE_URL+" TEXT NOT NULL, " +
            " "+CREATED_AT+" TEXT, " +
            " "+UPDATED_AT+" TEXT)";

    public static final String CREATE_INFO_ITEM_TABLE = "create table "+INFO_ITEM +
            "("+LABEL+" TEXT NOT NULL, " +
            " "+SHOW_LABEL+" TEXT NOT NULL, " +
            " "+VALUE+" TEXT NOT NULL, " +
            " "+TYPE+" INTEGER)";

    public DbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 3);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ALBUM_TABLE);
        db.execSQL(CREATE_INFO_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS "+ALBUM);
        //onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS "+INFO_ITEM);
        db.execSQL(CREATE_INFO_ITEM_TABLE);
    }

    public boolean addInfoItem(InfoItem item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LABEL, item.getLabel());
        contentValues.put(SHOW_LABEL, item.getShowLabel());
        contentValues.put(VALUE, item.getValue());
        contentValues.put(TYPE, item.getType());
        db.insert(INFO_ITEM, null, contentValues);
        return true;
    }

    public List<InfoItem> getAllInfoItem(int type){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+INFO_ITEM+" where "+TYPE+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{String.valueOf(type)});
        InfoItem item = null;
        ArrayList<InfoItem> itemList=new ArrayList<>();
        if(res.moveToFirst()){
            do{
                item = new InfoItem();
                item.setLabel(res.getString(res.getColumnIndex(LABEL)));
                item.setShowLabel(res.getString(res.getColumnIndex(SHOW_LABEL)));
                item.setValue(res.getString(res.getColumnIndex(VALUE)));
                item.setType(res.getInt(res.getColumnIndex(TYPE)));
                itemList.add(item);
            }while (res.moveToNext());
        }

        return itemList;
    }

    public List<InfoItem> getMyInfoItem(int type){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+INFO_ITEM+" where "+TYPE+" = ? AND "+VALUE+" != '-'";
        Cursor res =  db.rawQuery(query, new String[]{String.valueOf(type)});
        InfoItem item = null;
        ArrayList<InfoItem> itemList=new ArrayList<>();
        if(res.moveToFirst()){
            do{
                item = new InfoItem();
                item.setLabel(res.getString(res.getColumnIndex(LABEL)));
                item.setShowLabel(res.getString(res.getColumnIndex(SHOW_LABEL)));
                item.setValue(res.getString(res.getColumnIndex(VALUE)));
                item.setType(res.getInt(res.getColumnIndex(TYPE)));
                itemList.add(item);
            }while (res.moveToNext());
        }

        return itemList;
    }

    public boolean updateInfoItem(String label,String value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(VALUE,value);
        db.update(INFO_ITEM, contentValues,LABEL+" =?", new String[]{label});
        db.close();
        return true;
    }

  /*  public boolean insertBookingData(String level0Category,String level1Category,String level2Category,String receiptNumber,
                                  String date,String comment,String photo,int taxRate,String creditCardFee,double amount,
                                     double bookToAccountDebit,double bookToAccountCredit,String version)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LEVEL_0_CATEGORY, level0Category);
        contentValues.put(LEVEL_1_CATEGORY, level1Category);
        contentValues.put(LEVEL_2_CATEGORY, level2Category);
        contentValues.put(RECEIPT_NUMBER, receiptNumber);
        contentValues.put(DATE, date);
        contentValues.put(COMMENT, comment);
        contentValues.put(PHOTO, photo);
        contentValues.put(TAX_RATE, taxRate);
        contentValues.put(CREDIT_CARD_FEE, creditCardFee);
        contentValues.put(AMOUNT, amount);
        contentValues.put(BOOK_TO_ACCOUNT_DEBIT, bookToAccountDebit);
        contentValues.put(BOOK_TO_ACCOUNT_CREDIT, bookToAccountCredit);
        contentValues.put(VERSION, version);
     //   contentValues.put(AMOUNT_CREDIT, amountCredit);

     //   contentValues.put(AMOUNT_DEBIT, amountDebit);

        db.insert("Booking", null, contentValues);
        return true;
    }

    public ArrayList<Booking> getAllBookings(String month){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from Booking where strftime('%m',_date)=?";
        Cursor res =  db.rawQuery(query, new String[]{month});
       // res.moveToFirst();
        ArrayList<Booking> bookingList=new ArrayList<>();
        if(res.moveToFirst()){
            do{
                Booking booking=new Booking(res.getString(res.getColumnIndex(LEVEL_0_CATEGORY)),
                        res.getString(res.getColumnIndex(LEVEL_1_CATEGORY)),res.getString(res.getColumnIndex(LEVEL_2_CATEGORY)),
                        res.getString(res.getColumnIndex(RECEIPT_NUMBER)), res.getString(res.getColumnIndex(DATE)),
                        res.getString(res.getColumnIndex(COMMENT)),res.getString(res.getColumnIndex(PHOTO)),
                        res.getInt(res.getColumnIndex(TAX_RATE)),res.getString(res.getColumnIndex(CREDIT_CARD_FEE)),
                        res.getDouble(res.getColumnIndex(AMOUNT)),res.getInt(res.getColumnIndex(BOOK_TO_ACCOUNT_DEBIT)),
                        res.getInt(res.getColumnIndex(BOOK_TO_ACCOUNT_CREDIT)),res.getString(res.getColumnIndex(VERSION)));
                bookingList.add(booking);
            }while (res.moveToNext());
        }


        return bookingList;
    }

     public boolean updateSessionGuestCheckedOutServerStatus(String id,String eventId,String eventCatId,String status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHECKED_OUT_SERVER_SYNC_STATUS,status);
        contentValues.put(UPDATED_AT, Utility.getTimeStamp());
        db.update(SESSION_GUEST_TABLE, contentValues,ID+" =? and "+EVENT_ID+" =? and "+EVENT_CATEGORY_ID+" =?",
                new String[]{id,eventId,eventCatId});
        db.close();
        return true;
    }

    public boolean deleteSessionGuestTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SESSION_GUEST_TABLE, null, null);
        return true;
    }*/

    public boolean deleteTable(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, null, null);
        return true;
    }

    public boolean deleteAllTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ALBUM, null, null);
        db.delete(INFO_ITEM, null, null);
        return true;
    }

    public boolean dropAndCreateBookingTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+ALBUM);
        db.execSQL("DROP TABLE IF EXISTS "+INFO_ITEM);
        onCreate(db);
        return true;
    }


}
