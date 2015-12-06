package db;

import java.sql.*;
import java.util.List;

import model.Model;
import model.Note;

public class DBManager {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/spotagram";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "root";
	private Connection connection;
	private static DBManager instance;

	private DBManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}

	public DBManager open() {
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER,
					DB_PASSWORD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return this;
	}

	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean save(Model entity) {
		try {
			Statement statement = connection.createStatement();
			String savesql = ModelUtils.getInsertSQL(entity);
			statement.executeUpdate(savesql, Statement.RETURN_GENERATED_KEYS);
			ResultSet resultSet = statement.getGeneratedKeys();
			while (resultSet.next()) {
				entity.setId(resultSet.getInt(1));
			}
			close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			close();
			return false;
		}
		
	}

	public boolean update(Model entity) {
		try {
			Statement statement = connection.createStatement();
			String updatesql = ModelUtils.getUpdateSQL(entity);
			statement.execute(updatesql);
			close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			close();
			return false;
		}
		
	}

	public boolean delete(Model entity) {
		try {
			Statement statement = connection.createStatement();
			String updatesql = ModelUtils.getDeleteSQL(entity);
			statement.execute(updatesql);
			close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			close();
			return false;
		}
		
		//
	}

	public Model findById(Integer id, Class<? extends Model> cls) {
		List<Model> searchResult = findByFieldName(cls, "id", id);
		if (searchResult != null && searchResult.size() > 0) {
			return searchResult.get(0);
		} else {
			return null;
		}

	}

	public List<Model> findByFieldName(Class<? extends Model> cls,
			String fieldName, Object value) {
		// List<Model> result = new ArrayList<Model>();
		try {
			Statement statement = connection.createStatement();
			String updatesql = ModelUtils.getSelectSQL(cls, fieldName, value);
			ResultSet resultSet = statement.executeQuery(updatesql);
			List<Model> results = ModelUtils.ResultSetToList(resultSet, cls);
			close();

			return results;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			close();
		}
		
		return null;
	}
	
	public List<Model> findAll(Class<? extends Model> cls) {
		// List<Model> result = new ArrayList<Model>();
		try {
			Statement statement = connection.createStatement();
			String updatesql = ModelUtils.getSelectSQL(cls);
			ResultSet resultSet = statement.executeQuery(updatesql);
			List<Model> results = ModelUtils.ResultSetToList(resultSet, cls);
			close();

			return results;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			close();
		}
		
		return null;
	}


	public List<Model> findByFieldName(Class<? extends Model> cls,
			String fieldName1, Object value, String fieldName2, Object fValue,
			Object tValue) {
		// TODO Auto-generated method stub
		try {
			Statement statement = connection.createStatement();
			String updatesql = ModelUtils.getSelectSQL(cls, fieldName1, value, fieldName2, fValue, tValue);
			ResultSet resultSet = statement.executeQuery(updatesql);
			List<Model> results = ModelUtils.ResultSetToList(resultSet, cls);
			close();

			return results;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			close();
		}
		return null;
	}

	public List<Model> findNoteByRange(float longitude, float latitude,
			int radius_km) {
		// TODO Auto-generated method stub
		try {
			Statement statement = connection.createStatement();
			String updatesql = ModelUtils.getNoteRangeSQL(Note.class, longitude, latitude, radius_km);
			ResultSet resultSet = statement.executeQuery(updatesql);
			List<Model> results = ModelUtils.ResultSetToList(resultSet, Note.class);
			close();

			return results;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			close();
		}
		return null;
	
	}

}
