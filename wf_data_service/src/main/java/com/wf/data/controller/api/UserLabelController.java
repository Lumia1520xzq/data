package com.wf.data.controller.api;

import com.alibaba.fastjson.JSONArray;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.DataBaseController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.datarepo.entity.DatawareGameRecommendation;
import com.wf.data.dao.datarepo.entity.DatawareTopGames;
import com.wf.data.dao.datarepo.entity.DatawareUserLabel;
import com.wf.data.dto.RecommendationGameDto;
import com.wf.data.service.DatawareGameRecommendationService;
import com.wf.data.service.DatawareTopGamesService;
import com.wf.data.service.DatawareUserLabelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shihui
 * @date 2018/6/13
 */
@CrossOrigin(maxAge = 3600)
@RequestMapping("/data/api/userLabel")
@RestController
public class UserLabelController extends DataBaseController {

    @Autowired
    private DatawareUserLabelService userLabelService;

    @Autowired
    private DatawareGameRecommendationService datawareGameRecommendationService;

    @Autowired
    private DatawareTopGamesService topGamesService;

    @RequestMapping(value = "/getAllLabels", method = RequestMethod.GET)
    public String getAllLabels(@RequestParam String userId) {
        DatawareUserLabel userLabel =
                userLabelService.getUserLabelByUserId(Long.parseLong(userId));
        return JSONArray.toJSONString(userLabel);
    }

    @RequestMapping(value = "getRecommendationGameId", method = RequestMethod.GET)
    public String getRecommendationGameId(@RequestParam String userId) {
        String yesterDateString = DateUtils.getYesterdayDate();
        RecommendationGameDto dto = new RecommendationGameDto();
        if (StringUtils.isEmpty(userId)) {
            return "";
        }

        //推荐表不为空，返回类型为猜你喜欢
        List<DatawareGameRecommendation> recommendation = datawareGameRecommendationService
                .getRecommendationGameIdByUser(Long.parseLong(userId), yesterDateString);
        StringBuilder gameIds = new StringBuilder();
        String businessDate = "";
        if (recommendation != null && recommendation.size() > 0) {
            dto.setRecommendType(1);
            for (int i = 0; i < recommendation.size(); i++) {
                if (i == recommendation.size() - 1) {
                    gameIds.append(recommendation.get(i).getGameBn());
                    businessDate = recommendation.get(i).getStatisticalDateS();
                } else {
                    gameIds.append(recommendation.get(i).getGameBn()).append(";");
                }
            }
        } else {//取出最热门的两个游戏
            dto.setRecommendType(2);
            List<DatawareTopGames> top2Games = topGamesService.getTop2Games(yesterDateString);
            for (int i = 0; i < top2Games.size(); i++) {
                if (i == top2Games.size() - 1) {
                    gameIds.append(top2Games.get(i).getGameTypeN());
                    businessDate = top2Games.get(i).getStatisticalDateS();
                } else {
                    gameIds.append(top2Games.get(i).getGameTypeN()).append(";");
                }
            }
        }
        dto.setGameId(gameIds.toString());
        dto.setBusinessDate(businessDate);
        dto.setUserId(Long.parseLong(userId));
        return JSONArray.toJSONString(dto);
    }

}
