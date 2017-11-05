package com.hskj.jdccyxt.toolunit.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "cydbs.db";
	private static final int VERSION = 1;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Ω®±Ì”Ôæ‰÷¥––
		String sql = "create table photos(pid integer primary key autoincrement,jylsh varchar(64),pssj varchar(64),wjybh varchar(64),zpzl varchar(64),zpmc varchar(64),isupload varchar(64))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
}
