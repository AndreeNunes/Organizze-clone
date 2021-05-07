package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.ConfigurancaoFirebase;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.helper.DateUtil;
import com.example.organizze.model.Movimentacao;
import com.example.organizze.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitasActivity extends AppCompatActivity {
    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private DatabaseReference databaseReference = ConfigurancaoFirebase.getFirebase();
    private FirebaseAuth autenticao = ConfigurancaoFirebase.getFirebaseAutenticacao();
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        campoData = findViewById(R.id.editData);
        campoValor = findViewById(R.id.editValor);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);

        campoData.setText(DateUtil.dataAtual());
        recuperarReceitaTotal();
    }

    public void salvaReceita(View v){
        String dataEscolhida = campoData.getText().toString();
        Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());;

        if(validarCamposReceita()){
            movimentacao = new Movimentacao();
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(dataEscolhida);
            movimentacao.setTipo("d");

            atualizarReceita(receitaTotal + valorRecuperado);

            movimentacao.salvar(dataEscolhida);
        }
    }

    public Boolean validarCamposReceita(){
        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoDescricao.getText().toString();
        String textoDescricao = campoCategoria.getText().toString();

        if(!textoValor.isEmpty()){
            if(!textoData.isEmpty()){
                if(!textoCategoria.isEmpty()){
                    if(!textoDescricao.isEmpty()){
                        return true;
                    }else{
                        Toast.makeText(ReceitasActivity.this, "Preencha a descricao", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else{
                    Toast.makeText(ReceitasActivity.this, "Preencha a categoria", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(ReceitasActivity.this, "Preencha a data", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(ReceitasActivity.this, "Preencha o valor", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void recuperarReceitaTotal(){
        DatabaseReference usuarioRef = databaseReference.child("usuarios")
                .child(Base64Custom.codificarBase64(autenticao.getCurrentUser().getEmail()));

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void atualizarReceita(Double despesaAtualizada){
        DatabaseReference usuarioRef = databaseReference.child("usuarios")
                .child(Base64Custom.codificarBase64(autenticao.getCurrentUser().getEmail()));

        usuarioRef.child("receitaTotal").setValue(despesaAtualizada);

    }
}