package com.sunhapper.x.spedit.view

import android.content.Context
import androidx.annotation.CallSuper
import android.text.Editable
import android.text.Selection
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import com.sunhapper.x.spedit.insertSpannableString
import com.xxf.view.mentionedittext.span.DataSpan
import com.xxf.view.mentionedittext.span.MentionUser
import kotlin.collections.ArrayList

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 1/28/21 12:58 PM
 * Description:
 */
open class SuperSpXEditText : SpXEditText {
    private val mListeners: ArrayList<OnAtTextWatcher> = ArrayList();

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    @CallSuper
    override fun addTextChangedListener(watcher: TextWatcher?) {
        super.addTextChangedListener(watcher)
        if (watcher is OnAtTextWatcher) {
            mListeners.add(watcher)
        }
    }

    @CallSuper
    override fun removeTextChangedListener(watcher: TextWatcher?) {
        super.removeTextChangedListener(watcher)
        mListeners.remove(watcher);
    }

    fun insertMention(user: MentionUser) {
        insertSpannableString(this.text, user.spannableString)
    }


    abstract class OnAtTextWatcher : TextWatcher {

        abstract fun onAtMatched(matched: String?);

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        @CallSuper
        override fun afterTextChanged(s: Editable?) {
            if (!TextUtils.isEmpty(s)) {
                val start = Selection.getSelectionEnd(s);
                var end = Selection.getSelectionEnd(s);
                if (start == end && start >= 0 && end <= s!!.length) {
                    var substring = s!!.substring(0, start)
                    val lastIndexOf = substring.lastIndexOf("@")
                    if (lastIndexOf >= 0) {
                        substring = s.substring(lastIndexOf, start);
                        val spans = s.getSpans(lastIndexOf, substring.length, DataSpan::class.java)
                        if (spans != null && spans.size > 0) {
                            return
                        } else {
                            onAtMatched(substring)
                        }
                    }
                }
            }
        }
    }
}