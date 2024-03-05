package com.yixin.xiaoyi.web.controller.inner;

import cn.dev33.satoken.annotation.SaIgnore;
import com.yixin.xiaoyi.common.core.domain.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: huangzexin
 * @date: 2023/8/1 23:29
 */
@RestController
@RequestMapping("/inner/test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class MyTestController {

    @GetMapping("/list")
    @SaIgnore
    public R<Void> findUsers() {
        return R.fail("新增菜单'"  + "'失败，菜单名称已存在");
    }

}
