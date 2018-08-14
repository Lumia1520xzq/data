package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareTopGamesDao;
import com.wf.data.dao.datarepo.entity.DatawareTopGames;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shihui
 * @date 2018/8/14
 */

@Service
public class DatawareTopGamesService extends
        CrudService<DatawareTopGamesDao, DatawareTopGames> {


    public List<DatawareTopGames> getTop2Games(String yesterDateString) {
        return dao.getTop2Games(yesterDateString);
    }
}
