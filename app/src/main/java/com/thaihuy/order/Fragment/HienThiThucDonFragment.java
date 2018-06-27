package com.thaihuy.order.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.thaihuy.order.Adapter.AdapdaterHienThiLoaiMonAnThucDon;
import com.thaihuy.order.DAO.LoaiMonAnDAO;
import com.thaihuy.order.DTO.LoaiMonAnDTO;
import com.thaihuy.order.R;
import com.thaihuy.order.TrangChuActivity;

import java.util.List;

public class HienThiThucDonFragment extends Fragment {

    GridView gridView;
    List<LoaiMonAnDTO> loaiMonAnDTOs;
    LoaiMonAnDAO loaiMonAnDAO;
    FragmentManager fragmentManager;
    int maban;
    int maquyen;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_hienthithucdon, container, false);
        setHasOptionsMenu(true);
        ((TrangChuActivity) getActivity()).getSupportActionBar().setTitle(R.string.thucdon);
        gridView = view.findViewById(R.id.gvHienThiThucDon);
        fragmentManager = getActivity().getSupportFragmentManager();
        loaiMonAnDAO = new LoaiMonAnDAO(getActivity());
        HienThiDSLoaiMonAn();
        sharedPreferences = getActivity().getSharedPreferences("luuquyen", Context.MODE_PRIVATE);
        maquyen = sharedPreferences.getInt("maquyen", 0);
        if (maquyen == 1) {
            registerForContextMenu(gridView);
        }
        Bundle bDuLieuThucDon = getArguments();
        if (bDuLieuThucDon != null) {
            maban = bDuLieuThucDon.getInt("maban");
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int maloai = loaiMonAnDTOs.get(position).getMaLoai();
                HienThiDanhSachMonAnFragment hienThiDanhSachMonAnFragment = new HienThiDanhSachMonAnFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("maloai", maloai);
                bundle.putInt("maban", maban);
                hienThiDanhSachMonAnFragment.setArguments(bundle);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content, hienThiDanhSachMonAnFragment).addToBackStack("hienthiloai");
                transaction.commit();
            }
        });
        return view;
    }

    private void HienThiDSLoaiMonAn() {
        loaiMonAnDTOs = loaiMonAnDAO.LayDanhSachLoaiMonAn();
        AdapdaterHienThiLoaiMonAnThucDon adapdater = new AdapdaterHienThiLoaiMonAnThucDon(getActivity(), R.layout.custom_layout_hienloaimonan, loaiMonAnDTOs);
        gridView.setAdapter(adapdater);
        adapdater.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (maquyen == 1) {
            MenuItem itThemBanAn = menu.add(1, R.id.itThemThucDon, 1, R.string.themthucdon);
            itThemBanAn.setIcon(R.drawable.ic_add);
            itThemBanAn.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itThemThucDon:
                ThemThucDonFragment themThucDonFragment = new ThemThucDonFragment();
                FragmentTransaction tranThemThucDon = fragmentManager.beginTransaction();
                tranThemThucDon.setCustomAnimations(R.anim.hieuung_activity_vao, R.anim.hieuung_activity_ra);
                tranThemThucDon.replace(R.id.content, themThucDonFragment);
                tranThemThucDon.addToBackStack(null);
                tranThemThucDon.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.context_menu_loai_mon_an, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        int mamonan = loaiMonAnDTOs.get(vitri).getMaLoai();
        switch (item.getItemId()) {
            case R.id.itXoa:
                boolean kiemtra = loaiMonAnDAO.XoaLoaiMonAn(mamonan);
                if (kiemtra) {
                    HienThiDSLoaiMonAn();
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.xoathanhcong), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.loi), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }
}
