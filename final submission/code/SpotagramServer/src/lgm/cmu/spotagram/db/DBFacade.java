package lgm.cmu.spotagram.db;

import java.util.List;

import lgm.cmu.spotagram.model.Model;

public class DBFacade {
	public static boolean save(Model entity) {
		return DBManager.getInstance().open().save(entity);
	}

	public static boolean update(Model entity) {
		return DBManager.getInstance().open().update(entity);
	}

	public static boolean delete(Model entity) {
		return DBManager.getInstance().open().delete(entity);
	}

	public static Model findById(Integer id, Class<? extends Model> cls) {
		return DBManager.getInstance().open().findById(id, cls);
	}

	public static List<Model> findByFieldName(Class<? extends Model> cls,
			String fieldName, Object value) {
		return DBManager.getInstance().open()
				.findByFieldName(cls, fieldName, value);
	}
	
	public static List<Model> findNoteByRange(float longitude, float latitude, int radius_km) {
		return DBManager.getInstance().open()
				.findNoteByRange(longitude, latitude, radius_km);
	}
	
	public static List<Model> findAll(Class<? extends Model> cls) {
		return DBManager.getInstance().open()
				.findAll(cls);
	}

}
