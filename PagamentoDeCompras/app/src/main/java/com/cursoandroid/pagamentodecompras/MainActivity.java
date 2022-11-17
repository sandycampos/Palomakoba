package com.cursoandroid.pagamentodecompras;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    CheckBox pdtArroz, pdtCarne, pdtPao, pdtLeite, pdtOvos;
    Button total, pagamento;
    TextView txtTotal;
    RadioGroup desconto;
    RadioButton desc0, desc5, desc10, desc15;
    EditText valorPago;
    double resultado, valorRecebido, troco;
    int desc;
    Double valorFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pdtArroz = findViewById(R.id.cbArroz);
        pdtCarne = findViewById(R.id.cbCarne);
        pdtPao = findViewById(R.id.cbPao);
        pdtLeite = findViewById(R.id.cbLeite);
        pdtOvos = findViewById(R.id.cbOvos);
        total = findViewById(R.id.btTotal);
        pagamento = findViewById(R.id.btPagamento);
        txtTotal = findViewById(R.id.txtValor);
        desconto = findViewById(R.id.rgDesconto);
        desc0 = findViewById(R.id.rb0);
        desc5 = findViewById(R.id.rb5);
        desc10 = findViewById(R.id.rb10);
        desc15 = findViewById(R.id.rb15);
        valorPago = findViewById(R.id.txtPago);

        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultado = 0;
                if (pdtArroz.isChecked())
                    resultado += 3.50;
                if (pdtCarne.isChecked())
                    resultado += 12.30;
                if (pdtPao.isChecked())
                    resultado += 2.20;
                if (pdtLeite.isChecked())
                    resultado += 5.50;
                if (pdtOvos.isChecked())
                    resultado += 7.50;
                txtTotal.setText("Valor: R$ " + resultado);
            }
        });

        pagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desc15.isChecked()) {
                    valorFinal = resultado * 0.85;
                    desc = 15;
                }
                else if (desc10.isChecked()) {
                    valorFinal = resultado * 0.90;
                    desc = 10;
                }
                else if (desc5.isChecked()) {
                    valorFinal = resultado * 0.95;
                    desc = 5;
                }
                else if (desc0.isChecked() || desconto.getCheckedRadioButtonId() == -1) {
                    valorFinal = resultado;
                    desc = 0;
                }

                if (valorPago.getText().toString().isEmpty()) {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                    alerta.setTitle("Aviso");
                    alerta.setMessage("Valor incompatível com a compra!");
                    alerta.setNeutralButton("OK", null);
                    alerta.show();
                }

                else {
                    valorRecebido = Double.parseDouble(valorPago.getText().toString());
                    troco = valorRecebido - valorFinal;
                    if (troco >= 0) {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                        alerta.setTitle("Aviso");
                        String msg = String.format("Valor total da compra: R$ %.2f \nDesconto: %d%% \nValor pago: R$ %.2f \nTroco: R$ %.2f", valorFinal, desc, valorRecebido, troco);
                        alerta.setMessage(msg);
                        alerta.setNeutralButton("OK", null);
                        alerta.show();
                    }
                    else {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                        alerta.setTitle("Aviso");
                        alerta.setMessage("Valor incompatível com a compra!");
                        alerta.setNeutralButton("OK", null);
                        alerta.show();
                    }
                }

            }
        });
    }
}