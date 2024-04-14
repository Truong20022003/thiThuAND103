package com.example.testthi1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testthi1.databinding.ItemRcvBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class XemayAdapter extends RecyclerView.Adapter<XemayAdapter.ViewHodle> {
private List<XeMay> list;
private Context context;
public OnClick mListener;
public XemayAdapter(Context context, List<XeMay> list,OnClick mListener){
    this.context = context;
    this.list = list;
    this.mListener = mListener;
}
    @NonNull
    @Override
    public ViewHodle onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRcvBinding binding = ItemRcvBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHodle(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodle holder, int position) {
        XeMay xeMay = list.get(position);
        holder.binding.tenXe.setText("Tên xe: "+xeMay.getTen_xe_ph41980());
        holder.binding.mauSac.setText("Màu xe: "+xeMay.getMausac_ph41980());
        holder.binding.giaBan.setText("gia bán: "+xeMay.getGia_ban_ph41980());
        holder.binding.moTa.setText("Mô tả: "+xeMay.getMo_ta_ph41980());
        if (xeMay.getHinh_anh_ph41980().isEmpty()){
            String url = "https://cdn4.iconfinder.com/data/icons/picture-sharing-sites/32/No_Image-512.png";
            Toast.makeText(context, "Không tìm thấy đường dẫn hình ảnh", Toast.LENGTH_SHORT).show();
            Picasso.get().load(url).into(holder.binding.imageAva);
        }else {
            String url = xeMay.getHinh_anh_ph41980();
            String newUrl = url.replace("localhost","10.0.2.2");
            Picasso.get().load(newUrl).into(holder.binding.imageAva);
        }
    holder.binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListener!=null){
                mListener.onClickUpdate(position);
            }
        }
    });
holder.binding.btnXoa.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (mListener!=null){
            mListener.onClickDelete(position);
        }
    }
});
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (mListener!=null){
            mListener.onItemClick(position);
        }
    }
});

    }
    public void timkiemData(List<XeMay> newData) {
        list.clear();
        list.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHodle extends RecyclerView.ViewHolder {
        ItemRcvBinding binding;
        public ViewHodle(ItemRcvBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
