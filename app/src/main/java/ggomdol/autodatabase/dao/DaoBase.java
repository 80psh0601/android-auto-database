package ggomdol.autodatabase.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import ggomdol.autodatabase.util.Helper;

public class DaoBase {

	private static DaoBase sTheInstance = null;
	protected static Class<?> mEntityClass;
	protected DaoBase() {}
	
	public static synchronized DaoBase getInstance(Class<?> entityClass) {
		
		mEntityClass = entityClass;
		
		if(sTheInstance == null) {
			sTheInstance = new DaoBase();
		}
		
		return sTheInstance;
	}
	
	public void deleteAllData(Context context) throws SQLiteException {
		
		String sql = "delete from " + mEntityClass.getSimpleName();
		Helper.getInstance(context).getWritableDatabase().execSQL(sql, new String[0]);
		Helper.getInstance(context).close();
	}	

	public void deleteData(Context context, long rowID) {

		String sql ="delete from " + mEntityClass.getSimpleName() + " where ROWID = " + rowID;
		Helper.getInstance(context).getWritableDatabase().execSQL(sql);
		Helper.getInstance(context).close();
	}		
	
	public void deleteDataFromWhere(Context context, String colName, String value) {

		String sql ="delete from " + mEntityClass.getSimpleName() + " where "+ colName +" = " +value;
		Helper.getInstance(context).getWritableDatabase().execSQL(sql);
		Helper.getInstance(context).close();
	}
	
	public void deleteDataWithWhereCondition(Context context, String whereCondition) {

		String sql ="delete from " + mEntityClass.getSimpleName() + " " +whereCondition;
		Helper.getInstance(context).getWritableDatabase().execSQL(sql);
		Helper.getInstance(context).close();
	}
	
	public long insertEntity(Context context, Object entity) throws Exception {
		
		ContentValues values = new ContentValues();
		
		Field fieldArray[] = entity.getClass().getDeclaredFields();
		for(int count = 0, maxCount = fieldArray.length; count < maxCount; count++) {
			if(fieldArray[count].getName().equalsIgnoreCase("ROWID")) {
				continue;
			} else if(fieldArray[count].getType() == int.class) {
				values.put(fieldArray[count].getName(), (Integer)fieldArray[count].get(entity));
			} else if(fieldArray[count].getType() == long.class) {
				values.put(fieldArray[count].getName(), (Long)fieldArray[count].get(entity));
			} else if(fieldArray[count].getType() == String.class) {
				values.put(fieldArray[count].getName(), (String)fieldArray[count].get(entity));
			}
		}
		
		long extraID = Helper.getInstance(context).getWritableDatabase().insert(entity.getClass().getSimpleName(), null, values);
		Helper.getInstance(context).close();
		return extraID;
	}

	public ArrayList<Object> getEntityListAsc(Context context) throws SQLiteException {
		
		String sql = "select * from " + mEntityClass.getSimpleName() + " order by ROWID asc";
		return selectEntityList(context, sql);
	}
	
	public ArrayList<Object> getEntityList(Context context) throws SQLiteException {
		
		String sql = "select * from " + mEntityClass.getSimpleName() + " order by ROWID desc";
		return selectEntityList(context, sql);
	}
	
	public int getRecordCount(Context context) throws SQLiteException {
		
		String sql = "select count(ROWID) as RecordCount from " + mEntityClass.getSimpleName();
		return selectRecordCount(context, sql);
	} 
	
	protected static int selectRecordCount(Context context, String sql) throws SQLiteException {
		
		Cursor cursor = null;
		
		try{
			cursor = Helper.getInstance(context).getWritableDatabase().rawQuery(sql, null);
			int count  = 0;
			
			if(cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				int colurmIdx = cursor.getColumnIndex("RecordCount");
				count = cursor.getInt(colurmIdx);
			}
			
			return count;
			
		} catch(Exception e) 	{
			throw new SQLiteException(e.getMessage());
		} finally {
			if(cursor != null) {
				cursor.deactivate();
				cursor.close();
			}
		}
	}
	
	protected ArrayList<Object> selectEntityList(Context context, String sql) throws SQLiteException {
		
		ArrayList<Object> rowList = new ArrayList<Object>();
		Cursor cursor = null;
		
		try {
			cursor = Helper.getInstance(context).getWritableDatabase().rawQuery(sql, null);

			if(cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				Object object = null;
				
				while(!cursor.isAfterLast()) {
					object = setEntity(cursor);
					rowList.add(object);
					cursor.moveToNext();
				}
			} 
		} catch(Exception e) 	{
			throw new SQLiteException(e.getMessage());
		} finally {
			if(cursor != null) {
				cursor.deactivate();
				cursor.close();
				Helper.getInstance(context).close();
			}
		}

		return rowList;
	}
	
	protected Object setEntity(Cursor cursor) throws Exception {
		
	    Object entity = mEntityClass.newInstance();
		Field fieldArray[] = mEntityClass.getFields();
		
		for(int index = 0, maxIndex = fieldArray.length; index < maxIndex; index++) {
			int colIndex = cursor.getColumnIndex(fieldArray[index].getName());
			
			if(colIndex >= 0){
				if(fieldArray[index].getType() == int.class) {
					fieldArray[index].set(entity, cursor.getInt(colIndex));
				} else if(fieldArray[index].getType() == long.class) {
					fieldArray[index].setAccessible(true);
					fieldArray[index].set(entity, cursor.getLong(colIndex));
				} else if(fieldArray[index].getType() == String.class) {
					fieldArray[index].set(entity, cursor.getString(colIndex));
				}
			}
		}
		
		return entity;
	}
}
