package com.example.testthi1;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.testthi1.databinding.ActivityMainBinding;
import com.example.testthi1.databinding.ItemAddBinding;
import com.example.testthi1.databinding.ItemUpdateBinding;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnClick {
    ActivityMainBinding binding;
    private HttpRequest httpRequest;

    private XemayAdapter adapter;
    private List<XeMay> list;
    File fileimg;
    File fileimgAdd;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        list = new ArrayList<>();
        adapter = new XemayAdapter(this, list, this);
        httpRequest = new HttpRequest();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rcvDanhSach.setLayoutManager(layoutManager);
        binding.rcvDanhSach.setAdapter(adapter);
        httpRequest.callAPI().getListSanPham().enqueue(getListXeMay);
        ////nút add
        binding.floatAddDanhSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadXeMay();
            }
        });
        //// ô edit tìm kiếm
        binding.edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                timKiemXeMay(binding.edtTimKiem.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ////// sắp xếp
        binding.btnSapXepGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sapXepGiamDan();
            }
        });
        binding.btnSapXepTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sapXepTangDan();
            }
        });

        //////////refresh
        binding.main.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        httpRequest.callAPI().getListSanPham().enqueue(getListXeMay);
                        binding.main.setRefreshing(true);
                    }
                }, 5000);

                // Trong hàm onResponse hoặc onFailure của request tải dữ liệu
                binding.main.setRefreshing(false);

            }
        });


    }
    // Hàm lấy danh sách

    Callback<ArrayList<XeMay>> getListXeMay = new Callback<ArrayList<XeMay>>() {
        @Override
        public void onResponse(Call<ArrayList<XeMay>> call, Response<ArrayList<XeMay>> response) {
            if (response.isSuccessful()) {
                ArrayList<XeMay> ds = response.body();
                list.clear();
                list.addAll(ds);
                getData(ds);
            }
        }

        @Override
        public void onFailure(Call<ArrayList<XeMay>> call, Throwable t) {

        }
    };

    private void getData(ArrayList<XeMay> ds) {
        adapter = new XemayAdapter(this, ds, this);
        binding.rcvDanhSach.setAdapter(adapter);
    }


    //////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////
    // xóa
    private void deleteXeMay(int position) {
        String id = list.get(position).get_id();
        XeMay xeMay = list.get(position);
        httpRequest.callAPI().deleteSanPham(id).enqueue(new Callback<Response<Void>>() {
            @Override
            public void onResponse(Call<Response<Void>> call, retrofit2.Response<Response<Void>> response) {
                if (response.isSuccessful()) {
                    list.remove(xeMay);

                    Toast.makeText(MainActivity.this, "Đã xóa xe máy thành công", Toast.LENGTH_SHORT).show();
                    //gọi lại dòng này để update lại danh sách
                    httpRequest.callAPI().getListSanPham().enqueue(getListXeMay);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Xóa xe máy không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<Void>> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClickDelete(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc muốn xóa?");

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Không xóa", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                deleteXeMay(position);
                adapter.notifyDataSetChanged();
                dialogInterface.dismiss();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //////////////////////////////////////////////
    //////////////////////////////////////////////
    /////////////////////////////////////////////////////
// thêm
    ItemAddBinding addBinding;

    private void chooseImageAndUpload() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        getImageAdd.launch(intent);

    }

    ActivityResultLauncher<Intent> getImageAdd = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Uri tempUri = null;

                        Intent data = o.getData();
                        if (data.getData() != null) {
                            Uri imageUri = data.getData();
                            tempUri = imageUri;


                            fileimgAdd = createFileFormUri(tempUri, "image");
                            if (tempUri != null) {
                                Glide.with(MainActivity.this)
                                        .load(tempUri)
                                        .thumbnail(Glide.with(MainActivity.this).load(R.drawable.ic_launcher_background))
                                        .centerCrop()
                                        .circleCrop()
                                        .skipMemoryCache(true)
                                        .into(addBinding.imgAvatar);
                            }
                        }
                    }
                }
            });

    private void uploadXeMay() {
        LayoutInflater layoutInflater = getLayoutInflater();
        addBinding = ItemAddBinding.inflate(layoutInflater);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(addBinding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.show();


        addBinding.imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageAndUpload();
            }
        });
        addBinding.btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileimgAdd != null) {
                    String tenXe = addBinding.edtTenSach.getText().toString();
                    String mauSac = addBinding.edtMauSac.getText().toString();
                    String giaBan = addBinding.edtGiaBan.getText().toString();
                    String moTa = addBinding.edtMoTa.getText().toString();
                    // Tạo MultipartBody.Part cho hình ảnh
                    RequestBody imageRequestBody = RequestBody.create(MediaType.parse("hinh_anh_ph41980/*"), fileimgAdd);
                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("hinh_anh_ph41980", fileimgAdd.getName(), imageRequestBody);

                    // Tạo Map chứa các RequestBody cho các trường thông tin xe máy
                    Map<String, RequestBody> requestBodyMap = new HashMap<>();
                    requestBodyMap.put("ten_xe_ph41980", getRequestBody(tenXe));
                    requestBodyMap.put("mausac_ph41980", getRequestBody(mauSac));
                    requestBodyMap.put("gia_ban_ph41980", getRequestBody(giaBan));
                    requestBodyMap.put("mo_ta_ph41980", getRequestBody(moTa));


                    // Gọi phương thức để thêm xe máy lên server
                    httpRequest.callAPI().addXeMay(requestBodyMap, imagePart).enqueue(new Callback<Response<XeMay>>() {
                        @Override
                        public void onResponse(Call<Response<XeMay>> call, Response<Response<XeMay>> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Thêm xe máy thành công", Toast.LENGTH_SHORT).show();
                                httpRequest.callAPI().getListSanPham().enqueue(getListXeMay);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(MainActivity.this, "Thêm xe máy thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Response<XeMay>> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Thêm xe máy thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Chưa có ảnh", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }


    ///////////////////////////////////////
    ////////////////////////////////////////
    //////////////////////////////////////
    // Sửa
    ItemUpdateBinding updateBinding;


    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        getImage.launch(intent);
    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Uri tempUri = null;

                        Intent data = o.getData();
                        if (data.getData() != null) {
                            Uri imageUri = data.getData();
                            tempUri = imageUri;
                            imagePath = imageUri.toString();

                            fileimg = createFileFormUri(tempUri, "image");
                            if (tempUri != null) {
                                Glide.with(MainActivity.this)
                                        .load(tempUri)
                                        .thumbnail(Glide.with(MainActivity.this).load(R.drawable.ic_launcher_background))
                                        .centerCrop()
                                        .circleCrop()
                                        .skipMemoryCache(true)
                                        .into(updateBinding.imgAvatar);
                            }
                        }
                    }
                }
            });


    private File createFileFormUri(Uri path, String name) {
        File _file = new File(MainActivity.this.getCacheDir(), name + ".png");
        try {
            InputStream in = MainActivity.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            Log.d("123123", "createFileFormUri: " + _file);
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onClickUpdate(int position) {
        LayoutInflater layoutInflater = getLayoutInflater();
        updateBinding = ItemUpdateBinding.inflate(layoutInflater);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(updateBinding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.show();
        updateBinding.edtTenSach.setText(list.get(position).getTen_xe_ph41980());
        updateBinding.edtGiaBan.setText(list.get(position).getGia_ban_ph41980());
        updateBinding.edtMauSac.setText(list.get(position).getMausac_ph41980());
        updateBinding.edtMoTa.setText(list.get(position).getMo_ta_ph41980());

        String url = list.get(position).getHinh_anh_ph41980();
        String newUrl = url.replace("localhost", "10.0.2.2");
        if (url.isEmpty()) {
            String url1 = "https://cdn4.iconfinder.com/data/icons/picture-sharing-sites/32/No_Image-512.png";
            Picasso.get().load(url1).into(updateBinding.imgAvatar);
            Toast.makeText(this, "Không có ảnh", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Glide.with(MainActivity.this)
                    .load(newUrl)
                    .thumbnail(Glide.with(MainActivity.this).load(R.drawable.ic_launcher_background))
                    .into(updateBinding.imgAvatar);

        }
        updateBinding.imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        updateBinding.btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        updateBinding.btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenXe = updateBinding.edtTenSach.getText().toString();
                String mauSac = updateBinding.edtMauSac.getText().toString();
                String giaBan = updateBinding.edtGiaBan.getText().toString();
                String moTa = updateBinding.edtMoTa.getText().toString();
                Map<String, RequestBody> requestBodyMap = new HashMap<>();
                requestBodyMap.put("ten_xe_ph41980", getRequestBody(tenXe));
                requestBodyMap.put("mausac_ph41980", getRequestBody(mauSac));
                requestBodyMap.put("gia_ban_ph41980", getRequestBody(giaBan));
                requestBodyMap.put("mo_ta_ph41980", getRequestBody(moTa));

                if (fileimg != null) {
                    // Gọi phương thức để cập nhật dữ liệu lên server
                    // Chuyển đổi java.io.File thành MultipartBody.Part
                    RequestBody requestBody = RequestBody.create(MediaType.parse("hinh_anh_ph41980/*"), fileimg);
                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("hinh_anh_ph41980", fileimg.getName(), requestBody);
                    // Gọi phương thức để cập nhật dữ liệu lên server
                    String id = list.get(position).get_id();
                    try {

                        httpRequest.callAPI().updateXeMay(id, requestBodyMap, imagePart).enqueue(new Callback<Response<XeMay>>() {
                            @Override
                            public void onResponse(Call<Response<XeMay>> call, Response<Response<XeMay>> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    httpRequest.callAPI().getListSanPham().enqueue(getListXeMay);
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(MainActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Response<XeMay>> call, Throwable t) {

                            }
                        });
                    } catch (Exception e) {
                        Log.w("aaaaaaaaaaa", e);
                    }


                }
            }
        });

    }

    /////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    // click qua màn hình chi tiết
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, ChiTiet.class);
        String idXeMay = list.get(position).get_id();
        intent.putExtra("id", idXeMay);
        startActivity(intent);
        Log.d("wwwwwwwwwwww", String.valueOf(intent));
    }

    /////////////////////////////////
    ////////////////////////////////
    //////////////////
    //Tìm kiếm
    private void timKiemXeMay(String keyword) {
        ArrayList<XeMay> searchResult = new ArrayList<>();
        for (XeMay xeMay : list) {
            if (xeMay.getTen_xe_ph41980().toLowerCase().contains(keyword.toLowerCase())) {
                searchResult.add(xeMay);
            }
        }
        adapter.timkiemData(searchResult);
    }

    //////////////////////////////////////////////
    ///////////////////////////////////////////////
    /////////////////////////////////////////////////
    // SẮP XẾP
    private void sapXepGiamDan() {
        try {
            Collections.sort(list, new Comparator<XeMay>() {
                @Override
                public int compare(XeMay sp1, XeMay sp2) {
                    int gia1 = Integer.parseInt(sp1.getGia_ban_ph41980());
                    int gia2 = Integer.parseInt(sp2.getGia_ban_ph41980());
                    return Integer.compare(gia2, gia1);
                }
            });

            // Cập nhật lại RecyclerView sau khi đã sắp xếp
            adapter.timkiemData(list); // Cập nhật lại dữ liệu trong adapter với danh sách đã sắp xếp
        } catch (NumberFormatException e) {
            Log.w("Exception", e);
        }
    }

    private void sapXepTangDan() {
        try {
            Collections.sort(list, new Comparator<XeMay>() {
                @Override
                public int compare(XeMay sp1, XeMay sp2) {
                    int gia1 = Integer.parseInt(sp1.getGia_ban_ph41980());
                    int gia2 = Integer.parseInt(sp2.getGia_ban_ph41980());
                    return Integer.compare(gia1, gia2);
                }
            });

            // Cập nhật lại RecyclerView sau khi đã sắp xếp
            adapter.timkiemData(list); // Cập nhật lại dữ liệu trong adapter với danh sách đã sắp xếp
        } catch (NumberFormatException e) {
            Log.w("Exception", e);
        }
    }
}