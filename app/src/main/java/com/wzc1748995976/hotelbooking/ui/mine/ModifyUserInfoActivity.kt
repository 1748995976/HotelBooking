package com.wzc1748995976.hotelbooking.ui.mine

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.FileUtils
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.qqtheme.framework.picker.NumberPicker
import cn.qqtheme.framework.picker.OptionPicker
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.LoginViewModel
import com.wzc1748995976.hotelbooking.MakePerfect.LoadingDialog
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.model.OperateResponse
import com.wzc1748995976.hotelbooking.logic.model.UserInfoResponseData
import com.wzc1748995976.hotelbooking.logic.network.MyService
import com.wzc1748995976.hotelbooking.logic.network.MyServiceCreator
import com.wzc1748995976.hotelbooking.ui.homepage.CityPickerInstance
import com.wzc1748995976.hotelbooking.ui.homepage.onPickCallBack
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropCircleTransformation
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random


class ModifyUserInfoActivity : AppCompatActivity() {

    private lateinit var viewModel: ModifyUserInfoViewModel
    private var uri:Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val blurImageView = findViewById<ImageView>(R.id.iv_blur)
        val avatarImageView = findViewById<ImageView>(R.id.iv_avatar)
        if(requestCode == 5 && resultCode == Activity.RESULT_OK && data != null){
            uri = data.data
            Glide.with(this)
                .load(uri)
                .circleCrop()
                .priority(Priority.IMMEDIATE)
                .placeholder(R.mipmap.loading)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(avatarImageView)
            //实现个人中心头部磨砂布局
            Glide.with(this)
                .asBitmap()
                .load(uri)
                .transform(BlurTransformation(20, 1),CenterCrop())
                .priority(Priority.IMMEDIATE)
                .placeholder(R.mipmap.loading)
                .into(blurImageView)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_user_info)
        val loading = object : LoadingDialog(this){
            override fun cancle() {
            }
        }

        viewModel = ViewModelProvider(this).get(ModifyUserInfoViewModel::class.java)

        val userInfoResponseData = intent.getParcelableExtra<UserInfoResponseData>("userInfoResponseData")

        val blurImageView = findViewById<ImageView>(R.id.iv_blur)
        val avatarImageView = findViewById<ImageView>(R.id.iv_avatar)
        val nickName = findViewById<EditText>(R.id.nickName)
        val sex = findViewById<TextView>(R.id.sex)
        val age = findViewById<TextView>(R.id.age)
        val phone = findViewById<EditText>(R.id.phone)
        val location = findViewById<TextView>(R.id.location)
        val backImg = findViewById<ImageView>(R.id.backImg)
        val saveInfo = findViewById<TextView>(R.id.saveInfo)

        viewModel.modifyResult.observe(this, Observer { result->
            loading.dismiss()
            if(result.isFailure){
                Toast.makeText(HotelBookingApplication.context,"网络异常",Toast.LENGTH_LONG).show()
            }else{
                val data = result.getOrNull()
                if(data!=null && data){
                    Toast.makeText(HotelBookingApplication.context,"上传成功",Toast.LENGTH_LONG).show()
                    finish()
                }else{
                    Toast.makeText(HotelBookingApplication.context,"上传失败",Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        })

        if(userInfoResponseData != null){
            nickName.setText(userInfoResponseData.name)
            sex.text = userInfoResponseData.sex
            age.text = userInfoResponseData.age
            phone.setText(userInfoResponseData.phone)
            location.text = userInfoResponseData.location
            Glide.with(this)
                .load(MyServiceCreator.userAvatar + userInfoResponseData.avatar)
                .circleCrop()
                .priority(Priority.IMMEDIATE)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.mipmap.loading)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(avatarImageView)
            //实现个人中心头部磨砂布局
            Glide.with(this)
                .asBitmap()
                .load(MyServiceCreator.userAvatar + userInfoResponseData.avatar)
                .transform(BlurTransformation(20, 1),CenterCrop())
                .priority(Priority.IMMEDIATE)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.mipmap.loading)
                .into(blurImageView)
        }

        backImg.setOnClickListener {
            finish()
        }
        saveInfo.setOnClickListener {
            loading.show()
            val userInfo = UserInfoResponseData(userInfoResponseData?.account ?: "未知account",
                userInfoResponseData?.avatar ?: "未知avatar",nickName.text.toString(),
                sex.text.toString(),age.text.toString(),phone.text.toString(),location.text.toString())
            if(uri != null){
                val file = uriToFileQ(this,uri!!)!!
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file )
                val body = MultipartBody.Part.createFormData("file", HotelBookingApplication.account, requestFile)
                MyServiceCreator.create<MyService>().postAvatar(body).enqueue(object : retrofit2.Callback<OperateResponse>{
                    override fun onResponse(
                        call: Call<OperateResponse>,
                        response: Response<OperateResponse>
                    ) {
                        val result = response.body()
                        if(result != null && result.data){
                            viewModel.modify(userInfo)
                        }
                    }

                    override fun onFailure(call: Call<OperateResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }else{
                viewModel.modify(userInfo)
            }

        }
        avatarImageView.setOnClickListener {
            when(this.let { it1 -> ActivityCompat.checkSelfPermission(it1,WRITE_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_GRANTED){
                true->{
                    val picture = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(picture, 5)
                }
                false->{
                    if(!this.let { it1 ->
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                it1, WRITE_EXTERNAL_STORAGE)
                        }){
                        Toast.makeText(this, "请前往应用设置赋予存储权限", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val sexPicker = OptionPicker(this, listOf("男","女","保密"))
        sexPicker.setOffset(3)
        sexPicker.selectedIndex = 1
        sexPicker.setTextSize(20)
        sexPicker.setOnOptionPickListener(object : OptionPicker.OnOptionPickListener(){
            override fun onOptionPicked(index: Int, item: String?) {
                sex.text = item
            }
        })
        sex.setOnClickListener {
            sexPicker.show()
        }

        val agePicker = NumberPicker(this)
        agePicker.setOffset(5)
        agePicker.setRange(0,130)
        agePicker.setSelectedItem(19)
        agePicker.setOnNumberPickListener(object : NumberPicker.OnNumberPickListener(){
            override fun onNumberPicked(index: Int, item: Number?) {
                age.text = "${item}岁"
            }
        })
        age.setOnClickListener {
            agePicker.show()
        }

        location.setOnClickListener {
            CityPickerInstance.let {
                it.setonPickCallBack(object : onPickCallBack {
                    override fun getResultToSet(
                        cityName: String,
                        adCode: String,
                        cityCode: String,
                        pinyin: String
                    ) {
                        location.text = cityName
                    }
                })
                it.getInstance(this)?.show()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        //将状态栏的颜色设置成透明色
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
        actionBar?.hide()
    }
}


@RequiresApi(Build.VERSION_CODES.Q)
private fun uriToFileQ(context: Context, uri: Uri): File? =
    if (uri.scheme == ContentResolver.SCHEME_FILE)
        File(requireNotNull(uri.path))
    else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        //把文件保存到沙盒
        val contentResolver = context.contentResolver
        val displayName = run {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.let {
                if(it.moveToFirst())
                    it.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                else null
            }
        }?:"${System.currentTimeMillis()}${Random.nextInt(0, 9999)}.${MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(contentResolver.getType(uri))}"

        val ios = contentResolver.openInputStream(uri)
        if (ios != null) {
            File("${context.externalCacheDir!!.absolutePath}/$displayName")
                .apply {
                    val fos = FileOutputStream(this)
                    FileUtils.copy(ios, fos)
                    fos.close()
                    ios.close()
                }
        } else null
    } else null