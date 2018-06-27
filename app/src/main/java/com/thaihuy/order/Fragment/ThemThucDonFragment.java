package com.thaihuy.order.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.thaihuy.order.R;
import com.thaihuy.order.ThemLoaiThucDonActivity;
import com.thaihuy.order.TrangChuActivity;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class ThemThucDonFragment extends Fragment implements View.OnClickListener {

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
    EditText edTenMonAn, edGiaTien;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_themthucdon, container, false);
        ((TrangChuActivity)getActivity()).getSupportActionBar().setTitle(R.string.themthucdon);
        loaiMonAnDAO = new LoaiMonAnDAO(getActivity());
        monAnDAO = new MonAnDAO(getActivity());
        imThemLoaiThucDon = view.findViewById(R.id.imThemLoaiThucDon);
        spinLoaiThucDon = view.findViewById(R.id.spinLoaiMonAn);
        imHinhThucDon = view.findViewById(R.id.imHinhThucDon);
        btnDongYThemMonAn = view.findViewById(R.id.btnDongYThemMonAn);
        btnThoatThemMonAn = view.findViewById(R.id.btnThoatThemMonAn);
        edTenMonAn = view.findViewById(R.id.edThemTenMonAn);
        edGiaTien = view.findViewById(R.id.edThemGiaTien);
        HienThiSpinnerLoaiMonAn();
        imThemLoaiThucDon.setOnClickListener(this);
        imHinhThucDon.setOnClickListener(this);
        btnDongYThemMonAn.setOnClickListener(this);
        btnThoatThemMonAn.setOnClickListener(this);

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    getFragmentManager().popBackStack("hienthiloai", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                return false;
            }
        });
        return view;
    }

    private void HienThiSpinnerLoaiMonAn() {
        loaiMonAnDTOs = loaiMonAnDAO.LayDanhSachLoaiMonAn();
        adapterHienThiLoaiMonAn = new AdapterHienThiLoaiMonAn(getActivity(), R.layout.custom_layout_spinloaithucdon, loaiMonAnDTOs);
        spinLoaiThucDon.setAdapter(adapterHienThiLoaiMonAn);
        adapterHienThiLoaiMonAn.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.imThemLoaiThucDon:
                Intent iThemLoaiMonAn = new Intent(getActivity(), ThemLoaiThucDonActivity.class);
                startActivityForResult(iThemLoaiMonAn, REQUEST_CODE_THEMLOAITHUCDON);
                break;
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
                if (tenmonan != null && giatien != null && !tenmonan.equals("") && !giatien.equals("")) {
                    MonAnDTO monAnDTO = new MonAnDTO();
                    monAnDTO.setGiaTien(giatien);
                    monAnDTO.setHinhAnh(sDuongDanHinh);
                    monAnDTO.setMaLoai(maloai);
                    monAnDTO.setTenMonAn(tenmonan);
                    boolean kiemtra = monAnDAO.ThemMonAn(monAnDTO);
                    if (kiemtra) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.themthanhcong), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.themthatbai), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.loithemmonan), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnThoatThemMonAn:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content,new HienThiThucDonFragment()).addToBackStack("abc").commit();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_THEMLOAITHUCDON){
            if(resultCode == Activity.RESULT_OK){
                Intent dulieu = data;
                boolean kiemtra = dulieu.getBooleanExtra("kiemtraloaithucdon",false);
                if(kiemtra){
                    HienThiSpinnerLoaiMonAn();
                    Toast.makeText(getActivity(), getResources().getString(R.string.themthanhcong), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),getResources().getString(R.string.themthatbai), Toast.LENGTH_SHORT).show();
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
