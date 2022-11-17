package com.unir.projetocep.encapsulamento;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unir.projetocep.Permissao;
import com.unir.projetocep.R;
import com.unir.projetocep.databinding.ActivityMapsBinding;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String[] permissoes = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String endereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Permissao.validarPermissoes(permissoes, this, 1);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        endereco = intent.getStringExtra("endereco");
        Log.i("endereco", endereco);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent intent = getIntent();
        String endereco = intent.getStringExtra("endereco");

        LatLng posicao = determineLocal(getApplicationContext(), endereco);
        if (posicao != null) {
            mMap.addMarker(new MarkerOptions().position(posicao).title("Endereço"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicao, 16));
        } else {
            Toast.makeText(this, "Endereço invalido", Toast.LENGTH_SHORT).show();
        }
        pedirPermissao();
    }

    public LatLng determineLocal(Context context, String ender) {
        LatLng latLng = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> geoResults = null;

        try {
            geoResults = geocoder.getFromLocationName(ender, 1);
            while (geoResults.size() == 0) {
                geoResults = geocoder.getFromLocationName(ender, 1);
            }
            if (geoResults.size() > 0) {
                Address addr = geoResults.get(0);
                latLng = new LatLng(addr.getLatitude(), addr.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLng;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacao();
            } else if (permissaoResultado == PackageManager.PERMISSION_GRANTED) {
                pedirPermissao();
            }
        }
    }

    private void pedirPermissao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        }
    }

    private void alertaValidacao() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(getApplicationContext());
        alerta.setTitle("Permissão negada");
        alerta.setMessage("Para continuar você precisa aceitar as permissões!");
        alerta.setCancelable(false);
        alerta.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alerta.create().show();
    }
}