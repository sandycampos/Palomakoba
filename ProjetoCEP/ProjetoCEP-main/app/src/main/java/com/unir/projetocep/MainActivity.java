package com.unir.projetocep;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.unir.projetocep.api.CEPService;
import com.unir.projetocep.dados.SQLite;
import com.unir.projetocep.encapsulamento.CEP;
import com.unir.projetocep.encapsulamento.Mapa;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button botaoRecuperar;
    private TextView textoResultado;
    private EditText edtCEP;
    private Retrofit retrofit;
    private Button btnMostrar;
    private String endereco = null;
    private Button btnSalvar;
    SQLite bd;
    private String cep1 = "";
    private Button btnLista;
    private Sensor acelerometro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botaoRecuperar = findViewById(R.id.btnRecuperar);
        textoResultado = findViewById(R.id.txtExibir);
        edtCEP = findViewById(R.id.edtCEP);
        btnMostrar = findViewById(R.id.btnMostrar);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnLista = findViewById(R.id.btnLista);
        String urlCep = "https://viacep.com.br/ws/";
        retrofit = new Retrofit.Builder()
                .baseUrl(urlCep)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        botaoRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resultado = recuperarCep();
            }
        });
        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Mapa.class);
                Log.i("endereco", endereco);
                intent.putExtra("endereco", endereco);
                startActivity(intent);
            }
        });
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cep1 = edtCEP.getText().toString();
                bd = new SQLite(getApplicationContext());


                CEPService cepService = retrofit.create(CEPService.class);
                Call<CEP> call = cepService.recuperarCEP(cep1);
                call.enqueue(new Callback<CEP>() {
                    @Override
                    public void onResponse(Call<CEP> call, Response<CEP> response) {
                        if (response.isSuccessful()) {

                            CEP cep = response.body();
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("CEP: " + cep1 + "\n\nEndereço: " + cep.getLogradouro() +
                                    ", " + cep.getComplemento() + "\n" + cep.getBairro() + " - " + cep.getLocalidade() + "/" + cep.getUf());
                            builder.setCancelable(false);

                            builder.setNegativeButton("Salvar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("logradouro", cep.getLogradouro());
                                    cv.put("cep", cep.getCep());
                                    cv.put("bairro", cep.getBairro());
                                    cv.put("localidade", cep.getLocalidade());


                                    Long idd = bd.inserir(cep);

                                    if (idd > 0) {
                                        Toast.makeText(MainActivity.this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();
                                    }

                                    StringBuilder monta = new StringBuilder();
                                    monta.append(cep.getUf()).append("\n")
                                            .append(cep.getCep()).append("\n")
                                            .append(cep.getLocalidade()).append("\n")
                                            .append(cep.getBairro()).append("\n")
                                            .append(cep.getComplemento());


                                    Log.i("val", "vll: " + idd + " \n " + monta.toString());
                                }
                            });
                            builder.setNeutralButton("Voltar", null);
                            builder.show();

                            edtCEP.setText("");
                        } else {
                            Toast.makeText(MainActivity.this, "Informe o CEP", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<CEP> call, Throwable t) {

                    }
                });
            }
        });
        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Lista.class);
                startActivity(intent);
            }
        });
    }

    private String recuperarCep() {
        CEPService cepService = retrofit.create(CEPService.class);
        String cep = edtCEP.getText().toString();
        Call<CEP> call = cepService.recuperarCEP(cep);
        call.enqueue(new Callback<CEP>() {
            @Override
            public void onResponse(Call<CEP> call, Response<CEP> response) {
                if (response.isSuccessful()) {
                    CEP cep = response.body();
                    endereco = cep.getLogradouro() + " " + cep.getBairro() + " " + cep.getCep();
                    textoResultado.
                            setText(cep.getLogradouro() + ", " + cep.getBairro() + " - " + cep.getLocalidade());
                }
            }

            @Override
            public void onFailure(Call<CEP> call, Throwable t) {

            }
        });
        return endereco;
    }
}