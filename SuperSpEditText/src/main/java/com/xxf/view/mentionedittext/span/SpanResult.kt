package com.xxf.view.mentionedittext.span

import java.io.Serializable

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 1/28/21 2:28 PM
 * Description:
 */
data class SpanResult(val start: Int, val end: Int, val span: DataSpan) : Serializable{
    override fun toString(): String {
        return "SpanResult(start=$start, end=$end, span=$span)"
    }
}