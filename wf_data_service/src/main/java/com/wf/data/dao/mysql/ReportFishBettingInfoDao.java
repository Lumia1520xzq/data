package com.wf.data.dao.mysql;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.entity.mysql.ReportFishBettingInfo;
import org.apache.ibatis.annotations.Param;

@MyBatisDao(tableName = "report_fish_betting_info")
public interface ReportFishBettingInfoDao extends CrudDao<ReportFishBettingInfo> {


    ReportFishBettingInfo getFishBettingInfo(@Param("tableName") String tableName, @Param("entity") ReportFishBettingInfo entity);

    /**
     * 获取单条数据
     * @param tableName
     * @param id
     * @return
     */
    public ReportFishBettingInfo get(@Param("tableName") String tableName, @Param("id") Long id);


    /**
     * 插入数据
     * @param tableName
     * @param entity
     * @return
     */
    public int insert(@Param("tableName") String tableName, @Param("entity") ReportFishBettingInfo entity);

    /**
     * 更新数据
     * @param tableName
     * @param entity
     * @return
     */
    public int update(@Param("tableName") String tableName, @Param("entity") ReportFishBettingInfo entity);

    /**
     * 删除数据（一般为逻辑删除，更新del_flag字段为1）
     * @param id
     * @param tableName
     * @see public int delete(ReportFishBettingInfo entity)
     * @return
     */
    public int delete(@Param("tableName") String tableName, @Param("id") Long id);
}
