package com.thaihuy.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.thaihuy.order.Fragment.HienThiBanAnFagment;
import com.thaihuy.order.Fragment.HienThiNhanVienFragment;
import com.thaihuy.order.Fragment.HienThiThucDonFragment;


public class TrangChuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView txtTenNhanVien_Navigation;
    FragmentManager fragmentManager;
    BottomNavigationView bottomNavigationView;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_trangchu);
        drawerLayout =  findViewById(R.id.drawerLayout);
        navigationView =  findViewById(R.id.navigationview_trangchu);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.botNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        View view = navigationView.inflateHeaderView(R.layout.layout_header_navigation_trangchu);
        txtTenNhanVien_Navigation = view.findViewById(R.id.txtTenNhanVien_Navigation);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.mo, R.string.dong) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorWhite));
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        String tendn = intent.getStringExtra("tendn");
        txtTenNhanVien_Navigation.setText(tendn);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction tranHienThiBanAn = fragmentManager.beginTransaction();
        HienThiBanAnFagment hienThiBanAnFagment = new HienThiBanAnFagment();
        tranHienThiBanAn.replace(R.id.content, hienThiBanAnFagment);
        tranHienThiBanAn.commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.itHome:
                    HienThiBanAnFagment hienThiBanAnFragment = new HienThiBanAnFagment();
                    FragmentTransaction tranHienThiBanAn = fragmentManager.beginTransaction();
                    tranHienThiBanAn.replace(R.id.content, hienThiBanAnFragment);
                    tranHienThiBanAn.commit();
                    return true;
                case R.id.itMenu:
                    HienThiThucDonFragment hienThiThucDonFragment = new HienThiThucDonFragment();
                    FragmentTransaction tranHienThiThucDon = fragmentManager.beginTransaction();
                    tranHienThiThucDon.setCustomAnimations(R.anim.hieuung_activity_vao, R.anim.hieuung_activity_ra);
                    tranHienThiThucDon.replace(R.id.content, hienThiThucDonFragment);
                    tranHienThiThucDon.commit();
                    return true;
                case R.id.itMember:
                    HienThiNhanVienFragment hienThiNhanVienFragment = new HienThiNhanVienFragment();
                    FragmentTransaction tranHienThiNhanVien = fragmentManager.beginTransaction();
                    tranHienThiNhanVien.replace(R.id.content, hienThiNhanVienFragment);
                    tranHienThiNhanVien.commit();
                    return true;
            }
            return true;
        }
    };

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itHome:
                FragmentTransaction tranHienThiBanAn = fragmentManager.beginTransaction();
                HienThiBanAnFagment hienThiBanAnFagment = new HienThiBanAnFagment();
                tranHienThiBanAn.setCustomAnimations(R.anim.hieuung_activity_vao, R.anim.hieuung_activity_ra);
                tranHienThiBanAn.replace(R.id.content, hienThiBanAnFagment);
                tranHienThiBanAn.commit();
                item.setChecked(true);
                drawerLayout.closeDrawers();
                break;
            case R.id.itMenu:
                FragmentTransaction tranHienThiThucDon = fragmentManager.beginTransaction();
                HienThiThucDonFragment hienThiThucDonFragment = new HienThiThucDonFragment();
                tranHienThiThucDon.setCustomAnimations(R.anim.hieuung_activity_vao, R.anim.hieuung_activity_ra);
                tranHienThiThucDon.replace(R.id.content, hienThiThucDonFragment);
                tranHienThiThucDon.commit();
                item.setChecked(true);
                drawerLayout.closeDrawers();
                break;
            case R.id.itMember:
                FragmentTransaction tranNhanVien = fragmentManager.beginTransaction();
                HienThiNhanVienFragment hienThiNhanVienFragment = new HienThiNhanVienFragment();
                tranNhanVien.setCustomAnimations(R.anim.hieuung_activity_vao, R.anim.hieuung_activity_ra);
                tranNhanVien.replace(R.id.content, hienThiNhanVienFragment);
                tranNhanVien.commit();
                item.setChecked(true);
                drawerLayout.closeDrawers();
                break;
            case R.id.itLogout:
                Intent intent = new Intent(TrangChuActivity.this,DangNhapActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }
}
