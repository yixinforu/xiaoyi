package com.yixin.xiaoyi.web.controller.inner.statistics;

import cn.dev33.satoken.annotation.SaIgnore;
import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.core.module.common.vo.DateVO;
import com.yixin.xiaoyi.recruit.model.dto.RecruitDataDTO;
import com.yixin.xiaoyi.recruit.service.impl.RecruitAdminServiceImpl;
import com.yixin.xiaoyi.user.model.dto.ClientIpTrendDTO;
import com.yixin.xiaoyi.user.model.dto.UserDataDTO;
import com.yixin.xiaoyi.user.service.XyUserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/9/3 15:38
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/inner/statistics")
public class StatisticsController {

    private final XyUserAdminService xyUserAdminService;
    private final RecruitAdminServiceImpl recruitAdminService;


    /**
     * 获取用户数据基本信息
     * @return
     */
    @GetMapping("/user-count")
    @SaIgnore
    public R<UserDataDTO> findUserCountData(){
        return R.ok(xyUserAdminService.findUserCountData());
    }


    /**
     * 获取ip访问数据
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/user-client-ip")
    @SaIgnore
    public R<List<ClientIpTrendDTO>> findClientIpTrendData(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        return R.ok(xyUserAdminService.findClientIpTrendData(new DateVO(startDate,endDate)));
    }


    /**
     * 获取招聘信息
     * @return
     */
    @GetMapping("/recruit-count")
    @SaIgnore
    public R<RecruitDataDTO> findRecruitData(){
        return R.ok(recruitAdminService.findRecruitBaseData());
    }
}
