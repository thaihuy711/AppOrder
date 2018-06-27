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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.thaihuy.order.Adapter.AdapterHienThiBanAn;
import com.thaihuy.order.DAO.BanAnDAO;
import com.thaihuy.order.DTO.BanAnDTO;
import com.thaihuy.order.R;
import com.thaihuy.order.SuaBanAnActivity;
import com.thaihuy.order.ThemBanAnActivity;
import com.thaihuy.order.TrangChuActivity;

import java.util.List;

public class HienThiBanAnFagment extends Fragment {

    public static int RESQUEST_CODE_THEM = 111;
    public static int RESQUEST_CODE_SUA = 16;

    GridView gvHienThiBanAn;
    List<BanAnDTO> banAnDTOList;
    BanAnDAO banAnDAO;
    AdapterHienThiBanAn adapterHienThiBanAn;
    int maquyen = 0;
    SharedPreferences sharedPreferences;
    ViewFlipper viewFlipper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_hienthibanan, container, false);
        setHasOptionsMenu(true);
        ((TrangChuActivity) getActivity()).getSupportActionBar().setTitle(R.string.trangchu);

        gvHienThiBanAn = view.findViewById(R.id.gvHienBanAn);
        viewFlipper = view.findViewById(R.id.viewflipper);
        sharedPreferences = getActivity().getSharedPreferences("luuquyen", Context.MODE_PRIVATE);
        maquyen = sharedPreferences.getInt("maquyen", 0);
        banAnDAO = new BanAnDAO(getActivity());
        banAnDTOList = banAnDAO.LayTatCaBanAn();
        ActionViewFlipper();
        HienThiDanhSachBanAn();
        if (maquyen == 1) {
            registerForContextMenu(gvHienThiBanAn);
        }
        return view;
    }

    private void ActionViewFlipper() {
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        int maban = banAnDTOList.get(vitri).getMaBan();
        switch (id) {
            case R.id.itSua:
                Intent intent = new Intent(getActivity(), SuaBanAnActivity.class);
                intent.putExtra("maban", maban);
                startActivityForResult(intent, RESQUEST_CODE_SUA);
                break;
            case R.id.itXoa:
                boolean kiemtra = banAnDAO.XoaBanAnTheoMa(maban);
                if (kiemtra) {
                    HienThiDanhSachBanAn();
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.xoathanhcong), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.loi) + maban, Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (maquyen == 1) {
            //là quản lý
            MenuItem itThemBanAn = menu.add(1, R.id.itThemBanAn, 1, R.string.thembanan);
            itThemBanAn.setIcon(R.drawable.ic_add);
            itThemBanAn.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itThemBanAn:
                Intent iThemBanAn = new Intent(getActivity(), ThemBanAnActivity.class);
                startActivityForResult(iThemBanAn, RESQUEST_CODE_THEM);
                break;
        }
        return true;
    }

    private void HienThiDanhSachBanAn() {
        banAnDTOList = banAnDAO.LayTatCaBanAn();
        adapterHienThiBanAn = new AdapterHienThiBanAn(getActivity(), R.layout.custom_layout_hienthibanan, banAnDTOList);
        gvHienThiBanAn.setAdapter(adapterHienThiBanAn);
        adapterHienThiBanAn.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESQUEST_CODE_THEM) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = data;
                boolean kiemtra = intent.getBooleanExtra("ketquathem", false);
                if (kiemtra) {
                    HienThiDanhSachBanAn();
                    Toast.makeText(getActivity(), getResources().getString(R.string.themthanhcong), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.themthatbai), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == RESQUEST_CODE_SUA) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = data;
                boolean kiemtra = intent.getBooleanExtra("kiemtra", false);
                HienThiDanhSachBanAn();
                if (kiemtra) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.suathanhcong), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.loi), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
