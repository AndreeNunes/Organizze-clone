package com.example.organizze.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.organizze.config.ConfigurancaoFirebase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.organizze.R;
import com.google.firebase.auth.FirebaseAuth;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView textoSaudacao;
    private TextView textoSaldo;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        textoSaudacao = findViewById(R.id.textSaudacao);
        textoSaldo = findViewById(R.id.textSaldo);

        calendarView = findViewById(R.id.calendarView);
        configCalendarView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuSair:
                autenticacao = ConfigurancaoFirebase.getFirebaseAutenticacao();
                autenticacao.signOut();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void adicionarReceita(View view){
        startActivity(new Intent(this, ReceitasActivity.class));
    }

    public void adicionarDespesas(View view){
        startActivity(new Intent(this, DespesasActivity.class));

    }

    public void configCalendarView(){
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

            }
        });
    }
}