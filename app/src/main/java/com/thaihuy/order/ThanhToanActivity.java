package com.thaihuy.order;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import com.thaihuy.order.Adapter.AdapterHienThiThanhToan;
import com.thaihuy.order.DAO.BanAnDAO;
import com.thaihuy.order.DAO.GoiMonDAO;
import com.thaihuy.order.DTO.ThanhToanDTO;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class ThanhToanActivity extends AppCompatActivity implements View.OnClickListener {

    GridView gridView;
    FancyButton btnThanhToan, btnThoat;
    TextView txtTongTien;
    GoiMonDAO goiMonDAO;
    List<ThanhToanDTO> thanhToanDTOList;
    AdapterHienThiThanhToan adapterHienThiThanhToan;
    long tongtien = 0;
    BanAnDAO banAnDAO;
    int maban = 0;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_thanhtoan);

        gridView = findViewById(R.id.gvThanhToan);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        btnThoat = findViewById(R.id.btnThoatThanhToan);
        txtTongTien = findViewById(R.id.txtTongTien);
        goiMonDAO = new GoiMonDAO(this);
        banAnDAO = new BanAnDAO(this);
        fragmentManager = getSupportFragmentManager();

        maban = getIntent().getIntExtra("maban", 0);
        if (maban != 0) {
            HienThiThanhToan();

            for (int i = 0; i < thanhToanDTOList.size(); i++) {
                int soluong = thanhToanDTOList.get(i).getSoLuong();
                int giatien = thanhToanDTOList.get(i).getGiaTien();
                tongtien += (soluong * giatien); // tongtien = tongtien + (soluong*giatien)
            }
            txtTongTien.setText(getResources().getString(R.string.tongcong) + tongtien);
        }
        btnThanhToan.setOnClickListener(this);
        btnThoat.setOnClickListener(this);
    }

    private void HienThiThanhToan() {
        int magoimon = (int) goiMonDAO.LayMaGoiMonTheoMaBan(maban, "false");
        thanhToanDTOList = goiMonDAO.LayDanhSachMonAnTheoMaGoiMon(magoimon);
        adapterHienThiThanhToan = new AdapterHienThiThanhToan(this, R.layout.custom_layout_thanhtoan, thanhToanDTOList);
        gridView.setAdapter(adapterHienThiThanhToan);
        adapterHienThiThanhToan.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnThanhToan:
                boolean kiemtrabanan = banAnDAO.CapNhatLaiTinhTrangBan(maban, "false");
                boolean kiemtragoimon = goiMonDAO.CapNhatTrangThaiGoiMonTheoMaBan(maban, "true");
                if (kiemtrabanan && kiemtragoimon) {
                    Toast.makeText(ThanhToanActivity.this, getResources().getString(R.string.thanhtoanthanhcong), Toast.LENGTH_SHORT);
                    HienThiThanhToan();
                } else {
                    Toast.makeText(ThanhToanActivity.this, getResources().getString(R.string.loi), Toast.LENGTH_SHORT);
                }
                break;
            case R.id.btnThoatThanhToan:
                finish();
                break;
        }
    }
}
