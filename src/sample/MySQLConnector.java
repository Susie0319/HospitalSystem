package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLConnector {
        // 加载数据库驱动  oracle.jdbc.driver
        private static String dbdriver = "com.mysql.cj.jdbc.Driver";
        // 获取mysql连接地址
        private static String dburl = "jdbc:mysql://127.0.0.1:3306/cmxDatabaseName?&useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Hongkong";        // ��������
        private static String username = "root";
        // 数据库密码
        private static String userpassword = "     ";
        // 获取数据的一个连接
        public static Connection conn = null;
        // 5种加锁支持
        public static int TRANSACTION_NONE = 0;
        public static int TRANSACTION_READ_UNCOMMITTED = 1;
        public static int TRANSACTION_READ_COMMITTED = 2;
        public static int TRANSACTION_REPEATABLE_READ = 4;
        public static int TRANSACTION_SERIALIZABLE = 8;

        /**
         * 获取数据库连接
         *
         * @param myProjName
         * @return
         */
        private static Connection getConn(String myProjName) {
            Connection conn = null;
            try {
                Class.forName(dbdriver);
                String myjdbcUrl = dburl.replace("cmxDatabaseName", myProjName);
                conn = DriverManager.getConnection(myjdbcUrl, username, userpassword);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return conn;
        }
        /**
         * 关闭数据库连接
         *
         * @param rs
         * @param ps
         * @param conn
         */
        private static void closeAll(ResultSet rs, PreparedStatement ps,
                                     Connection conn) {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn == null)
                return;
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /**
         * 查表，返回行的列表，每个列表中包含的列的列表
         *
         * @param sql
         * @return
         */
        public static List<List<Object>> getData(String sql) {
            Connection conn = getConn("HOSPITAL");
            PreparedStatement ps = null;
            List<List<Object>> list = new ArrayList<List<Object>>();
            ResultSet rs = null;
            try {
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                ResultSetMetaData md = rs.getMetaData();
                int columnCount = md.getColumnCount();
                while (rs.next()) {
                    List<Object> lst = new ArrayList<Object>();
                    for (int i = 1; i <= columnCount; ++i) {
                        lst.add(rs.getObject(i) == null ? "" : rs.getObject(i));
                    }
                    list.add(lst);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeAll(rs, ps, conn);
            }
            return list;
        }

        /**
         * 向表中插入数据，返回是否插入成功
         *
         * @param sql
         * @return
         */
        public static int insData(String sql, int selLevel) {
            Connection conn = getConn("HOSPITAL");
            PreparedStatement ps = null;
            //  List<List<Object>> list = new ArrayList<List<Object>>();
            int rs = -1;
            try {
                conn.setTransactionIsolation(selLevel);
                ps = conn.prepareStatement(sql);
                rs = ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage() + " " + e.getSQLState() + " " + e.getCause());
                e.printStackTrace();
            } finally {
                // closeAll(rs, ps, conn);
            }
            return rs;
        }
}
