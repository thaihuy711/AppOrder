package com.thaihuy.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.thaihuy.order.Adapter.AdapterHienThiLoaiMonAn;
import com.thaihuy.order.DAO.LoaiMonAnDAO;
import com.thaihuy.order.DAO.MonAnDAO;
import com.thaihuy.order.DTO.LoaiMonAnDTO;
import com.thaihuy.order.DTO.MonAnDTO;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class ThemThucDonActivity extends AppCompatActivity implements View.OnClickListener {

    public static int REQUEST_CODE_THEMLOAITHUCDON = 113;
    public static int REQUEST_CODE_MOHINH = 123;
    ImageButton imThemLoaiThucDon;
    Spinner spinLoaiThucDon;
    LoaiMonAnDAO loaiMonAnDAO;
    MonAnDAO monAnDAO;
    List<LoaiMonAnDTO> loaiMonAnDTOs;
    AdapterHienThiLoaiMonAn adapterHienThiLoaiMonAn;
    ImageView imHinhThucDon;
    FancyButton btnDongYThemMonAn, btnThoatThemMonAn;
    String sDuongDanHinh;
    EditText edTenMonAn,edGiaTien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_themthucdon);
        loaiMonAnDAO = new LoaiMonAnDAO(this);
        monAnDAO = new MonAnDAO(this);
        imThemLoaiThucDon =  findViewById(R.id.imThemLoaiThucDon);
        spinLoaiThucDon =  findViewById(R.id.spinLoaiMonAn);
        imHinhThucDon = findViewById(R.id.imHinhThucDon);
        btnDongYThemMonAn =  findViewById(R.id.btnDongYThemMonAn);
        btnThoatThemMonAn =  findViewById(R.id.btnThoatThemMonAn);
        edTenMonAn =  findViewById(R.id.edThemTenMonAn);
        edGiaTien =  findViewById(R.id.edThemGiaTien);
        HienThiSpinnerLoaiMonAn();
        imThemLoaiThucDon.setOnClickListener(this);
        imHinhThucDon.setOnClickListener(this);
        btnDongYThemMonAn.setOnClickListener(this);
        btnThoatThemMonAn.setOnClickListener(this);
    }

    private void HienThiSpinnerLoaiMonAn (){
        loaiMonAnDTOs = loaiMonAnDAO.LayDanhSachLoaiMonAn();
        adapterHienThiLoaiMonAn = new AdapterHienThiLoaiMonAn(ThemThucDonActivity.this,R.layout.custom_layout_spinloaithucdon,loaiMonAnDTOs);
        spinLoaiThucDon.setAdapter(adapterHienThiLoaiMonAn);
        adapterHienThiLoaiMonAn.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.imThemLoaiThucDon:
                Intent iThemLoaiMonAn = new Intent(ThemThucDonActivity.this,ThemLoaiThucDonActivity.class);
                startActivityForResult(iThemLoaiMonAn,REQUEST_CODE_THEMLOAITHUCDON);break;
            case R.id.imHinhThucDon:
                Intent iMoHinh = new Intent();
                iMoHinh.setType("image/*");
                iMoHinh.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(iMoHinh, "Chọn hình thực đơn"), REQUEST_CODE_MOHINH);
                break;
            case R.id.btnDongYThemMonAn:
                int vitri = spinLoaiThucDon.getSelectedItemPosition();
                int maloai = loaiMonAnDTOs.get(vitri).getMaLoai();
                String tenmonan = edTenMonAn.getText().toString();
                String giatien = edGiaTien.getText().toString();
                if(tenmonan != null && giatien != null && !tenmonan.equals("") && !giatien.equals("") ){
                    MonAnDTO monAnDTO = new MonAnDTO();
                    monAnDTO.setGiaTien(giatien);
                    monAnDTO.setHinhAnh(sDuongDanHinh);
                    monAnDTO.setMaLoai(maloai);
                    monAnDTO.setTenMonAn(tenmonan);

                    boolean kiemtra = monAnDAO.ThemMonAn(monAnDTO);
                    if(kiemtra){
                        Toast.makeText(this,getResources().getString(R.string.themthanhcong), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this,getResources().getString(R.string.themthatbai), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,getResources().getString(R.string.loithemmonan), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnThoatThemMonAn:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_THEMLOAITHUCDON){
            if(resultCode == Activity.RESULT_OK){
                Intent dulieu = data;
                boolean kiemtra = dulieu.getBooleanExtra("kiemtraloaithucdon",false);
                if(kiemtra){
                    HienThiSpinnerLoaiMonAn();
                    Toast.makeText(this, getResources().getString(R.string.themthanhcong), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,getResources().getString(R.string.themthatbai), Toast.LENGTH_SHORT).show();
                }
            }
        }else if(REQUEST_CODE_MOHINH == requestCode){
            if(resultCode == Activity.RESULT_OK && data !=null){
                sDuongDanHinh = data.getData().toString();
                imHinhThucDon.setImageURI(data.getData());
            }
        }
    }
}
