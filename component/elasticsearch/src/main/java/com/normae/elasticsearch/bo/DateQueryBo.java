package com.normae.elasticsearch.bo;

import lombok.Data;

/**
 * @author lishanglei
 * @version v1.0.0
 * @date 2020/7/14
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/14              lishanglei      v1.0.0           Created
 */
@Data
public class DateQueryBo {

    private String field;

    private String startTime;

    private String endTime;
}
