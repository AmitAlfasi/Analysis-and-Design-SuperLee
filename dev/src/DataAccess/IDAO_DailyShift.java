package DataAccess;

import java.sql.SQLException;

public interface IDAO_DailyShift {

    Object findByKey(Object date, Object id) throws SQLException, ClassNotFoundException;

    void insert(Object o, Object id) throws SQLException, ClassNotFoundException;
    void update(Object o, Object id) throws SQLException, ClassNotFoundException;
    void delete(Object o, Object id) throws SQLException, ClassNotFoundException;
}
