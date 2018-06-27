package com.thaihuy.order.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.thaihuy.order.DAO.BanAnDAO;
import com.thaihuy.order.DAO.GoiMonDAO;
import com.thaihuy.order.DTO.BanAnDTO;
import com.thaihuy.order.DTO.GoiMonDTO;
import com.thaihuy.order.Fragment.HienThiThucDonFragment;
import com.thaihuy.order.R;
import com.thaihuy.order.ThanhToanActivity;
import com.thaihuy.order.TrangChuActivity;

import java.util.List;

public class AdapterHienThiBanAn extends BaseAdapter implements View.OnClickListener {

    Context context;
    int layout;
    List<BanAnDTO> banAnDTOList;
    ViewHolderBanAn viewHolderBanAn;
    BanAnDAO banAnDAO;
    GoiMonDAO goiMonDAO;
    FragmentManager fragmentManager;

    public AdapterHienThiBanAn(Context context, int layout, List<BanAnDTO> banAnDTOList) {
        this.context = context;
        this.layout = layout;
        this.banAnDTOList = banAnDTOList;
        banAnDAO = new BanAnDAO(context);
        goiMonDAO = new GoiMonDAO(context);
        fragmentManager = ((TrangChuActivity) context).getSupportFragmentManager();
    }

    @Override
    public int getCount() {
        return banAnDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return banAnDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return banAnDTOList.get(position).getMaBan();
    }

    public class ViewHolderBanAn {
        ImageView imBanAn, imGoiMon, imThanhToan, imAnButton;
        TextView txtTenBanAn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewHolderBanAn = new ViewHolderBanAn();
            view = inflater.inflate(R.layout.custom_layout_hienthibanan, parent, false);
            viewHolderBanAn.imBanAn = view.findViewById(R.id.imBanAn);
            viewHolderBanAn.imGoiMon = view.findViewById(R.id.imGoiMon);
            viewHolderBanAn.imThanhToan = view.findViewById(R.id.imThanhToan);
            viewHolderBanAn.imAnButton = view.findViewById(R.id.imAnButton);
            viewHolderBanAn.txtTenBanAn = view.findViewById(R.id.txtTenBanAn);
            view.setTag(viewHolderBanAn);
        } else {
            viewHolderBanAn = (ViewHolderBanAn) view.getTag();
        }
        if (banAnDTOList.get(position).isDuocChon()) {
            HienThiButton();
        } else {
            AnButton(false);
        }
        BanAnDTO banAnDTO = banAnDTOList.get(position);
        String kttinhtrang = banAnDAO.LayTinhTrangBanTheoMa(banAnDTO.getMaBan());
        if (kttinhtrang.equals("true")) {
            viewHolderBanAn.imBanAn.setImageResource(R.drawable.order_true);
        } else {
            viewHolderBanAn.imBanAn.setImageResource(R.drawable.order);
        }
        viewHolderBanAn.txtTenBanAn.setText(banAnDTO.getTenBan());
        viewHolderBanAn.imBanAn.setTag(position);

        viewHolderBanAn.imBanAn.setOnClickListener(this);
        viewHolderBanAn.imGoiMon.setOnClickListener(this);
        viewHolderBanAn.imThanhToan.setOnClickListener(this);
        viewHolderBanAn.imAnButton.setOnClickListener(this);
        return view;
    }

    private void HienThiButton() {
        viewHolderBanAn.imGoiMon.setVisibility(View.VISIBLE);
        viewHolderBanAn.imThanhToan.setVisibility(View.VISIBLE);
        viewHolderBanAn.imAnButton.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.hieuung_hienthi_button_banan);
        viewHolderBanAn.imGoiMon.startAnimation(animation);
        viewHolderBanAn.imThanhToan.startAnimation(animation);
        viewHolderBanAn.imAnButton.startAnimation(animation);
    }

    private void AnButton(boolean hieuung) {
        if (hieuung) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.hieuung_anbutton_banan);
            viewHolderBanAn.imGoiMon.startAnimation(animation);
            viewHolderBanAn.imThanhToan.startAnimation(animation);
            viewHolderBanAn.imAnButton.startAnimation(animation);
        }
        viewHolderBanAn.imGoiMon.setVisibility(View.INVISIBLE);
        viewHolderBanAn.imThanhToan.setVisibility(View.INVISIBLE);
        viewHolderBanAn.imAnButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        viewHolderBanAn = (ViewHolderBanAn) ((View) v.getParent()).getTag();
        int vitri1 = (int) viewHolderBanAn.imBanAn.getTag();
        int maban = banAnDTOList.get(vitri1).getMaBan();
        switch (id) {
            case R.id.imBanAn:
                String tenban = viewHolderBanAn.txtTenBanAn.getText().toString();
                int vitri = (int) v.getTag();
                banAnDTOList.get(vitri).setDuocChon(true);
                HienThiButton();
                break;
            case R.id.imGoiMon:
                Intent layITrangChu = ((TrangChuActivity) context).getIntent();
                int manhanvien = layITrangChu.getIntExtra("manhanvien", 0);
                String tinhtrang = banAnDAO.LayTinhTrangBanTheoMa(maban);
                if (tinhtrang.equals("false")) {
                    GoiMonDTO goiMonDTO = new GoiMonDTO();
                    goiMonDTO.setMaBan(maban);
                    goiMonDTO.setMaNV(manhanvien);
                    goiMonDTO.setTinhTrang("false");
                    long kiemtra = goiMonDAO.ThemGoiMon(goiMonDTO);
                    banAnDAO.CapNhatLaiTinhTrangBan(maban, "true");
                    if (kiemtra == 0) {
                        Toast.makeText(context, context.getResources().getString(R.string.themthatbai), Toast.LENGTH_SHORT).show();
                    }

                }
                FragmentTransaction tranThucDonTransaction = fragmentManager.beginTransaction();
                HienThiThucDonFragment hienThiThucDonFragment = new HienThiThucDonFragment();

                Bundle bDuLieuThucDon = new Bundle();
                bDuLieuThucDon.putInt("maban", maban);
                hienThiThucDonFragment.setArguments(bDuLieuThucDon);
                tranThucDonTransaction.replace(R.id.content, hienThiThucDonFragment).addToBackStack("hienthibanan");
                tranThucDonTransaction.commit();
                break;
            case R.id.imThanhToan:
                Intent iThanhToan = new Intent(context, ThanhToanActivity.class);
                iThanhToan.putExtra("maban", maban);
                context.startActivity(iThanhToan);
                break;
            case R.id.imAnButton:
                AnButton(true);
                break;
        }
    }
}
