package com.normae.elasticsearch.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lishanglei
 * @version v1.0.0
 * @date 2020/7/20
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/20              lishanglei      v1.0.0           Created
 */
@Data
public class YsInfo {

    //头像
    private String headPortrait;
    //姓名
    private String name;
    //出生日期
    private String bronDate;
    //国籍
    private String nationality;
    //授衔机构
    private String endowed;
    //邮箱
    private String email;
    //研究领域
    private String domain;
    //签约状态
    private String signStatus;
    //签约类型
    private String signType;
    //联络情况
    private String contact;
    //标签
    private String tag;
    //手机
    private String phone;
    //工作单位名称
    private String employer;
    //生活习惯
    private String habit;
    //信仰
    private String belief;
    //教育经历
    private String education;
    //发明专利
    private String invent;
    //工作日期
    private String workDate;
    //授衔日期
    private String endowedDate;
    //获奖日期
    private String prizeDate;
    //论文标题
    private String thesisTitle;
    //获奖名称
    private String prizeName;
    //标准日期
    private LocalDateTime standardDate;
    //是否展厅展示
    private boolean isShow;

}
