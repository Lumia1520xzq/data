package com.wf.data.common.utils;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.data.common.config.DbConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  lcs
 */
@Component
public class JdbcUtils {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private DbConfig dbConfig;

    private Connection createConnection(DbConfig config, String dbName) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:mysql://");
        sb.append(config.getHost());
        sb.append(":");
        sb.append(config.getPort());
        sb.append("/");
        sb.append(dbName);
        sb.append("?useUnicode=true&characterEncoding=utf-8&useSSL=true");
        return DriverManager.getConnection(sb.toString(), config.getUsername(), config.getPassword());
    }


    private Connection createConnection(String dbName) {
        Connection conn = null;
        try {
            conn = createConnection(dbConfig, dbName);
        } catch (SQLException e) {
            try {
                if (!(conn == null)){
                    conn.close();
                }
            } catch (Exception e1) {
                logger.error("关闭连接失败,ex={}，traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
            }
            e.printStackTrace();
            logger.error("无法连接到源数据库,ex={}，traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
            throw new RuntimeException("无法连接到源数据库:" + dbName + "," + dbConfig);
        }

        return conn;
    }

    public List<Map<String, Object>> query(String sql, String dbName) {
        logger.info("执行sql: traceId={}, sql={},", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(sql));
        Connection connection = createConnection(dbName);
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            List<Map<String, Object>> resultList = new ArrayList<>();
            ResultSetMetaData rsd = rs.getMetaData();
            int count = rsd.getColumnCount();
            String[] names = new String[count];
            for (int i = 0; i < count; i++) {
                names[i] = rsd.getColumnLabel(i + 1);
            }
            Map<String, Object> data;
            while (rs.next()) {
                data = new LinkedHashMap<>(count);
                for (int i = 0; i < count; i++) {
                    data.put(names[i], rs.getObject(i + 1));
                }
                resultList.add(data);
            }
            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("执行查询错误: traceId={}, sql={},", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(sql));
            throw new RuntimeException("执行查询错误:" + sql);
        } finally {
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }

            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {

                }
            }


        }
    }
}
