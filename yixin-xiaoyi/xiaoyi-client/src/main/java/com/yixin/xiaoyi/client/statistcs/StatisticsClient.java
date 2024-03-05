package com.yixin.xiaoyi.client.statistcs;

import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.recruit.model.dto.RecruitDataDTO;
import com.yixin.xiaoyi.user.model.dto.ClientIpTrendDTO;
import com.yixin.xiaoyi.user.model.dto.UserDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/9/3 21:12
 */
@FeignClient(
    name = "xy-statistics",
    contextId = "xy-statistics-client",
    url = "${feign.client.config.xiaoyi.url}")
public interface StatisticsClient {

    /**
     * 获取用户数据基本信息
     * @return
     */
    @GetMapping("/inner/statistics/user-count")
    public R<UserDataDTO> findUserCountData();


    /**
     * 获取ip访问数据
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/inner/statistics/user-client-ip")
    public R<List<ClientIpTrendDTO>> findClientIpTrendData(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate);



    /**
     * 获取招聘信息
     * @return
     */
    @GetMapping("/inner/statistics/recruit-count")
    public R<RecruitDataDTO> findRecruitData();
}
