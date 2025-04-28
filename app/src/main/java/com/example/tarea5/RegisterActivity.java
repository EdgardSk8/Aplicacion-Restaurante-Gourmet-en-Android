package com.example.tarea5;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, passwordRepeatEditText;
    private Button registerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Referencias a las vistas
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        passwordRepeatEditText = findViewById(R.id.passwordrepeat);  // Añadir referencia a passwordRepeatEditText
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String passwordRepeat = passwordRepeatEditText.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordRepeat)) {
                    Toast.makeText(RegisterActivity.this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegisterActivity.this, "Por favor ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(passwordRepeat)) {
                    Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (task.isSuccessful()) {
                                    SignInMethodQueryResult result = task.getResult();
                                    if (result != null && result.getSignInMethods() != null) {
                                        if (result.getSignInMethods().isEmpty()) {
                                            // No hay cuentas asociadas con este correo electrónico, puedes registrar un nuevo usuario
                                            mAuth.createUserWithEmailAndPassword(email, password)
                                                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (task.isSuccessful()) {
                                                                // Registro exitoso, actualizar la UI con la información del usuario
                                                                FirebaseUser user = mAuth.getCurrentUser();
                                                                Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                                                                // Navegar a la actividad principal
                                                                // Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                                // startActivity(intent);
                                                                finish();
                                                            } else {
                                                                // Si el registro falla, verificar la razón
                                                                if (task.getException() != null && task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                                    // El correo electrónico ya está en uso
                                                                    Toast.makeText(RegisterActivity.this, "Ya existe una cuenta con este correo", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    // Otros errores de registro
                                                                    Toast.makeText(RegisterActivity.this, "Registro fallido", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }
                                                    });
                                        } else {
                                            // Ya existe una cuenta asociada con este correo electrónico
                                            Toast.makeText(RegisterActivity.this, "Un usuario ya usa este correo", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        // Ocurrió un error al obtener los métodos de inicio de sesión
                                        Toast.makeText(RegisterActivity.this, "Error al verificar el correo", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Ocurrió un error al verificar el correo electrónico
                                    Toast.makeText(RegisterActivity.this, "Error al verificar el correo", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}