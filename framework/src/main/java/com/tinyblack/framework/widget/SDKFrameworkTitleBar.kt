package com.tinyblack.framework.widget

import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.tinyblack.framework.R


class SDKFrameworkTitleBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var title: String?
    private val tvTitle: TextView
    private val rightImageId: Int
    private val ivRight: ImageView
    private val ivBack: ImageView
    fun setRightIcon(
        rightImageId: Int? = null,
        clickListener: ((rightImage: ImageView) -> Unit)? = null
    ) {
        rightImageId?.let {
            ivRight.setImageResource(it)
        }
        clickListener?.let {
            ivRight.setOnClickListener { it(ivRight) }
        }
    }

    fun getRightIcon(): ImageView = ivRight
    fun setBackListener(clickListener: (backImage: ImageView) -> Unit) {
        ivBack.setOnClickListener { clickListener(ivBack) }
    }

    fun setTitle(title: String) {
        this.title = title
        tvTitle.text = title
    }

    init {
        val array = context.obtainStyledAttributes(
            attrs,
            R.styleable.SDKFrameworkTitleBar
        )
        rightImageId = array.getResourceId(
            R.styleable.SDKFrameworkTitleBar_rightImageId, 0
        )
        title = array.getString(R.styleable.SDKFrameworkTitleBar_titleContent)
        array.recycle()
        val view = LayoutInflater.from(context).inflate(R.layout.sdk_view_title_bar, null, false)
        tvTitle = view.findViewById<TextView>(R.id.tvTitleBarTitle)
        tvTitle.text = title
        ivRight =
            view.findViewById<ImageView>(R.id.ivTitleBarRight)
                .apply { setImageResource(rightImageId) }
        ivBack = view.findViewById(R.id.ivTitleBarBack)
        addView(view)
    }
}