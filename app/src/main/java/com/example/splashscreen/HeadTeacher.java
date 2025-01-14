package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splashscreen.response_object.AdminResponseObject;
import com.example.splashscreen.response_object.HeadTeacherResponseObject;
import com.example.splashscreen.services.EndPoints;
import com.example.splashscreen.services.Repository;
import com.example.splashscreen.services.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class HeadTeacher extends AppCompatActivity {

    private static final String TAG = "TAG";
    EditText edtheadteachername,edtheadteacherpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_teacher);

        Button headteacherlogin = (Button) findViewById(R.id.headteacherlogin);
        edtheadteachername = (EditText) findViewById(R.id.edtheadteachername);
        edtheadteacherpass = (EditText) findViewById(R.id.edtheadteacherpass);
        TextView tvheadteachersignup = (TextView) findViewById(R.id.tvheadteachersignup);
        TextView tvheadteacherforgot = (TextView) findViewById(R.id.tvheadteacherforgot);

        // fetch test
        fetchAllHeadTeacher();

        headteacherlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get all the values
                String headteachername = edtheadteachername.getText().toString();
                String headteacherpass = edtheadteacherpass.getText().toString();

                boolean check = validateInfo(headteachername,headteacherpass);
                if (check==true){
                    Intent intent = new Intent(HeadTeacher.this, DashboardHeadTeacher.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Sorry Check Info again",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvheadteachersignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HeadTeacher.this, HeadTeacherSignUp.class);
                startActivity(intent);
                finish();
            }
        });

        tvheadteacherforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HeadTeacher.this, ResetPasswordHeadTeacher.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void fetchAllHeadTeacher() {
        Log.d(TAG, "fetch_headteacher: "+"called");
        Repository repository = RetrofitClientInstance.getRetrofitInstance(EndPoints.BASE_URL).create(Repository.class);
        Call<List<HeadTeacherResponseObject>> call = repository.fetchAllHeadTeacher();
        call.enqueue(new Callback<List<HeadTeacherResponseObject>>() {
            @Override
            public void onResponse(Call<List<HeadTeacherResponseObject>> call, retrofit2.Response<List<HeadTeacherResponseObject>> response) {
                Log.d(TAG, "onResponse: "+response);
                Log.d(TAG, "onResponse: "+response.body().get(0));
                if (response.isSuccessful() && response.code() == 200) {
                    List<HeadTeacherResponseObject> headTeacherResponseObjectList = response.body();
                    if (headTeacherResponseObjectList != null)
                        Toast.makeText(HeadTeacher.this,"Successful",Toast.LENGTH_SHORT).show();
                    //getBaseContext(),
                    //"Successful"+adminResponseObjectList.get(4).getAdminname(),
                    // Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getBaseContext(), "Error! Check internet connection and try again... "+response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<HeadTeacherResponseObject>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
                System.out.println();
                Toast.makeText(getBaseContext(), "Error! Check internet connection and try again... "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private Boolean validateInfo(String headteachername, String headteacherpass) {
        if (headteachername.length() == 0) {
            edtheadteachername.requestFocus();
            edtheadteachername.setError("Field cannot be empty");
            return false;
        }else if (headteacherpass.length() == 0) {
            edtheadteacherpass.requestFocus();
            edtheadteacherpass.setError("Field cannot be empty");
            return false;
        } else if (headteacherpass.length() <= 5) {
            edtheadteacherpass.requestFocus();
            edtheadteacherpass.setError("Minimum 6 Characters Required!!");
            return false;
        } else {
            return true;
        }
    }
}