package com.yixin.xiaoyi.recruit.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.config.XiaoYiConfig;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.enums.ErrorCode;
import com.yixin.xiaoyi.common.exception.ServiceException;
import com.yixin.xiaoyi.common.helper.LoginHelper;
import com.yixin.xiaoyi.common.utils.DateUtils;
import com.yixin.xiaoyi.common.utils.sign.Base64;
import com.yixin.xiaoyi.common.utils.sign.Md5Utils;
import com.yixin.xiaoyi.recruit.dao.NewCountFinder;
import com.yixin.xiaoyi.recruit.dao.RecruitFinder;
import com.yixin.xiaoyi.recruit.dao.RecruitUserDeliverFinder;
import com.yixin.xiaoyi.recruit.entity.NewCount;
import com.yixin.xiaoyi.recruit.entity.Recruit;
import com.yixin.xiaoyi.recruit.entity.RecruitUserDeliver;
import com.yixin.xiaoyi.recruit.enums.RecruitNatureEnum;
import com.yixin.xiaoyi.recruit.model.dto.RecruitDayDTO;
import com.yixin.xiaoyi.recruit.model.dto.RecruitListDTO;
import com.yixin.xiaoyi.recruit.model.vo.RecruitUserDeliverVO;
import com.yixin.xiaoyi.recruit.service.RecruitApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: yixin
 * @date: 2023/3/8 16:28
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecruitApiServiceImpl implements RecruitApiService {

    private final RecruitFinder recruitFinder;
    private final RecruitUserDeliverFinder recruitUserDeliverFinder;
    private final XiaoYiConfig xiaoYiConfig;
    private final NewCountFinder newCountFinder;



    /**
     * 编辑 用户-招聘关联
     * @param recruitUserDeliverVO
     * @return
     */
    @Override
    public int editUserDelivered(RecruitUserDeliverVO recruitUserDeliverVO){
        Long userId = LoginHelper.getUserId();
        RecruitUserDeliver recruitUserDeliverInfo = recruitUserDeliverFinder.findByUserIdAndJobId(userId, recruitUserDeliverVO.getRecruitId());
        if(Objects.isNull(recruitUserDeliverInfo)){
            //新增
            RecruitUserDeliver recruitUser = new RecruitUserDeliver()
                    .setUserId(userId).setRecruitId(recruitUserDeliverVO.getRecruitId());
            return recruitUserDeliverFinder.insert(recruitUser);
        } else {
            //说明是取消投递
           return recruitUserDeliverFinder.deleteByUserIdAndRecruitId(userId,recruitUserDeliverVO.getRecruitId());
        }
    }

    /**
     * 获取用户的投递信息
     * @param pageQuery
     * @return
     */
    @Override
    public Page<RecruitListDTO> selectPageDeliverList(PageQuery pageQuery) {
        Long userId = LoginHelper.getUserId();
        Page<Recruit> recruitPage = recruitFinder.selectPageDeliverList(userId,pageQuery);
        List<RecruitListDTO> recruitListDTOS = new ArrayList<>();
        recruitPage.getRecords().stream().forEach(it->{
            RecruitListDTO recruitListDTO = new RecruitListDTO();
            BeanUtils.copyProperties(it,recruitListDTO);
            recruitListDTO.setDelivered(true);
            recruitListDTOS.add(recruitListDTO);
        });
        Page<RecruitListDTO> page = new Page<>();
        page.setRecords(recruitListDTOS);
        page.setTotal(recruitPage.getTotal());
        return page;
    }

    /**
     * 获取当天的信息数据
     * @return
     */
    @Override
    public RecruitDayDTO findTodayRecruitData() {
        NewCount newCount = newCountFinder.selectNewCount();
        return RecruitDayDTO.builder()
            .newTotalCount(newCount.getNewTotalCount())
            .newCampusCount(newCount.getNewCampusCount())
            .newPracticeCount(newCount.getNewPracticeCount())
            .updateTime(newCount.getShowUpdateTime()).build();

    }

    /**
     * 获取招聘信息
     * @param searchKey 职位
     * @param nature 性质
     * @param lateDays  发布时间 最近几天
     * @param location 位置
     * @return
     */
    @Override
    public Page<RecruitListDTO> selectRecruitList(String searchKey, String nature, Integer lateDays, String location, PageQuery pageQuery,String signature,Long timestamp){
        //签名和重发校验
        if(!generateSignature(timestamp).equals(signature) || Math.abs(System.currentTimeMillis() - timestamp) > 10000 ){
            throw new ServiceException(ErrorCode.VIOLATION_CALL);
        }
        Long userId = LoginHelper.getUserId();
        Page<Recruit> recruits = recruitFinder.selectRecruitPageList(searchKey,nature, lateDays, location,true,pageQuery);
        List<Long> recruitIds = recruits.getRecords().stream().map(Recruit::getId).collect(Collectors.toList());
        //获取用户已经投递的招聘id
        List<Long> deliveredJobIds = recruitUserDeliverFinder.findByUserId(userId,recruitIds).stream().map(RecruitUserDeliver::getRecruitId).distinct().collect(Collectors.toList());
        List<RecruitListDTO> recruitListDTOS = new ArrayList<>();
        recruits.getRecords().stream().forEach(it->{
            RecruitListDTO recruitListDTO = new RecruitListDTO();
            BeanUtils.copyProperties(it,recruitListDTO);
            recruitListDTO.setDelivered(deliveredJobIds.contains(it.getId()));
            recruitListDTOS.add(recruitListDTO);
        });
        Page<RecruitListDTO> page = new Page<>();
        page.setRecords(recruitListDTOS);
        page.setTotal(recruits.getTotal());
        return page;
    }

    /**
     * 游客获取数据
     * @param searchKey
     * @param nature
     * @param lateDays
     * @param location
     * @param pageQuery
     * @return
     */
    @Override
    public Page<RecruitListDTO> selectRecruitListByTourist(String searchKey, String nature, Integer lateDays, String location, PageQuery pageQuery,String signature,Long timestamp) {
        //签名和重发校验
        if(!generateSignature(timestamp).equals(signature) || Math.abs(System.currentTimeMillis() - timestamp) > 10000 ){
            throw new ServiceException(ErrorCode.VIOLATION_CALL);
        }
        Page<Recruit> recruits = recruitFinder.selectRecruitPageList(searchKey,nature, lateDays, location,true,pageQuery);
        List<RecruitListDTO> recruitListDTOS = new ArrayList<>();
        recruits.getRecords().stream().forEach(it->{
            RecruitListDTO recruitListDTO = new RecruitListDTO();
            BeanUtils.copyProperties(it,recruitListDTO);
            recruitListDTOS.add(recruitListDTO);
        });
        Page<RecruitListDTO> page = new Page<>();
        page.setRecords(recruitListDTOS);
        page.setTotal(recruits.getTotal());
        return page;
    }

    /**
     * 生成签名
     * @param timestamp
     * @return
     */
    public String generateSignature(Long timestamp){
        StringBuilder content = new StringBuilder();
        content
            .append("timestamp=").append(timestamp).append("&")
            .append("secret=").append(xiaoYiConfig.getSignSecret());
        //先MD5加密 再进行Base64加密
        String signature = Base64.encode(Md5Utils.hash(content.toString()).getBytes(StandardCharsets.UTF_8));
        return signature;
    }
}
