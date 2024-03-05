package com.yixin.xiaoyi.web.controller.api.recruit;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.core.controller.BaseController;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.common.enums.ErrorCode;
import com.yixin.xiaoyi.common.exception.ServiceException;
import com.yixin.xiaoyi.common.utils.StringUtils;
import com.yixin.xiaoyi.recruit.model.dto.RecruitDayDTO;
import com.yixin.xiaoyi.recruit.model.dto.RecruitListDTO;
import com.yixin.xiaoyi.recruit.model.vo.RecruitUserDeliverVO;
import com.yixin.xiaoyi.recruit.service.RecruitApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author: yixin
 * @date: 2023/3/8 21:44
 */
@RestController
@RequestMapping("/c/recruit")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecruitApiController extends BaseController {


    private final RecruitApiService recruitApiService;


    /** 分页
     * 招聘信息表
     * @param searchKey
     * @param nature
     * @param releaseTime
     * @param location
     * @return
     */
    @SaCheckPermission(value = {"recruit:list:all","recruit:list:limit"},mode = SaMode.OR)
    @GetMapping("/list")
    public TableDataInfo<RecruitListDTO> getJobInfoList(
        @RequestParam(value = "searchKey",required = false) String searchKey,
        @RequestParam(value = "nature",required = false) String nature,
        @RequestParam(value = "releaseTime",required = false) Integer releaseTime,
        @RequestParam(value = "location",required = false) String location, PageQuery pageQuery,
        @RequestParam(value = "signature") String signature,
        @RequestParam(value = "timestamp") Long timestamp){
        Page<RecruitListDTO> recruitListDTOS = new Page<>();
        if(StpUtil.hasPermission("recruit:list:all")){
            //vip
            recruitListDTOS = recruitApiService.selectRecruitList(searchKey,nature,releaseTime,location,pageQuery,signature,timestamp);
        }else if(StpUtil.hasPermission("recruit:list:limit")){
            //普通用户
            checkDataAccess(searchKey,nature,releaseTime,location,pageQuery);
            recruitListDTOS  =recruitApiService.selectRecruitList(searchKey,nature,releaseTime,location,pageQuery,signature,timestamp);
        }
       return TableDataInfo.build(recruitListDTOS);
    }


    /**
     * 游客获取的数据
     * 触发：进入首页时会触发，所以其为游客/已登录的用户
     * @return
     */
    @SaIgnore
    @GetMapping("/list-identity")
    public TableDataInfo<RecruitListDTO> getRecruitListByTourist(
        @RequestParam(value = "signature") String signature,
        @RequestParam(value = "timestamp") Long timestamp
    ){
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNum(1);
        pageQuery.setPageSize(10);
        if(StpUtil.isLogin()){
            return getJobInfoList(null,null,null,null,pageQuery,signature,timestamp);
        }else{
            return TableDataInfo.build(recruitApiService.selectRecruitListByTourist(null,null,null,null,pageQuery,signature,timestamp));
        }
    }




    /**
     * 普通用户会进行功能限制，不允许使用查询功能，并且数据限制在1页
     * @param searchKey
     * @param nature
     * @param releaseTime
     * @param location
     * @param pageQuery
     */
    private void checkDataAccess(String searchKey,String nature,Integer releaseTime,String location,PageQuery pageQuery){

        if(StringUtils.isAllBlank(searchKey,nature,location) && ObjectUtil.isNull(releaseTime) &&  pageQuery.getPageNum() <= 1 ){
            return;
        }
            throw new ServiceException(ErrorCode.USER_DATA_UNAUTHORIZED);
    }




    /**
     * 获取用户已投递的信息
     * @param pageQuery
     * @return
     */
    @GetMapping("/user-delivered")
    public TableDataInfo<RecruitListDTO> getUserDeliveredList(PageQuery pageQuery){
        return  TableDataInfo.build(recruitApiService.selectPageDeliverList(pageQuery));
    }

    /**
     * 编辑 用户-招聘 关联信息
     * @param recruitUserDeliverVO
     */
    @PostMapping("/user_delivered")
    public R<Void> editUserDelivered(@RequestBody RecruitUserDeliverVO recruitUserDeliverVO){
       return toAjax(recruitApiService.editUserDelivered(recruitUserDeliverVO));
    }

    /**
     * 获取当天的信息数据
     * @return
     */
    @SaIgnore
    @GetMapping("/update-data")
    public R<RecruitDayDTO> findTodayRecruitData(){
        return R.ok(recruitApiService.findTodayRecruitData());
    }


}
