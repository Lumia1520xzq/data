package com.wf.data.common.utils;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.data.common.config.FishDbConfig;
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
 * @author lcs
 */
@Component
public class FishJdbcUtils {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FishDbConfig fishDbConfig;

    /**
     * 捕鱼APP
     * Connection
     *
     * @param config
     * @param dbName
     * @return
     * @throws SQLException
     */
    private Connection createConnection(FishDbConfig config, String dbName) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:mysql://");
        sb.append(config.getApphost());
        sb.append(":");
        sb.append(config.getAppport());
        sb.append("/");
        sb.append(dbName);
        sb.append("?useUnicode=true&characterEncoding=utf-8&useSSL=true");
        return DriverManager.getConnection(sb.toString(), config.getAppusername(), config.getApppassword());
    }

    /**
     * 根据dbname获取连接
     *
     * @param dbName
     * @return
     */
    private Connection createConnection(String dbName) {
        Connection conn = null;
        try {
            conn = createConnection(fishDbConfig, dbName);
        } catch (Exception e) {

            try {
                conn.close();
            } catch (SQLException e1) {
                logger.error("关闭连接失败,ex={}，traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
            }
            e.printStackTrace();
            logger.error("无法连接到源数据库,ex={}，traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
        }
        return conn;


    }

    /**
     * 获取查询结果
     *
     * @param sql
     * @param dbName
     * @return
     */
    public List<Map<String, Object>> query(String sql, String dbName) {
        logger.info("执行sql: traceId={}, sql={},", TraceIdUtils.getTraceId(), sql);
        List<Map<String, Object>> resultList = new ArrayList<>();
        Connection connection = createConnection(dbName);
        PreparedStatement ps = null;
        try {
            if (null == connection) {
                return resultList;
            }
            ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

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
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("执行查询错误: traceId={}, sql={},", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(sql));
        } finally {
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error("ps close error");
                }
            }

            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("connection close error");
                }
            }


        }
        return resultList;
    }
}
