package com.xxf.view.mentionedittext.span

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import com.sunhapper.x.spedit.mention.span.BreakableSpan
import com.sunhapper.x.spedit.mention.span.IntegratedBgSpan

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 1/28/21 12:58 PM
 * Description:使用三星输入法IntegratedSpan完整性不能保证，所以加上BreakableSpan使得@mention完整性被破坏时删除对应span
 */
open class MentionUser(val userId: String = "0",
                       val name: String = "test",
                       val foregroundColor: Int = Color.RED,
                       val backgroundColor: Int = Color.YELLOW)
    : BreakableSpan, DataSpan, IntegratedBgSpan {
    override var isShow = false
    private var styleSpan: Any? = null
    override var bgSpan: BackgroundColorSpan? = null

    val spannableString: Spannable
        get() {
            styleSpan = ForegroundColorSpan(foregroundColor)
            val spannableString = SpannableString(displayText)
            spannableString.setSpan(styleSpan, 0, spannableString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(this, 0, spannableString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            val stringBuilder = SpannableStringBuilder()
            return stringBuilder.append(spannableString).append(" ")
        }

    private val displayText: CharSequence
        get() = "@$name"


    override fun isBreak(spannable: Spannable): Boolean {
        val spanStart = spannable.getSpanStart(this)
        val spanEnd = spannable.getSpanEnd(this)
        val isBreak = spanStart >= 0 && spanEnd >= 0 && spannable.subSequence(spanStart, spanEnd).toString() != displayText
        if (isBreak && styleSpan != null) {
            spannable.removeSpan(styleSpan)
            styleSpan = null
        }
        return isBreak
    }

    override fun generateBgSpan(): BackgroundColorSpan {
        return BackgroundColorSpan(backgroundColor)
    }

    override fun toString(): String {
        return "MentionUser(userId='$userId', name='$name', foregroundColor=$foregroundColor, backgroundColor=$backgroundColor, isShow=$isShow)"
    }


}
