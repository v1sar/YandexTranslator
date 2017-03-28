package com.v1sar.yandextranslator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.v1sar.yandextranslator.Helpers.LanguageConverter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "trnsl.1.1.20170319T210150Z.f957fb7c5aa69a04.aa08dbf2c63ea7557971c4902005270c45eeb94b";
    Button btnTranslate;
    EditText txtEdit;
    TextView txtTranslated;
    AppCompatSpinner leftSpinner;
    AppCompatSpinner rightSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtTranslated = (TextView) findViewById(R.id.txt_translated);
        btnTranslate = (Button) findViewById(R.id.btn_translate);
        txtEdit = (EditText) findViewById(R.id.text_to_translate);
        leftSpinner = (AppCompatSpinner) findViewById(R.id.left_spinner);
        rightSpinner = (AppCompatSpinner) findViewById(R.id.right_spinner);
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate();
            }
        });
    }

    private void translate() {
        ApiService api = RetroClient.getApiService();
        Call<Answer> call = api.getMyJSON(API_KEY, txtEdit.getText().toString(),
                LanguageConverter.getInstance().convert(leftSpinner.getSelectedItem().toString())+"-"+
                        LanguageConverter.getInstance().convert(rightSpinner.getSelectedItem().toString()));
        call.enqueue(new Callback<Answer>() {
            @Override
            public void onResponse(Call<Answer> call, Response<Answer> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "GREAT", Toast.LENGTH_SHORT).show();
                    txtTranslated.setText(response.body().getText()[0]);
                } else {
                    Toast.makeText(MainActivity.this, "NOT GREAT", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Answer> call, Throwable t) {
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
