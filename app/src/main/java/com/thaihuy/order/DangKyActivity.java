package com.thaihuy.order;

import android.annotation.TargetApi;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thaihuy.order.DAO.NhanVienDAO;
import com.thaihuy.order.DAO.QuyenDAO;
import com.thaihuy.order.DTO.NhanVienDTO;
import com.thaihuy.order.DTO.QuyenDTO;
import com.thaihuy.order.Fragment.DatePickerFragment;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class DangKyActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    EditText edTenDangNhapDK, edMatKhauDK, edNgaySinhDK, edSDT;
    FancyButton btnDongYDK;
    Spinner spinQuyen;
    NhanVienDAO nhanVienDAO;
    QuyenDAO quyenDAO;
    int manv = 0;
    int landautien = 0;
    List<QuyenDTO> quyenDTOList;
    List<String> dataAdapter;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dangky);
        initView();
        quyenDTOList = quyenDAO.LayDanhSachQuyen();
        dataAdapter = new ArrayList<String>();
        for (int i = 0; i < quyenDTOList.size(); i++) {
            String tenquyen = quyenDTOList.get(i).getTenQuyen();
            dataAdapter.add(tenquyen);
        }
        manv = getIntent().getIntExtra("manv", 0);
        landautien = getIntent().getIntExtra("landautien", 0);
        if (landautien == 0) {
            //neu khong phai lan dau tien
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataAdapter);
            spinQuyen.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            quyenDAO.ThemQuyen("Quản lý");
            quyenDAO.ThemQuyen("Nhân viên");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataAdapter);
            spinQuyen.setVisibility(View.GONE);
            spinQuyen.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        if (manv != 0) {
            NhanVienDTO nhanVienDTO = nhanVienDAO.LayDanhSachNhanVienTheoMa(manv);
            edTenDangNhapDK.setText(nhanVienDTO.getTENDN());
            edMatKhauDK.setText(nhanVienDTO.getMATKHAU());
            edNgaySinhDK.setText(nhanVienDTO.getNGAYSINH());
            edSDT.setText(String.valueOf(nhanVienDTO.getSDT()));
            edNgaySinhDK.setText(nhanVienDTO.getNGAYSINH());
        }
    }

    private void initView() {
        edTenDangNhapDK = findViewById(R.id.edTenDangNhapDK);
        edMatKhauDK = findViewById(R.id.edMatKhauDK);
        edNgaySinhDK = findViewById(R.id.edNgaySinhDK);
        edSDT = findViewById(R.id.edSDT);
        btnDongYDK = findViewById(R.id.btnDongYDK);
        spinQuyen = findViewById(R.id.spinQuyen);
        btnDongYDK.setOnClickListener(this);
        edNgaySinhDK.setOnFocusChangeListener(this);
        nhanVienDAO = new NhanVienDAO(this);
        quyenDAO = new QuyenDAO(this);
    }

    private void DongYThemNhanVien() {
        String sTenDangNhap = edTenDangNhapDK.getText().toString();
        String sMatKhau = edMatKhauDK.getText().toString();
        String sNgaySinh = edNgaySinhDK.getText().toString();
        int sSDT = Integer.parseInt(edSDT.getText().toString());

        if (sTenDangNhap == null || sTenDangNhap.equals("")) {
            Toast.makeText(DangKyActivity.this, getResources().getString(R.string.loitendangnhap), Toast.LENGTH_SHORT).show();
        } else if (sMatKhau == null || sMatKhau.equals("")) {
            Toast.makeText(DangKyActivity.this, getResources().getString(R.string.loimatkhau), Toast.LENGTH_SHORT).show();
        } else {
            NhanVienDTO nhanVienDTO = new NhanVienDTO();
            nhanVienDTO.setTENDN(sTenDangNhap);
            nhanVienDTO.setMATKHAU(sMatKhau);
            nhanVienDTO.setSDT(sSDT);
            nhanVienDTO.setNGAYSINH(sNgaySinh);
            if (landautien != 0) {
                nhanVienDTO.setMAQUYEN(1);
            } else {
                int vitri = spinQuyen.getSelectedItemPosition();
                int maquyen = quyenDTOList.get(vitri).getMaQuyen();
                nhanVienDTO.setMAQUYEN(maquyen);
            }

            long kiemtra = nhanVienDAO.ThemNhanVien(nhanVienDTO);
            if (kiemtra != 0) {
                Toast.makeText(DangKyActivity.this, getResources().getString(R.string.themthanhcong), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DangKyActivity.this, getResources().getString(R.string.themthatbai), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SuaNhanVien() {
        String sTenDangNhap = edTenDangNhapDK.getText().toString();
        String sMatKhau = edMatKhauDK.getText().toString();
        String sNgaySinh = edNgaySinhDK.getText().toString();
        int sSDT = Integer.parseInt(edSDT.getText().toString());
        NhanVienDTO nhanVienDTO = new NhanVienDTO();
        nhanVienDTO.setMANV(manv);
        nhanVienDTO.setTENDN(sTenDangNhap);
        nhanVienDTO.setMATKHAU(sMatKhau);
        nhanVienDTO.setSDT(sSDT);
        nhanVienDTO.setNGAYSINH(sNgaySinh);

        boolean kiemtra = nhanVienDAO.SuaNhanVien(nhanVienDTO);
        if (kiemtra) {
            Toast.makeText(DangKyActivity.this, getResources().getString(R.string.suathanhcong), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(DangKyActivity.this, getResources().getString(R.string.loi), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnDongYDK:
                if (manv != 0) {
                    SuaNhanVien();
                } else {
                    DongYThemNhanVien();
                }

                break;

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        switch (id) {
            case R.id.edNgaySinhDK:
                if (hasFocus) {
                    DatePickerFragment datePickerFragment = new DatePickerFragment();
                    datePickerFragment.show(getSupportFragmentManager(), "Ngày Sinh");
                }
                break;
        }
    }
}
