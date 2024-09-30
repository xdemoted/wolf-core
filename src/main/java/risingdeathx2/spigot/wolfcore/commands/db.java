package risingdeathx2.spigot.wolfcore.commands;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;

import risingdeathx2.spigot.wolfcore.core;

public class db {
    public db(core core, String[] args) {
        String host = core.getConfig().getString("database.host");
        int port = core.getConfig().getInt("database.port");
        String user = core.getConfig().getString("database.user");
        String password = core.getConfig().getString("database.password");
        core.getLogger().info(password + " " + user + " " + port + " " + host);

        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setServerName(host);
        dataSource.setPort(port);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        try {
            test(dataSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void test(MysqlDataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            if (conn.isValid(1)) {
                core.log("Connection is valid");
            } else {
                core.log("Connection is not valid");
            }
        }
    }
}
