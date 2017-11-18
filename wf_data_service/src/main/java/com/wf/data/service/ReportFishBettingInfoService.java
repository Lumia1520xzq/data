package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.entity.ReportFishBettingInfo;
import com.wf.data.dao.data.ReportFishBettingInfoDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportFishBettingInfoService  extends CrudService<ReportFishBettingInfoDao, ReportFishBettingInfo> {

    public ReportFishBettingInfo getFishBettingInfo(String tableName,ReportFishBettingInfo entity){
        return dao.getFishBettingInfo(tableName,entity);
    }

    /**
     * 保存数据（插入或更新）
     * @param entity
     */
    public void save(String tableName,ReportFishBettingInfo entity) {
        if (entity.isNewRecord()) {
            this.preInsert(entity);
            dao.insert(tableName,entity);
        }else{
            this.preUpdate(entity);
            dao.update(tableName,entity);
        }
        clearCache(entity);
    }


    public void batchSave(String tableName,List<ReportFishBettingInfo> entitys) {
        for (int i = 0; i < entitys.size();)
            doBatchSave(tableName,entitys, i, (i += 500) >= entitys.size() ? entitys.size() : i);
    }

    protected void doBatchSave(String tableName,List<ReportFishBettingInfo> entitys, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++){
           save(tableName, entitys.get(i));
        }
    }

    public void batchSave(String tableName,List<ReportFishBettingInfo> entitys,String bettingDate) {
        for (int i = 0; i < entitys.size();)
            doBatchSave(tableName,bettingDate,entitys, i, (i += 500) >= entitys.size() ? entitys.size() : i);
    }

    protected void doBatchSave(String tableName,String bettingDate,List<ReportFishBettingInfo> entitys, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++){
            entitys.get(i).setBettingDate(bettingDate);
            save(tableName, entitys.get(i));
        }
    }

    /**
     * 获取单条数据
     * @param tableName
     * @param id
     * @return
     */
    public ReportFishBettingInfo get(String tableName, Long id){
        return dao.get(tableName,id);
    }



    /**
     * 插入数据
     * @param tableName
     * @param entity
     * @return
     */
    public int insert( String tableName,ReportFishBettingInfo entity){
        return dao.insert(tableName,entity);
    }

    /**
     * 更新数据
     * @param tableName
     * @param entity
     * @return
     */
    public int update(String tableName,ReportFishBettingInfo entity){
        return dao.update(tableName,entity);
    }

    /**
     * 删除数据（一般为逻辑删除，更新del_flag字段为1）
     * @param id
     * @param tableName
     * @see public int delete(ReportFishBettingInfo entity)
     * @return
     */
    public int delete(String tableName,Long id){
        return dao.delete(tableName,id);
    }
}
