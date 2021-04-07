package com.wzc1748995976.hotelbooking.MakePerfect

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Nullable
import com.wzc1748995976.hotelbooking.R


@SuppressLint("CustomViewStyleable")
class MyLinearLayout(
    context: Context,
    @Nullable attrs: AttributeSet?,
    defStyleAttr: Int
) :
    LinearLayout(context, attrs, defStyleAttr) {
    private val imageViewLeft: ImageView
    private val imageViewRight: ImageView
    private val textViewLeft: TextView
    private val textViewRight: TextView
    private val bottomView: ImageView
    private var isBottom = true //是否显示底部的下划线
    private var isShowRightImg = false //是否显示右侧的图标

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(context, attrs, -1) {}

    private fun initView() {
        //如果isBottom为true，显示下划线，否则隐藏
        if (isBottom) {
            bottomView.visibility = View.VISIBLE
        } else {
            bottomView.visibility = View.GONE
        }
        if(isShowRightImg){
            imageViewRight.visibility = View.VISIBLE
        } else {
            imageViewRight.visibility = View.INVISIBLE
        }
    }

    init {
        //加载布局
        LayoutInflater.from(getContext()).inflate(R.layout.mine_item, this)
        //获取设置属性对象
        val ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.mine_item_view)
        //获取控件
        bottomView = findViewById(R.id.item_bottom)
        imageViewLeft = findViewById(R.id.item_img_left)
        textViewLeft = findViewById(R.id.item_text_left)
        imageViewRight = findViewById(R.id.item_img_right)
        textViewRight = findViewById(R.id.item_text_right)
        //设置属性值
        isBottom = ta.getBoolean(R.styleable.mine_item_view_show_bottom_line, true)
        isShowRightImg = ta.getBoolean(R.styleable.mine_item_view_is_show_right_img, false)
        textViewLeft.text = ta.getString(R.styleable.mine_item_view_show_left_text)
        textViewRight.text = ta.getString(R.styleable.mine_item_view_show_right_text)
        // R.drawable.ic_circle_hotel是默认值
        imageViewLeft.setBackgroundResource(
            ta.getResourceId(
                R.styleable.mine_item_view_show_left_img,
                R.drawable.ic_circle_hotel
            )
        )
        imageViewRight.setBackgroundResource(
            ta.getResourceId(
                R.styleable.mine_item_view_show_right_img,
                R.drawable.ic_circle_hotel
            )
        )
        //回收属性对象
        ta.recycle()
        initView()
    }
}