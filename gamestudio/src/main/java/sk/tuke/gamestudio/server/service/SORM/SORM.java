package sk.tuke.gamestudio.server.service.SORM;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import sk.tuke.gamestudio.server.service.DatabaseSettings;
import sk.tuke.gamestudio.server.service.annotation.Column;
import sk.tuke.gamestudio.server.service.annotation.Table;

public class SORM {

	private void classTest(Class<?> clazz) throws SORMException {
		if (clazz.getAnnotation(Table.class) == null)
			throw new SORMException(clazz.getName() + " doesn't contain @Table annotation");

		int idCount = 0;
		for (Field field : clazz.getDeclaredFields()) {
			Column column = field.getAnnotation(Column.class);
			if (column != null && column.primaryKey()) {
				if (!field.getType().getSimpleName().equals("int")) {
					throw new SORMException("Wrong primary key");
				}
				idCount++;
			}
		}
		if (idCount != 1)
			throw new SORMException("No primary key assigned");
	}

	public String getCreateTableString(Class<?> clazz) throws SORMException {
		classTest(clazz);
		StringBuilder sb = new StringBuilder();
		Table table = clazz.getAnnotation(Table.class);

		sb.append("CREATE TABLE " + table.name() + "(\n");

		for (Field field : clazz.getDeclaredFields()) {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				if (column.primaryKey())
					sb.append(column.name() + " " + getSQLType(field.getType()) + " SERIAL PRIMARY KEY,\n");
				else
					sb.append(column.name() + " " + getSQLType(field.getType()) + " NOT NULL,\n");
			}
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append("\n)");
		return sb.toString();
	}

	public String getDropTableString(Class<?> clazz) throws SORMException {
		classTest(clazz);
		Table table = clazz.getAnnotation(Table.class);
		return "DROP TABLE " + table.name();
	}

	public String getInsertString(Class<?> clazz) throws SORMException {
		classTest(clazz);
		int columnCount = 0;
		StringBuilder sb = new StringBuilder();
		Table table = clazz.getAnnotation(Table.class);

		sb.append("INSERT INTO " + table.name() + " (");

		for (Field field : clazz.getDeclaredFields()) {
			Column column = field.getAnnotation(Column.class);
			if (column != null && (!column.primaryKey())) {
				columnCount++;
				sb.append(column.name() + ", ");
			}
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(")\nVALUES (");

		for (int i = 0; i < columnCount; i++) {
			sb.append("?, ");
		}

		sb.delete(sb.length() - 2, sb.length());
		sb.append(")");
		return sb.toString();
	}

	public String getSelectString(Class<?> clazz) throws SORMException {
		classTest(clazz);
		StringBuilder sb = new StringBuilder();
		Table table = clazz.getAnnotation(Table.class);

		sb.append("SELECT ");

		for (Field field : clazz.getDeclaredFields()) {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				sb.append(column.name() + ", ");
			}
		}

		sb.delete(sb.length() - 2, sb.length());
		sb.append("\nFROM " + table.name());
		return sb.toString();
	}

	public String getUpdateString(Class<?> clazz) throws SORMException {
		classTest(clazz);
		StringBuilder sb = new StringBuilder();
		Table table = clazz.getAnnotation(Table.class);

		String idName = "";

		sb.append("UPDATE " + table.name() + "\nSET ");

		for (Field field : clazz.getDeclaredFields()) {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				if (column.primaryKey())
					idName = column.name();
				else
					sb.append(column.name() + " = ?, ");
			}
		}

		sb.delete(sb.length() - 2, sb.length());
		sb.append("\nWHERE " + idName + " = ?");

		return sb.toString();
	}

	public void insert(Object object) throws SORMException {
		Class<?> clazz = object.getClass();
		String command = getInsertString(clazz);

		try (Connection connection = DriverManager.getConnection(DatabaseSettings.URL, DatabaseSettings.USER,
				DatabaseSettings.PASSWORD); PreparedStatement ps = connection.prepareStatement(command)) {
			int index = 1;
			for (Field field : clazz.getDeclaredFields()) {
				Column column = field.getAnnotation(Column.class);
				if (column != null && (!column.primaryKey())) {
					field.setAccessible(true);
					Object value = field.get(object);
					ps.setObject(index, value);
					index++;
				}
			}
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SORMException(e.getMessage());
		}
	}

	public <T> List<T> select(Class<T> clazz) throws SORMException {
		return select(clazz, null);
	}

	public <T> List<T> select(Class<T> clazz, String condition) throws SORMException {
		String command = getSelectString(clazz);
		if (condition != null)
			command += "\n" + condition;

		List<T> objects = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(DatabaseSettings.URL, DatabaseSettings.USER,
				DatabaseSettings.PASSWORD);
				Statement s = connection.createStatement();
				ResultSet rs = s.executeQuery(command)) {

			while (rs.next()) {
				T object = clazz.newInstance();
				int index = 1;
				for (Field field : clazz.getDeclaredFields()) {
					Column column = field.getAnnotation(Column.class);
					if (column != null) {
						field.setAccessible(true);
						Object value = rs.getObject(index);
						field.set(object, value);
						index++;
					}
				}
				objects.add(object);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new SORMException(e.getMessage());
		}
		return objects;
	}

	public void update(Object object) throws SORMException {
		Class<?> clazz = object.getClass();
		String command = getUpdateString(clazz);

		try (Connection connection = DriverManager.getConnection(DatabaseSettings.URL, DatabaseSettings.USER,
				DatabaseSettings.PASSWORD); PreparedStatement ps = connection.prepareStatement(command)) {
			int index = 1;
			int id = -1;
			for (Field field : clazz.getDeclaredFields()) {
				Column column = field.getAnnotation(Column.class);
				if (column != null) {
					field.setAccessible(true);
					if (column.primaryKey()) {
						id = (int) field.get(object);
					} else {
						Object value = field.get(object);
						ps.setObject(index, value);
						index++;
					}
				}
			}
			ps.setInt(index, id);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SORMException(e.getMessage());
		}
	}

	private String getSQLType(Class<?> clazz) {
		switch (clazz.getSimpleName()) {
		case "String":
			return "VARCHAR(128)";
		case "int":
			return "INT";
		case "Date":
			return "DATE";
		default:
			throw new IllegalArgumentException("Un. java type " + clazz.getCanonicalName());
		}
	}
}
