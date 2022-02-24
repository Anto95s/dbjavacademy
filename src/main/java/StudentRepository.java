import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRepository {
    Connection connection;

    StudentRepository(Connection connection) {
        this.connection = connection;
    }

    public void createTable() throws SQLException {
        var statement = connection.createStatement();
        statement.executeUpdate("""
                    DROP SCHEMA IF EXISTS my_schema CASCADE;
                    CREATE SCHEMA IF NOT EXISTS my_schema;
                    CREATE TABLE my_schema.students (
                        id              SERIAL PRIMARY KEY,
                        name            TEXT NOT NULL,
                        surname         TEXT NOT NULL,
                        department      TEXT DEFAULT 'no department',
                        driving_license TEXT
                    )
                """);
    }

    public ArrayList<StudentEntity> findAll(String query) throws SQLException {
        var resultSet = connection.createStatement().executeQuery(query);
        var data = new ArrayList<StudentEntity>();
        while (resultSet.next()) {
            data.add(new StudentEntity(
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    resultSet.getString("department"),
                    resultSet.getString("driving_license")
            ));
        }
        return data;
    }

    public Optional<StudentEntity> findById(int id) throws SQLException {
        return findOne("SELECT * FROM my_table.students WHERE id = " + id);
    }

    public Optional<StudentEntity> findOne(String query) throws SQLException {
        var result = findAll(query);
        return result.isEmpty()
                ? Optional.empty()
                : Optional.of(result.get(0));
    }

    public void populateTable(ArrayList<StudentEntity> data) throws SQLException {
        var bulkInsertString = """
                    INSERT INTO my_schema.students (name, surname, department, driving_license)
                    VALUES (?, ?, ?, ?)
                """;
        var preparedStatement = connection.prepareStatement(bulkInsertString);

        for (var student : data) {
            preparedStatement.setString(1, student.name);
            preparedStatement.setString(2, student.surname);
            preparedStatement.setString(3, student.department);
            preparedStatement.setString(4, student.drivingLicense);
            preparedStatement.addBatch();
        }

        preparedStatement.executeBatch();
    }

    public void saveSingleRecord(StudentEntity studentEntity) throws SQLException {
        populateTable(new ArrayList<>(List.of(studentEntity)));
    }
}
