package ggomdol.autodatabase.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ggomdol.autodatabase.entity.EntityOne;
import ggomdol.autodatabase.entity.EntityTwo;

public class TableCreator {
	private static String createTableString(Class <?> classTemplate) {

		String strMakeQuery = "create table ";
		strMakeQuery += classTemplate.getSimpleName() + " (";
		strMakeQuery += "ROWID INTEGER PRIMARY KEY AUTOINCREMENT, ";

		Field fieldArray[] = classTemplate.getDeclaredFields();

		for(int field = 0, maxfield = fieldArray.length; field < maxfield; field++) {

			if( (fieldArray[field].getType() == int.class ||fieldArray[field].getType() == long.class || fieldArray[field].getType() == String.class) && field < maxfield-1 && field != 0) {
				strMakeQuery += ", ";
			}

			if(fieldArray[field].getType() == int.class ||fieldArray[field].getType() == long.class ) {
				strMakeQuery += fieldArray[field].getName() + " ";
				strMakeQuery += "INTEGER";
			} else if(fieldArray[field].getType() == String.class) {
				strMakeQuery += fieldArray[field].getName() + " ";
				strMakeQuery +="TEXT";
			}

			if(field == maxfield -1) {
				strMakeQuery += " ) ";
			}
		}

		return strMakeQuery;
	}

	public static List<String> getCreateTableDDL() {

		ArrayList<String> strDllArrayList = new ArrayList<String>();
		strDllArrayList.add(createTableString(EntityOne.class));
		strDllArrayList.add(createTableString(EntityTwo.class));

		return strDllArrayList;		
	}
	
}
