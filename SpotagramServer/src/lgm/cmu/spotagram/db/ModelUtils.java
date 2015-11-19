package lgm.cmu.spotagram.db;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lgm.cmu.spotagram.db.Column.DataType;
import lgm.cmu.spotagram.model.Model;
import lgm.cmu.spotagram.model.Note;

import org.omg.PortableServer.ID_ASSIGNMENT_POLICY_ID;

public class ModelUtils {

	public static String getCreateTableSQL(Class<? extends Model> modelClass) {
		Model m = null;
		try {
			m = modelClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		StringBuilder sb = new StringBuilder(100);
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(m.tableName()).append("(").append(m.idColumnName())
				.append(" INTEGER PRIMARY KEY AUTOINCREMENT");
		for (Field f : m.getClass().getDeclaredFields()) {
			if (f.isAnnotationPresent(Column.class)) {
				Column c = f.getAnnotation(Column.class);
				if (!c.pk()) {
					sb.append(",").append(c.name()).append(" ")
							.append(c.type());
				}
			}
		}
		sb.append(");");
		return sb.toString();
	}

	public static String getDropTableSQL(Class<? extends Model> modelClass) {
		Model m = null;
		try {
			m = modelClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		StringBuilder sb = new StringBuilder(35);
		sb.append("DROP TABLE IF EXISTS ").append(m.tableName()).append(";");
		return sb.toString();
	}

	public static List<Model> ResultSetToList(ResultSet resultSet,
			Class<? extends Model> cls) {
		if (resultSet == null) {
			return null;
		}
		List<Model> results = new ArrayList<Model>();
		try {
			while (resultSet.next()) {
				Model entity = null;
				entity = cls.newInstance();
				entity.setFieldsByResultSet(resultSet);
				results.add(entity);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return results;
	}

	public static String getInsertSQL(Model entity) {
		Map<String, String> keyValueMap = entity.toKeyValueMap();
		String savequery = "INSERT into " + entity.tableName();
		String field = "(";
		String value = " values(";
		for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
			field += entry.getKey() + ",";
			value += entry.getValue() + ",";
		}

		return savequery + field.substring(0, field.length() - 1) + ")"
				+ value.substring(0, value.length() - 1) + ")";
	}

	public static String getUpdateSQL(Model entity) {
		Map<String, String> keyValueMap = entity.toKeyValueMap();
		String updatequery = "UPDATE " + entity.tableName() + " SET ";
		for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
			updatequery += entry.getKey() + "=" + entry.getValue() + ", ";
		}

		return updatequery.substring(0, updatequery.length() - 2)
				+ " WHERE _id = " + entity.getId();
	}

	public static String getDeleteSQL(Model entity) {

		return "DELETE FROM " + entity.tableName() + " WHERE "
				+ entity.idColumnName() + " = " + entity.getId();
	}

	public static String getSelectSQL(Class<? extends Model> cls,
			String fieldName, Object value) {
		Model entity;
		String args = value.toString();
		try {
			entity = cls.newInstance();
			String where = entity.columnName(fieldName) + " = ";
			if (!fieldName.equals("id")) {
				DataType type = cls.getDeclaredField(fieldName)
						.getAnnotation(Column.class).type();
				if (type == DataType.TEXT) {
					args = "'" + value + "'";
				}
			}
			return "SELECT * FROM " + entity.tableName() + " WHERE " + where
					+ args;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String getSelectSQL(Class<? extends Model> cls) {
		Model entity;
		try {
			entity = cls.newInstance();
			return "SELECT * FROM " + entity.tableName();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@SuppressWarnings("deprecation")
	public static Date string2Date(String dateString) {
		String[] items = dateString.split("/");
		int year = Integer.parseInt(items[2]);
		int month = Integer.parseInt(items[0]);
		int day = Integer.parseInt(items[1]);

		return new Date(year - 1900, month - 1, day);
	}

	public static String getSelectSQL(Class<? extends Model> cls,
			String fieldName1, Object value, String fieldName2, Object fValue,
			Object tValue) {
		// TODO Auto-generated method stub
		Model entity;
		String arg1 = value.toString();
		String fArg2 = fValue.toString();
		String tArg2 = tValue.toString();
		try {
			entity = cls.newInstance();
			String columnName1 = entity.columnName(fieldName1);
			String columnName2 = entity.columnName(fieldName2);
			DataType type = cls.getDeclaredField(fieldName1)
					.getAnnotation(Column.class).type();
			if (type == DataType.TEXT) {
				arg1 = "'" + value + "'";
			}
			type = cls.getDeclaredField(fieldName2).getAnnotation(Column.class)
					.type();
			if (type == DataType.TEXT || type == DataType.DATE || type == DataType.DATETIME) {
				fArg2 = "'" + fValue + "'";
				tArg2 = "'" + tValue + "'";
			}

			return "SELECT * FROM " + entity.tableName() + " WHERE "
					+ columnName1 + " = " + arg1 + " AND " + columnName2
					+ " >= " + fArg2 + " AND " + columnName2 + " <= " + tArg2;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getNoteRangeSQL(Class<Note> cls, float longitude,
			float latitude, int radius_km) {
		// TODO Auto-generated method stub

		return "SELECT *, ( 3959 * acos( cos( radians(" + latitude
				+ ") ) * cos( radians( note.latitude ) )"
				+ "* cos( radians(note.longitude) - radians(" + longitude
				+ ")) + sin(radians(" + latitude
				+ ")) * sin( radians(note.latitude)))) AS distance "
				+ "FROM note " + " HAVING distance < " + radius_km
				+ " ORDER BY distance";
	}
}
