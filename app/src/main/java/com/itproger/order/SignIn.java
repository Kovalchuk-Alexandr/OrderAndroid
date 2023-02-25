package com.itproger.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itproger.order.Models.User;

public class SignIn extends AppCompatActivity {

    private Button btnSignIn;
    private EditText editPhone, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnSignIn = findViewById(R.id.btnSignIn);
        editPhone = findViewById(R.id.editTextPhone);
        editPassword = findViewById(R.id.editTextPassword);

        // Создаем объкт для работы с FireBase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Проверяем, смогли ли мы подключиться к таблице
                table.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //  В дочернем объекте ищем по ключу (номер телефона)
                        if(snapshot.child(editPhone.getText().toString()).exists()) {
                            // Если такой пользователь существует, получаем все данные и помещаем в класс User
                            User user = snapshot.child(editPhone.getText().toString()).getValue(User.class);

                            // Проверяем совпадает ли пароль из БД, с тем что введен в приложении
                            if(user.getPassword().equals(editPassword.getText().toString())) {
                                Toast.makeText(SignIn.this, "Успешно авторизован", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignIn.this, "Не авторизован", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(SignIn.this, "Такого пользователя нет", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SignIn.this, "Нет интернет соединения", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}