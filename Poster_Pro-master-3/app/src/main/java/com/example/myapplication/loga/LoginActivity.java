package com.example.myapplication.loga;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.Main2Activity;
import com.example.myapplication.R;

/**
 Reference
 https://github.com/Alaeddinalhamoud/Login-Signup-AndroidSDK-SQLite
 https://github.com/Android-Tutorials-Hub/login-register-sqlite-tutorial
 */
public class LoginActivity extends BaseActivity {


    private ImageButton mTvLogin;
    private EditText mEdtAddress;
    private EditText mEdtPassword;
    private TextView mTvPassword;
    private int uid;
    private SQLiteDatabase db;
    private Cursor c;

    /*布局
     * */
    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    /*
     * 初始化控件
     * */
    @Override
    public void onBindView() {

        initView();

        /*
         * 创建数据库的地方，可以放到App中
         * */
        DatabaseHelper helper = new DatabaseHelper(LoginActivity.this, "test_db", null, 1);
        //获取数据库对象
        db = helper.getReadableDatabase();

    }

    private void initView() {
        mTvLogin = findViewById(R.id.tv_login);
        mEdtAddress = findViewById(R.id.edt_address);
        mTvPassword = findViewById(R.id.tv_password);
        mEdtPassword = findViewById(R.id.edt_password);
        ImageButton mloginback=findViewById(R.id.loginback);
        mloginback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Main2Activity.class);
                startActivity(intent);
            }
        });

        mEdtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                /*
                 * 查询账号是否为已注册账号
                 * */
                c = db.rawQuery("select * from personal_info", null);
                int exist=0;
                while (c.moveToNext()) {
                    if (c.getString(c.getColumnIndex("email_address")).equals(mEdtAddress.getText().toString())) {
                        exist = 1;
                        break;
                    }
                }
                if (exist == 1) {
                    mTvPassword.setVisibility(View.VISIBLE);
                    mEdtPassword.setVisibility(View.VISIBLE);
                } else {
                    mTvPassword.setVisibility(View.GONE);
                    mEdtPassword.setVisibility(View.GONE);
                }

                c.close();
            }

        });


        mTvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEdtAddress.getText().toString().equals("")){
                    Toast.makeText(getApplication(),"Please enter E-mail address !",Toast.LENGTH_SHORT).show();
                    return;
                }
                /*
                 * 检测邮箱是否为正确格式
                 * */
                if (!mEdtAddress.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z0-9]+.[a-z]+")){
                    Toast.makeText(getApplication(),"Please enter the correct format of E-mail address !",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mTvPassword.getVisibility()==View.VISIBLE){
                    /*
                     * 验证密码
                     * */
                    c = db.rawQuery("select password from personal_info where email_address='"+mEdtAddress.getText().toString()+"'",null);
                    String password = "";
                    while (c.moveToNext()){
                        password = c.getString(c.getColumnIndex("password"));
                    }
                    c.close();
                    if (password.equals("")){
                        Toast.makeText(getApplication(),"No query password !",Toast.LENGTH_SHORT).show();
                    }else {
                        if (password.equals(mEdtPassword.getText().toString())){
                            c = db.rawQuery("select puid from personal_info where email_address = ?", new String[]{mEdtAddress.getText().toString()});
                            while(c.moveToNext()){
                                uid = c.getInt(0);
                            }
                            SharedPreferences preference = getSharedPreferences("CURRENT_UID", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preference.edit();
                            editor.putInt("uid", uid);
                            editor.commit();
                            Intent intent = new Intent(getApplication(), MainActivity.class);
                            Bundle login = new Bundle();
                            login.putString("email",mEdtAddress.getText().toString());
                            intent.putExtras(login);
                            startActivity(intent);
                            finish();

                            Toast.makeText(getApplication(),"Landing......",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplication(),"Password Error !",Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    /*
                     * 传address到注册页面
                     * */
                    Bundle bundle = new Bundle();
                    bundle.putString("address",mEdtAddress.getText().toString());
                    Intent intent = new Intent(getApplication(), SignUpActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭数据库连接对象
        db.close();
    }
}
