package com.thaihuy.order.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.thaihuy.order.Adapter.AdapterHienThiDanhSachMonAn;
import com.thaihuy.order.DAO.MonAnDAO;
import com.thaihuy.order.DTO.MonAnDTO;
import com.thaihuy.order.R;
import com.thaihuy.order.SoLuongActivity;
import com.thaihuy.order.SuaMonAnActivity;
import com.thaihuy.order.TrangChuActivity;

import java.util.List;

public class HienThiDanhSachMonAnFragment extends Fragment {
    public static int REQUEST_CODE_SUA = 10;
    GridView gridView;
    MonAnDAO monAnDAO;
    List<MonAnDTO> monAnDTOList;
    AdapterHienThiDanhSachMonAn adapterHienThiDanhSachMonAn;
    int maban, maloai;
    int maquyen = 0;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_hienthidsmonan, container, false);
        gridView = view.findViewById(R.id.gvHienThiDSThucDon);
        ((TrangChuActivity) getActivity()).getSupportActionBar().setTitle(R.string.danhsachmonan);
        monAnDAO = new MonAnDAO(getActivity());
        Bundle bundle = getArguments();
        sharedPreferences = getActivity().getSharedPreferences("luuquyen", Context.MODE_PRIVATE);
        maquyen = sharedPreferences.getInt("maquyen", 0);
        if (bundle != null) {
            maloai = bundle.getInt("maloai");
            maban = bundle.getInt("maban");
            HienThiDSMonAn();
            if (maquyen == 1) {
                registerForContextMenu(gridView);
            }
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (maban != 0) {
                        Intent iSoLuong = new Intent(getActivity(), SoLuongActivity.class);
                        iSoLuong.putExtra("maban", maban);
                        iSoLuong.putExtra("mamonan", monAnDTOList.get(position).getMaMonAn());
                        startActivity(iSoLuong);
                    }
                }
            });
        }
        return view;
    }

    public void HienThiDSMonAn() {
        monAnDTOList = monAnDAO.LayDanhSachMonAnTheoLoai(maloai);
        adapterHienThiDanhSachMonAn = new AdapterHienThiDanhSachMonAn(getActivity(), R.layout.custom_layout_hienthidanhsachmonan, monAnDTOList);
        gridView.setAdapter(adapterHienThiDanhSachMonAn);
        adapterHienThiDanhSachMonAn.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        int mamonan = monAnDTOList.get(vitri).getMaMonAn();
        switch (item.getItemId()) {
            case R.id.itSua:
                Intent intent = new Intent(getActivity(), SuaMonAnActivity.class);
                intent.putExtra("mamonan", mamonan);
                startActivityForResult(intent, REQUEST_CODE_SUA);
                break;
            case R.id.itXoa:
                boolean kiemtra = monAnDAO.XoaMonAn(mamonan);
                if (kiemtra) {
                    HienThiDSMonAn();
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.xoathanhcong), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.loi), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SUA) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = data;
                boolean kiemtra = intent.getBooleanExtra("kiemtra", false);
                if (kiemtra) {
                    HienThiDSMonAn();
                    Toast.makeText(getActivity(), getResources().getString(R.string.suathanhcong), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.loi), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
