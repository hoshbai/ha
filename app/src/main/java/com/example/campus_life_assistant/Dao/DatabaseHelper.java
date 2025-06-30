package com.example.campus_life_assistant.Dao;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executors;

public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";

    // Online connection configuration (uncommented)
    private static final String DB_URL = "jdbc:mysql://47.98.61.53:3306/AD?connectTimeout=5000&socketTimeout=10000"; // Added timeouts
    private static final String DB_USER = "remote_user";
    private static final String DB_PASSWORD = "FengKefeng520!";

    // Backup local connection configuration
    private static final String LOCAL_DB_URL = "jdbc:mysql://192.168.215.77:13306/ad?connectTimeout=3000&socketTimeout=5000"; // Added timeouts
    private static final String LOCAL_DB_USER = "remote_user";
    private static final String LOCAL_DB_PASSWORD = "123456";

    private static final String TABLE_USERS = "users";
    private Context context; // Context might not be strictly needed here unless used elsewhere

    public DatabaseHelper(Context context) {
        this.context = context; // Store context if needed for future use
    }


    public ConnectionResult testConnection() {
        ConnectionResult result = new ConnectionResult();
        Connection conn = null; // Declare outside try for finally block
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Log.d(TAG, "开始测试数据库连接: " + DB_URL); // Start testing DB connection
            result.status = "正在连接..."; // Connecting...
            result.details = "尝试连接到 " + DB_URL + " 使用用户 " + DB_USER; // Attempting to connect to... using user...

            // Load driver - MODIFIED for 5.1.x
            Log.d(TAG, "加载 MySQL JDBC 驱动 (5.1.x)..."); // Loading MySQL JDBC Driver (5.1.x)...
            Class.forName("com.mysql.jdbc.Driver"); // <-- 修改点 1/3: 使用旧版本驱动类名
            result.details += "\n✓ JDBC 驱动加载成功"; // JDBC Driver loaded successfully

            // Get connection (with timeouts specified in URL)
            Log.d(TAG, "正在获取数据库连接..."); // Getting database connection...
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            result.details += "\n✓ 成功建立数据库连接"; // Successfully established database connection

            // Test query
            Log.d(TAG, "正在创建语句对象..."); // Creating statement object...
            stmt = conn.createStatement();
            result.details += "\n✓ 创建语句对象成功"; // Statement object created successfully

            Log.d(TAG, "正在执行测试查询 'SELECT 1'..."); // Executing test query...
            rs = stmt.executeQuery("SELECT 1");
            result.details += "\n✓ 执行测试查询成功"; // Test query executed successfully

            if (rs.next()) {
                result.details += "\n✓ 获取查询结果成功: " + rs.getInt(1); // Successfully retrieved query result
            } else {
                result.details += "\n✗ 未能获取测试查询结果"; // Failed to get test query result
            }

            result.status = "连接成功"; // Connection successful
            result.success = true;
            Log.d(TAG, "数据库连接测试成功"); // Database connection test successful

        } catch (ClassNotFoundException e) {
            result.status = "驱动加载失败"; // Driver load failed
            result.details = "错误: 找不到 MySQL JDBC 驱动 (com.mysql.jdbc.Driver)。\n请确保已将 MySQL Connector/J 5.1.49 库添加到项目的依赖项中。";
            // Error: MySQL JDBC Driver not found... Ensure MySQL Connector/J 5.1.49 library is added to project dependencies.
            result.exception = e;
            Log.e(TAG, "MySQL JDBC 驱动未找到 (5.1.x)", e); // MySQL JDBC Driver not found (5.1.x)

        } catch (SQLException e) {
            result.status = "连接失败"; // Connection failed
            String serverIpPort = "未知"; // Unknown
            try {
                String[] parts = DB_URL.split("://")[1].split("/");
                serverIpPort = parts[0];
            } catch (Exception ignored) {}
            String dbName = "未知"; // Unknown
            try {
                String[] parts = DB_URL.split("/");
                dbName = parts[parts.length-1].split("\\?")[0];
            } catch (Exception ignored) {}

            // Log the detailed SQL exception
            Log.e(TAG, "数据库连接 SQLException - SQLState: " + e.getSQLState() + ", ErrorCode: " + e.getErrorCode() + ", Message: " + e.getMessage(), e);

            // Analyze SQLException for more user-friendly messages
            String detailsMsg;
            if (e.getMessage() != null && e.getMessage().contains("Access denied")) { // Added null check for message
                detailsMsg = "数据库访问被拒绝。\n"; // Database access denied.
                detailsMsg += "错误详情: " + e.getMessage() + "\n";
                detailsMsg += "SQLState: " + e.getSQLState() + ", ErrorCode: " + e.getErrorCode() + "\n";
                detailsMsg += "\n可能原因:\n"; // Possible reasons:
                detailsMsg += "1. 数据库用户名 '" + DB_USER + "' 或密码不正确。\n"; // Incorrect DB username or password.
                detailsMsg += "2. 用户 '" + DB_USER + "' 没有从您的设备 IP 地址连接到数据库 '" + dbName + "' 的权限。\n"; // User does not have permission to connect from your device IP.
                detailsMsg += "   - 请在 MySQL 服务器上检查授权: `SHOW GRANTS FOR '" + DB_USER + "'@'%';` (或特定 IP)\n"; // Check grants on MySQL server...
                detailsMsg += "   - 如需授权: `GRANT ALL PRIVILEGES ON " + dbName + ".* TO '" + DB_USER + "'@'%'; FLUSH PRIVILEGES;` (使用 '%' 允许所有 IP, 请注意安全风险)\n"; // To grant... (Use '%' for all IPs, note security risks)
            } else if (e.getMessage() != null && (e.getMessage().contains("Communications link failure") || e.getMessage().contains("connect timed out") || e.getMessage().contains("SocketTimeoutException"))) { // Added null check
                detailsMsg = "无法与数据库服务器建立通信链接。\n"; // Cannot establish communication link with DB server.
                detailsMsg += "目标: " + serverIpPort + "\n"; // Target:
                detailsMsg += "错误详情: " + e.getMessage() + "\n";
                detailsMsg += "SQLState: " + e.getSQLState() + ", ErrorCode: " + e.getErrorCode() + "\n";
                detailsMsg += "\n请检查:\n"; // Please check:
                detailsMsg += "1. 服务器 IP 地址和端口 (" + serverIpPort + ") 是否正确且可从您的网络访问。\n"; // Server IP and port correct and accessible from your network.
                detailsMsg += "2. 您的设备是否有有效的网络连接。\n"; // Your device has a valid network connection.
                detailsMsg += "3. MySQL 服务是否正在目标服务器上运行。\n"; // MySQL service running on target server.
                detailsMsg += "4. 防火墙 (设备、本地网络、服务器) 是否允许 TCP 端口 " + serverIpPort.split(":")[1] + " 的出站/入站连接。\n"; // Firewall allows connections on port...
                detailsMsg += "5. MySQL 服务器的 `bind-address` 配置是否允许远程连接 (应为 `0.0.0.0` 或服务器公网 IP)。\n"; // MySQL server bind-address allows remote connections.
                detailsMsg += "6. 连接是否超时 (增加了超时设置，但网络可能仍然太慢或不稳定)。\n"; // Connection timed out (timeouts added, but network might still be slow/unstable).
            } else if (e.getMessage() != null && e.getMessage().contains("Unknown database")) { // Added null check
                detailsMsg = "数据库 '" + dbName + "' 不存在。\n"; // Database does not exist.
                detailsMsg += "错误详情: " + e.getMessage() + "\n";
                detailsMsg += "SQLState: " + e.getSQLState() + ", ErrorCode: " + e.getErrorCode() + "\n";
                detailsMsg += "请确认数据库名称在连接 URL (" + DB_URL + ") 中是否正确，并且该数据库已在服务器上创建。\n"; // Confirm DB name is correct in URL and exists on server.
            } else {
                detailsMsg = "连接数据库时发生未分类的 SQL 错误。\n"; // Uncategorized SQL error occurred during connection.
                detailsMsg += "错误详情: " + (e.getMessage() != null ? e.getMessage() : "未知错误信息"); // Added null check
                detailsMsg += "\nSQLState: " + e.getSQLState() + ", ErrorCode: " + e.getErrorCode() + "\n";
                detailsMsg += "请查看 Logcat 中的详细堆栈跟踪以获取更多信息。\n"; // Check Logcat for detailed stack trace.
            }
            result.details = detailsMsg;
            result.exception = e;
            // Detailed logging already done above
        } finally {
            // Close resources in reverse order of creation
            Log.d(TAG, "正在关闭数据库资源..."); // Closing database resources...
            try {
                if (rs != null) {
                    rs.close();
                    Log.d(TAG, "ResultSet 已关闭"); // ResultSet closed
                }
            } catch (SQLException e) {
                Log.e(TAG, "关闭 ResultSet 时出错", e); // Error closing ResultSet
            }
            try {
                if (stmt != null) {
                    stmt.close();
                    Log.d(TAG, "Statement 已关闭"); // Statement closed
                }
            } catch (SQLException e) {
                Log.e(TAG, "关闭 Statement 时出错", e); // Error closing Statement
            }
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    Log.d(TAG, "Connection 已关闭"); // Connection closed
                }
            } catch (SQLException e) {
                Log.e(TAG, "关闭 Connection 时出错", e); // Error closing Connection
            }
            Log.d(TAG, "数据库资源关闭完成"); // Database resource closing finished
        }

        Log.d(TAG, "数据库连接测试完成。结果: " + result.status); // Database connection test finished. Result:
        return result;
    }

    /**
     * Gets a database connection using the primary configuration.
     * Throws SQLException on failure.
     */
    public Connection getConnection() throws SQLException {
        try {
            // Load driver - MODIFIED for 5.1.x
            Log.d(TAG, "加载驱动 (getConnection, 5.1.x)..."); // Loading driver...
            Class.forName("com.mysql.jdbc.Driver"); // <-- 修改点 2/3: 使用旧版本驱动类名
            Log.d(TAG, "驱动加载成功 (getConnection)"); // Driver loaded successfully

            Log.d(TAG, "尝试连接到 " + DB_URL + " (getConnection)"); // Attempting to connect to...
            // Use timeouts defined in the URL
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Log.d(TAG, "连接成功! (getConnection)"); // Connection successful!
            return conn;
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "MySQL JDBC驱动未找到 (getConnection, 5.1.x)", e); // MySQL JDBC Driver not found
            throw new SQLException("找不到数据库驱动 (请确保添加了 MySQL Connector/J 5.1.49 依赖)", e); // Cannot find database driver (Ensure MySQL Connector/J 5.1.49 dependency is added)
        } catch (SQLException e) {
            Log.e(TAG, "数据库连接失败 (getConnection): SQLState: " + e.getSQLState() + ", ErrorCode: " + e.getErrorCode() + ", Message: " + e.getMessage(), e); // DB connection failed...
            throw e; // Re-throw the exception to be handled by the caller
        }
    }

    /**
     * Attempts connection using the backup (local) configuration.
     * @return A ConnectionResult object.
     */
    public ConnectionResult tryLocalConnection() {
        ConnectionResult result = new ConnectionResult();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Log.d(TAG, "开始测试本地数据库连接: " + LOCAL_DB_URL); // Start testing local DB connection...
            result.status = "尝试本地连接..."; // Trying local connection...
            result.details = "尝试连接到备用数据库 " + LOCAL_DB_URL; // Attempting to connect to backup DB...

            // Load driver - MODIFIED for 5.1.x
            Log.d(TAG, "加载 MySQL JDBC 驱动 (tryLocalConnection, 5.1.x)..."); // Loading driver...
            Class.forName("com.mysql.jdbc.Driver"); // <-- 修改点 3/3: 使用旧版本驱动类名
            Log.d(TAG, "驱动加载成功 (tryLocalConnection)"); // Driver loaded successfully


            // Get connection
            conn = DriverManager.getConnection(LOCAL_DB_URL, LOCAL_DB_USER, LOCAL_DB_PASSWORD);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT 1");

            if (rs.next()) {
                result.details += "\n✓ 本地连接测试成功: " + rs.getInt(1); // Local connection test successful
            } else {
                result.details += "\n✗ 未能获取本地测试查询结果"; // Failed to get local test query result
            }

            result.status = "本地连接成功"; // Local connection successful
            result.success = true;
            Log.d(TAG, "本地数据库连接测试成功"); // Local database connection test successful

        } catch (Exception e) { // Catch Exception to include ClassNotFoundException
            result.status = "本地连接失败"; // Local connection failed
            result.details = "尝试本地连接 (" + LOCAL_DB_URL + ") 时出错: " + e.getMessage(); // Error during local connection attempt...
            result.exception = e;
            // Log the detailed exception, including potential ClassNotFoundException
            Log.e(TAG, "本地数据库连接失败: " + e.getMessage(), e); // Local database connection failed
        } finally {
            // Close resources for local connection attempt
            try { if (rs != null) rs.close(); } catch (SQLException e) { Log.e(TAG, "关闭本地 ResultSet 时出错", e); } // Error closing local ResultSet
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { Log.e(TAG, "关闭本地 Statement 时出错", e); } // Error closing local Statement
            try { if (conn != null && !conn.isClosed()) conn.close(); } catch (SQLException e) { Log.e(TAG, "关闭本地 Connection 时出错", e); } // Error closing local Connection
        }
        Log.d(TAG, "本地数据库连接测试完成。结果: " + result.status); // Local database connection test finished. Result:
        return result;
    }

    // Checks if the table exists, creates it if not (runs on a background thread)
    private void ensureTableExists() {
        // This should ideally only run ONCE after a successful connection is established.
        // Running it every time before register/login might cause redundant checks.
        // Consider a flag or checking connection status first.
        Log.d(TAG, "正在执行 ensureTableExists..."); // Executing ensureTableExists...
        Executors.newSingleThreadExecutor().execute(() -> {
            // Use IF NOT EXISTS to make the CREATE TABLE statement idempotent
            String checkTableQuery = "SHOW TABLES LIKE '" + TABLE_USERS + "'";
            // More robust create table query with constraints and charset
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(255) UNIQUE NOT NULL, " + // Make username unique and non-null
                    "password VARCHAR(255) NOT NULL" + // Make password non-null - IMPORTANT: HASH PASSWORDS IN REAL APPS
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;"; // Specify engine and charset for better compatibility

            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
                conn = getConnection(); // Get connection - handles its own exceptions and logging
                if (conn == null || conn.isClosed()) { // Check if connection was successful
                    Log.e(TAG, "ensureTableExists: 获取数据库连接失败，无法检查或创建表。"); // ensureTableExists: Failed to get DB connection, cannot check or create table.
                    return; // Exit if no valid connection
                }
                Log.d(TAG, "检查表 '" + TABLE_USERS + "' 是否存在..."); // Checking if table exists...
                stmt = conn.createStatement();

                rs = stmt.executeQuery(checkTableQuery);
                if (!rs.next()) { // If table does not exist
                    Log.i(TAG, "表 '" + TABLE_USERS + "' 不存在，正在创建..."); // Table does not exist, creating...
                    stmt.executeUpdate(createTableQuery); // Create table
                    Log.i(TAG, "表 '" + TABLE_USERS + "' 创建成功。"); // Table created successfully.
                } else {
                    Log.i(TAG, "表 '" + TABLE_USERS + "' 已存在。"); // Table already exists.
                }
            } catch (SQLException e) {
                // getConnection() already logs connection errors.
                // Log only table-specific SQL errors here.
                Log.e(TAG, "执行 SQL 语句检查或创建表 '" + TABLE_USERS + "' 时出错", e); // Error executing SQL statement checking or creating table...
            } catch (Exception e) {
                Log.e(TAG, "确保表存在时发生意外错误", e); // Unexpected error ensuring table exists
            } finally {
                // Close resources
                try { if (rs != null) rs.close(); } catch (SQLException e) { Log.e(TAG, "关闭 ensureTableExists ResultSet 时出错", e); }
                try { if (stmt != null) stmt.close(); } catch (SQLException e) { Log.e(TAG, "关闭 ensureTableExists Statement 时出错", e); }
                try { if (conn != null && !conn.isClosed()) conn.close(); } catch (SQLException e) { Log.e(TAG, "关闭 ensureTableExists Connection 时出错", e); }
            }
            Log.d(TAG, "ensureTableExists 执行完成。"); // ensureTableExists finished.
        });
    }


    // Register user (asynchronously)
    public void registerUser(String username, String password, OnRegisterListener listener) {
        Log.d(TAG, "尝试注册用户..."); // Attempting to register user...
        ensureTableExists(); // Ensure table exists before attempting registration
        // Add a small delay or a check here to ensure table creation is attempted
        // This ensures ensureTableExists task at least starts before register task.
        // A more robust solution would be to use a task queue or a state flag.
        new RegisterTask(listener).execute(username, password);
    }


    public void checkUserCredentials(String username, String password, OnLoginListener listener) {
        Log.d(TAG, "尝试检查用户凭据..."); // Attempting to check user credentials...
        ensureTableExists(); // Ensure table exists before attempting login
        // Add a small delay or a check here as in registerUser if needed
        new LoginTask(listener).execute(username, password);
    }

    // AsyncTask for registration
    private class RegisterTask extends AsyncTask<String, Void, Boolean> {
        private OnRegisterListener listener;
        private String errorMessage = null; // To store specific error message

        public RegisterTask(OnRegisterListener listener) {
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            Log.d(TAG, "RegisterTask doInBackground for user: " + username);

            // Basic validation
            if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
                errorMessage = "用户名和密码不能为空"; // Username and password cannot be empty
                Log.w(TAG, errorMessage);
                return false;
            }

            String query = "INSERT INTO " + TABLE_USERS + " (username, password) VALUES (?, ?)";
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                conn = getConnection(); // Get connection
                if (conn == null || conn.isClosed()) {
                    errorMessage = "注册失败：无法获取数据库连接"; // Registration failed: Cannot get DB connection
                    Log.e(TAG, errorMessage);
                    return false;
                }
                stmt = conn.prepareStatement(query);
                stmt.setString(1, username.trim()); // Trim username
                stmt.setString(2, password); // Consider hashing password in a real app - INSECURE currently
                Log.d(TAG, "执行注册 SQL: " + query + " with username: " + username);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    Log.i(TAG, "用户 '" + username + "' 注册成功"); // User registered successfully
                    return true;
                } else {
                    errorMessage = "注册失败，未插入行"; // Registration failed, no rows inserted
                    Log.w(TAG, errorMessage);
                    return false;
                }
            } catch (SQLException e) {
                errorMessage = "注册时数据库错误: " + e.getMessage(); // Database error during registration:
                if (e.getErrorCode() == 1062) { // MySQL error code for duplicate entry
                    errorMessage = "用户名 '" + username + "' 已存在"; // Username already exists
                }
                Log.e(TAG, "注册用户 '" + username + "' 出错", e); // Error registering user...
                return false;
            } catch (Exception e) { // Catch any other exceptions during the process
                errorMessage = "注册时发生意外错误: " + e.getMessage(); // Unexpected error during registration:
                Log.e(TAG, "注册用户 '" + username + "' 时发生意外错误", e); // Unexpected error registering user...
                return false;
            } finally {
                // Close resources
                try { if (stmt != null) stmt.close(); } catch (SQLException e) { Log.e(TAG, "关闭注册 Statement 时出错", e); } // Error closing registration Statement
                try { if (conn != null && !conn.isClosed()) conn.close(); } catch (SQLException e) { Log.e(TAG, "关闭注册 Connection 时出错", e); } // Error closing registration Connection
                Log.d(TAG, "RegisterTask 资源关闭完成");
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            Log.d(TAG, "RegisterTask onPostExecute: success=" + success);
            if (listener != null) {
                // Optionally pass back the error message
                // Modify the interface if needed: onRegisterResult(boolean success, String message)
                listener.onRegisterResult(success);
                if (!success && errorMessage != null) {
                    // You could show the errorMessage to the user via the listener/activity
                    Log.w(TAG, "注册失败详情: " + errorMessage); // Registration failure details:
                    // Example: ((Activity) context).runOnUiThread(() -> Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()); // Requires context to be an Activity
                }
            }
        }
    }

    // AsyncTask for login
    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        private OnLoginListener listener;
        private String errorMessage = null;

        public LoginTask(OnLoginListener listener) {
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            Log.d(TAG, "LoginTask doInBackground for user: " + username);

            if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
                errorMessage = "用户名和密码不能为空"; // Username and password cannot be empty
                Log.w(TAG, errorMessage);
                return false;
            }

            // IMPORTANT: In a real application, NEVER store plain text passwords.
            // You should hash the password during registration and compare hashes during login.
            String query = "SELECT id FROM " + TABLE_USERS + " WHERE username = ? AND password = ?";
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                conn = getConnection();
                if (conn == null || conn.isClosed()) {
                    errorMessage = "登录失败：无法获取数据库连接"; // Login failed: Cannot get DB connection
                    Log.e(TAG, errorMessage);
                    return false;
                }
                stmt = conn.prepareStatement(query);
                stmt.setString(1, username.trim());
                stmt.setString(2, password); // Compare plain text (insecure!)
                Log.d(TAG, "执行登录 SQL: " + query + " with username: " + username);
                rs = stmt.executeQuery();
                boolean loggedIn = rs.next(); // Returns true if a matching record is found
                if (loggedIn) {
                    Log.i(TAG, "用户 '" + username + "' 登录成功"); // User login successful
                } else {
                    errorMessage = "用户名或密码无效"; // Invalid username or password
                    Log.w(TAG, "用户 '" + username + "' 登录失败: " + errorMessage); // User login failed:
                }
                return loggedIn;
            } catch (SQLException e) {
                errorMessage = "登录时数据库错误: " + e.getMessage(); // Database error during login:
                Log.e(TAG, "检查用户 '" + username + "' 凭据出错", e); // Error checking user credentials...
                return false;
            } catch (Exception e) { // Catch any other exceptions
                errorMessage = "登录时发生意外错误: " + e.getMessage(); // Unexpected error during login:
                Log.e(TAG, "检查用户 '" + username + "' 凭据时发生意外错误", e); // Unexpected error checking user credentials...
                return false;
            } finally {
                // Close resources
                try { if (rs != null) rs.close(); } catch (SQLException e) { Log.e(TAG, "关闭登录 ResultSet 时出错", e); } // Error closing login ResultSet
                try { if (stmt != null) stmt.close(); } catch (SQLException e) { Log.e(TAG, "关闭登录 Statement 时出错", e); } // Error closing login Statement
                try { if (conn != null && !conn.isClosed()) conn.close(); } catch (SQLException e) { Log.e(TAG, "关闭登录 Connection 时出错", e); } // Error closing login Connection
                Log.d(TAG, "LoginTask 资源关闭完成");
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            Log.d(TAG, "LoginTask onPostExecute: success=" + success);
            if (listener != null) {
                // Optionally pass back the error message
                // Modify the interface if needed: onLoginResult(boolean success, String message)
                listener.onLoginResult(success);
                if (!success && errorMessage != null) {
                    Log.w(TAG, "登录失败详情: " + errorMessage); // Login failure details:
                    // Example: ((Activity) context).runOnUiThread(() -> Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()); // Requires context to be an Activity
                }
            }
        }
    }

    // Callback interfaces
    public interface OnRegisterListener {
        void onRegisterResult(boolean success);
        // Consider adding: void onRegisterResult(boolean success, String message);
    }

    public interface OnLoginListener {
        void onLoginResult(boolean success);
        // Consider adding: void onLoginResult(boolean success, String message);
    }

    /**
     * Helper class for returning detailed connection status.
     */
    public static class ConnectionResult {
        public boolean success = false;
        public String status = "未测试"; // Not tested
        public String details = "";
        public Exception exception = null; // Store the exception for logging

        @Override
        public String toString() {
            return "状态 (Status): " + status + "\n详情 (Details): " + details;
        }
    }
}