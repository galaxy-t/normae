package com.normae.elasticsearch.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@AllArgsConstructor
@NoArgsConstructor
public class QueryBo {

    private String key;

    private Object value;
}
