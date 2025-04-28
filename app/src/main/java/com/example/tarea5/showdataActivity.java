package com.example.tarea5;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class showdataActivity extends AppCompatActivity {

    RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showdata);

        requestQueue = Volley.newRequestQueue(this);

        ImageView imageView = findViewById(R.id.imageViewshowDish);
        TextView textViewName = findViewById(R.id.textViewDishshowName);
        TextView textViewDescription = findViewById(R.id.textViewDishshowDescription);
        TextView textViewLocation = findViewById(R.id.textViewDishshowLocation);

        // Obtener los datos del Intent
        long dishId = getIntent().getLongExtra("dishId", -1);
        String dishName = getIntent().getStringExtra("dishName");
        String dishImageURL = getIntent().getStringExtra("dishImageURL");
        String dishDescription = getIntent().getStringExtra("dishDescription");
        String Pricedish = getIntent().getStringExtra("Pricedish");

        // Establecer los datos en las vistas
        textViewName.setText(dishName);
        textViewDescription.setText(dishDescription);
        textViewLocation.setText(Pricedish);

        // Cargar la imagen desde la URL utilizando Volley
        ImageRequest imageRequest = new ImageRequest(dishImageURL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // La respuesta es la imagen cargada, asigna la imagen al ImageView
                        imageView.setImageBitmap(response);
                    }
                },
                0, 0, ImageView.ScaleType.CENTER_INSIDE, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(showdataActivity.this, "No se encontró la URL de la imagen", Toast.LENGTH_SHORT).show();
                        // Manejar errores
                        // Establecer una imagen de recurso de drawable predeterminada en caso de error
                        imageView.setImageResource(R.drawable.image_not_found);
                    }
                });

        // Añade la solicitud a la cola de solicitudes
        requestQueue.add(imageRequest);


        Button buttonBackToMain = findViewById(R.id.buttonBackToMain);
        buttonBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
