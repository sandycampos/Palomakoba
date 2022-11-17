package AdapterRecycler;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unir.projetocep.R;
import com.unir.projetocep.dados.SQLite;
import com.unir.projetocep.encapsulamento.Mapa;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.MyViewHolder> {

    private List<ContentValues> listEndereco = new ArrayList<ContentValues>();

    private FragmentManager fragmentManager = null;

    private Context context = null;

    public AdapterRecycler(List<ContentValues> listEndereco, FragmentManager fm, Context c) {
        this.fragmentManager = fm;
        this.context = c;
        this.listEndereco = listEndereco;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.endereco_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ContentValues cv = new ContentValues();
        cv = listEndereco.get(position);

        holder.cep.setText(cv.getAsString("cep"));
        holder.logradouro.setText(cv.getAsString("logradouro"));
        holder.bairro.setText(cv.getAsString("bairro"));
        holder.localidade.setText(cv.getAsString("localidade"));
        holder.text_id.setText(cv.getAsString("id"));
        holder.text_id.setAlpha(0); // Esconde o ID
    }

    @Override
    public int getItemCount() {
        return listEndereco.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView cep, logradouro, bairro, localidade, text_id;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cep = itemView.findViewById(R.id.txtCep);
            localidade = itemView.findViewById(R.id.txtLocalidade);
            logradouro = itemView.findViewById(R.id.txtLogradouro);
            bairro = itemView.findViewById(R.id.text_bairro);
            text_id = itemView.findViewById(R.id.txtId);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Confirme a exclusão");
                    builder.setMessage("Você realmente deseja excluir este local?");
                    builder.setCancelable(false);
                    builder.setNegativeButton("Não", null);
                    builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SQLite db = new SQLite(itemView.getContext());
                            int aux_lista = Integer.parseInt(text_id.getText().toString());
                            db.deletarRegistro(aux_lista);
                            Toast.makeText(itemView.getContext(), "Localização removida com sucesso!", Toast.LENGTH_SHORT).show();
                            listEndereco.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                        }
                    });
                    builder.show();
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), Mapa.class);
                    String endereco = "" + logradouro.getText().toString() + ", " + bairro.getText().toString()
                            + " - " + localidade.getText().toString();
                    intent.putExtra("endereco", endereco);//*/
                    Log.i("val", "end: " + endereco);
                    Toast.makeText(itemView.getContext(), "Aguarde enquanto mostra a localização do CEP!", Toast.LENGTH_SHORT).show();
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}