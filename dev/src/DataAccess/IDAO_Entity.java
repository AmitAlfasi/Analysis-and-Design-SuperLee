package DataAccess;

import java.sql.SQLException;

public interface IDAO_Entity {
    Object findByID(Object ID) throws SQLException, ClassNotFoundException;

    void insert(Object o) throws SQLException;
    void update(Object o) throws SQLException;
    void delete(Object o) throws SQLException;
}
