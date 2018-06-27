package com.thaihuy.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thaihuy.order.DAO.MonAnDAO;

public class SuaMonAnActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edSuaTenMon, edSuaGia;
    Button btnDongY;
    MonAnDAO monAnDAO;
    int mamonan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_suamonan);
        initView();
        mamonan = getIntent().getIntExtra("mamonan", 0);
    }

    private void initView() {
        edSuaTenMon = findViewById(R.id.edSuaTenMonAn);
        edSuaGia = findViewById(R.id.edSuaGiaMonAn);
        btnDongY = findViewById(R.id.btnDongYSuaMonAn);
        btnDongY.setOnClickListener(this);
        monAnDAO = new MonAnDAO(this);
    }

    @Override
    public void onClick(View view) {
        String tenmonan = edSuaTenMon.getText().toString();
        String gia = (edSuaGia.getText().toString());
        if (tenmonan.trim().equals("") || tenmonan.trim() != null && gia.trim().equals("") || gia.trim() != null) {
            boolean kiemtra = monAnDAO.CapNhatLaiMonAn(mamonan, tenmonan, gia);
            Intent intent = new Intent();
            intent.putExtra("kiemtra", kiemtra);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(SuaMonAnActivity.this, getResources().getString(R.string.vuilongnhapdulieu), Toast.LENGTH_SHORT).show();
        }
    }
}
