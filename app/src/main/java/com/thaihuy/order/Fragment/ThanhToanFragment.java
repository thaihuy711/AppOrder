package com.thaihuy.order.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.thaihuy.order.Adapter.AdapterHienThiThanhToan;
import com.thaihuy.order.DAO.BanAnDAO;
import com.thaihuy.order.DAO.GoiMonDAO;
import com.thaihuy.order.DTO.ThanhToanDTO;
import com.thaihuy.order.R;

import java.util.List;

public class ThanhToanFragment extends Fragment implements View.OnClickListener {
    GridView gridView;
    Button btnThanhToan, btnThoat;
    TextView txtTongTien;
    GoiMonDAO goiMonDAO;
    List<ThanhToanDTO> thanhToanDTOList;
    AdapterHienThiThanhToan adapterHienThiThanhToan;
    long tongtien = 0;
    BanAnDAO banAnDAO;
    int maban = 0;
    FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_thanhtoan, container, false);
        gridView = view.findViewById(R.id.gvThanhToan);
        btnThanhToan = view.findViewById(R.id.btnThanhToan);
        btnThoat = view.findViewById(R.id.btnThoatThanhToan);
        txtTongTien = view.findViewById(R.id.txtTongTien);
        goiMonDAO = new GoiMonDAO(getActivity());
        banAnDAO = new BanAnDAO(getActivity());
        fragmentManager = getActivity().getSupportFragmentManager();
        maban = getActivity().getIntent().getIntExtra("maban", 0);
        if (maban != 0) {
            HienThiThanhToan();
            for (int i = 0; i < thanhToanDTOList.size(); i++) {
                int soluong = thanhToanDTOList.get(i).getSoLuong();
                int giatien = thanhToanDTOList.get(i).getGiaTien();
                tongtien += (soluong * giatien);
            }
            txtTongTien.setText(getResources().getString(R.string.tongcong) + tongtien);
        }
        btnThanhToan.setOnClickListener(this);
        btnThoat.setOnClickListener(this);
        return view;
    }

    private void HienThiThanhToan() {
        int magoimon = (int) goiMonDAO.LayMaGoiMonTheoMaBan(maban, "false");
        thanhToanDTOList = goiMonDAO.LayDanhSachMonAnTheoMaGoiMon(magoimon);
        adapterHienThiThanhToan = new AdapterHienThiThanhToan(getActivity(), R.layout.custom_layout_thanhtoan, thanhToanDTOList);
        gridView.setAdapter(adapterHienThiThanhToan);
        adapterHienThiThanhToan.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnThanhToan:
                boolean kiemtrabanan = banAnDAO.CapNhatLaiTinhTrangBan(maban, "false");
                boolean kiemtragoimon = goiMonDAO.CapNhatTrangThaiGoiMonTheoMaBan(maban, "true");
                if (kiemtrabanan && kiemtragoimon) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.thanhtoanthanhcong), Toast.LENGTH_SHORT);
                    HienThiThanhToan();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.loi), Toast.LENGTH_SHORT);
                }
                break;
            case R.id.btnThoatThanhToan:
                getActivity().finish();
                break;
        }
    }
}
