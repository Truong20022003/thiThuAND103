package com.example.testthi1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.testthi1.databinding.ActivityChiTietBinding;
import com.example.testthi1.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class ChiTiet extends AppCompatActivity {
ActivityChiTietBinding binding;
    private HttpRequest httpRequest;
    private List<XeMay> list;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChiTietBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        list = new ArrayList<>();
        httpRequest = new HttpRequest();
        Intent intent = getIntent();
         id =  intent.getStringExtra("id");
         httpRequest.callAPI().getXeMayById(id).enqueue(new Callback<Response<XeMay>>() {
             @Override
             public void onResponse(Call<Response<XeMay>> call, retrofit2.Response<Response<XeMay>> response) {
                 if (response.isSuccessful()) {
                    // Lấy sản phẩm từ phản hồi
                    XeMay xeMay = response.body().getData();

                    // Hiển thị thông tin sản phẩm lên giao diện
                    if (xeMay != null) {
                        binding.tenXe.setText(xeMay.getTen_xe_ph41980());
                        binding.mauSac.setText(xeMay.getMausac_ph41980());
                        binding.giaBan.setText(xeMay.getGia_ban_ph41980());
                        binding.moTa.setText(xeMay.getMo_ta_ph41980());

                        // Load hình ảnh
                        Glide.with(ChiTiet.this)
                                .load(xeMay.getHinh_anh_ph41980())
                                .into(binding.imageAva);
                    }
                } else {
                    // Xử lý trường hợp lỗi
                    Toast.makeText(ChiTiet.this, "Không thể lấy thông tin xe", Toast.LENGTH_SHORT).show();
                }
             }

             @Override
             public void onFailure(Call<Response<XeMay>> call, Throwable t) {
                 Toast.makeText(ChiTiet.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                 Log.w("aaaaacsscs",t.getMessage());
             }
         });

    }




}