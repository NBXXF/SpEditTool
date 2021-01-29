package com.xxf.view.mentionedittext

import android.content.Context
import android.text.*
import androidx.annotation.CallSuper
import android.util.AttributeSet
import android.util.Log
import com.sunhapper.x.spedit.insertSpannableString
import com.sunhapper.x.spedit.view.SpXEditText
import com.xxf.view.mentionedittext.span.DataSpan
import com.xxf.view.mentionedittext.span.MentionUser
import com.xxf.view.mentionedittext.span.SpanResult
import kotlin.collections.ArrayList

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 1/28/21 12:58 PM
 * Description:
 */
open class SuperSpXEditText : SpXEditText {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var onTagMatchListener: OnTagMatchListener? = null;

    fun insertSpan(user: MentionUser) {
        insertSpannableString(this.text, user.spannableString, "@")
    }

    /**
     * 获取mentions
     */
    fun getAllSpan(): List<SpanResult> {
        val list: MutableList<SpanResult> = mutableListOf();
        if (!TextUtils.isEmpty(text)) {
            val ssb = SpannableStringBuilder(text);
            text!!.getSpans(0, text!!.length, DataSpan::class.java)?.forEach {
                val spanStart = ssb.getSpanStart(it);
                val spanEnd = ssb.getSpanEnd(it);
                ssb.removeSpan(it);
                list.add(SpanResult(spanStart, spanEnd, it));
            }
        }
        return list;
    }

    @CallSuper
    override fun onSelectionChanged(start: Int, end: Int) {
        super.onSelectionChanged(start, end)
        try {
            if (!TextUtils.isEmpty(text)) {
                if (start == end && start >= 0 && end <= text!!.length) {
                    var substring = text!!.substring(0, start)
                    val lastIndexOf = substring.lastIndexOf("@")
                    if (lastIndexOf >= 0) {
                        substring = substring.substring(lastIndexOf, substring.length);
                        val spans = text!!.getSpans(start - substring.length, start, DataSpan::class.java)
                        if (spans == null || spans.isEmpty()) {
                            onTagMatchListener?.onTagMatch("@", substring)
                            return
                        }
                    }
                }
            }
            onTagMatchListener?.onTagMatch("@", null)
        } catch (e: Throwable) {
            e.printStackTrace()
            Log.d("======>", "match fail" + e);
        }
    }

    interface OnTagMatchListener {
        /**
         * @param tag "@"
         * @param matched  null,"@" "@xxx"
         */
        fun onTagMatch(tag: String, matched: String?)
    }
}