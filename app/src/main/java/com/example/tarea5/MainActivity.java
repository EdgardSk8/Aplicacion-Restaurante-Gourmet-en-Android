package com.example.tarea5;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import com.example.tarea5.RestaurantContract.DishEntry;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private boolean isFirstTimeLaunch = true; //Variable para verificar primera vez
    ListView listView;
    RestaurantDbHelper restaurantDbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Obtener referencia al ListView
        listView = findViewById(R.id.ItemListView);
        registerForContextMenu(listView);

        // Inicializar la base de datos y cargar los lugares
        restaurantDbHelper = new RestaurantDbHelper(this);
        db = restaurantDbHelper.getWritableDatabase();

        requestQueue = Volley.newRequestQueue(this);

        verificarPrimerInicio();
        LoadDish();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                long dishId = cursor.getLong(cursor.getColumnIndexOrThrow(DishEntry._ID));
                String dishName = cursor.getString(cursor.getColumnIndexOrThrow(DishEntry.COLUMN_NAME_DISH));
                String dishImageURL = cursor.getString(cursor.getColumnIndexOrThrow(DishEntry.COLUMN_IMAGE_DISH));
                String dishDescription = cursor.getString(cursor.getColumnIndexOrThrow(DishEntry.COLUMN_DESCRIPTION_DISH));
                String Pricedish = cursor.getString(cursor.getColumnIndexOrThrow(DishEntry.COLUMN_PRICE_PRICE));

                Intent intent = new Intent(MainActivity.this, showdataActivity.class);
                intent.putExtra("dishId", dishId);
                intent.putExtra("dishName", dishName);
                intent.putExtra("dishImageURL", dishImageURL);
                intent.putExtra("dishDescription", dishDescription);
                intent.putExtra("Pricedish", Pricedish);
                startActivity(intent);


            }
        });
    }

    /*--------------------------------------------------------------Cargar Datos--------------------------------------------------------------*/
    public void LoadDish() {
        String[] columns = {
                DishEntry._ID,
                DishEntry.COLUMN_NAME_DISH,
                DishEntry.COLUMN_IMAGE_DISH,
                DishEntry.COLUMN_DESCRIPTION_DISH,
                DishEntry.COLUMN_PRICE_PRICE

        };

        cursor = db.query(DishEntry.TABLE_NAME, columns, null, null, null, null, null);

        String[] from = {
                DishEntry._ID,
                DishEntry.COLUMN_NAME_DISH,
                DishEntry.COLUMN_IMAGE_DISH,
                DishEntry.COLUMN_DESCRIPTION_DISH,
                DishEntry.COLUMN_PRICE_PRICE
        };

        int[] to = {
                R.id.textviewdishid,
                R.id.textViewDishName,
                R.id.imageViewDish,
                R.id.textviewdishdescription,
                R.id.textviewpricelocalization

        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.list_dish, cursor, from, to, 0
        ) {
            @Override
            public void setViewImage(ImageView v, String value) {

                // Cargar la imagen desde la URL utilizando Volley
                ImageRequest imageRequest = new ImageRequest(value,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                // La respuesta es la imagen cargada, asigna la imagen al ImageView
                                v.setImageBitmap(response);
                            }
                        },
                        0, 0, ImageView.ScaleType.CENTER_INSIDE, null,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "No se encontró la URL de la imagen", Toast.LENGTH_SHORT).show();
                                // Manejar errores
                                // Establecer una imagen de recurso de drawable predeterminada en caso de error
                                v.setImageResource(R.drawable.image_not_found);
                            }
                        });

                // Añade la solicitud a la cola de solicitudes
                requestQueue.add(imageRequest);
            }
        };
        listView.setAdapter(adapter);
    }

    /*--------------------------------------------------------------Cargar Datos--------------------------------------------------------------*/


    /*--------------------------------------------------------------Menu en barra--------------------------------------------------------------*/

    /*--------------------inflar main_menu--------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    /*--------------------inflar main_menu--------------------*/


    /*--------------------Logica de botones main_menu--------------------*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_menu_nuevo:

                // Inflar el diseño add_city.xml en un AlertDialog
                LayoutInflater inflater = LayoutInflater.from(this);
                View view = inflater.inflate(R.layout.add_dish, null);

                final AlertDialog dialog = new AlertDialog.Builder(this)
                        .setView(view)
                        .create();

                Button buttonAdd = view.findViewById(R.id.buttonAdd);
                Button buttonCancel = view.findViewById(R.id.buttonCancel);

                buttonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText editTextDishName = dialog.findViewById(R.id.addTextdishName);
                        EditText editTextDishDescription = dialog.findViewById(R.id.addTextdishDescription);
                        EditText editTextPriceDish = dialog.findViewById(R.id.addTextpriceDish);
                        EditText editTextDishurl = dialog.findViewById(R.id.addTextdishURL);

                        String NameDish = editTextDishName.getText().toString().trim();
                        String DescriptionDish = editTextDishDescription.getText().toString().trim();
                        String PriceDish = editTextPriceDish.getText().toString().trim();
                        String urlimage = editTextDishurl.getText().toString().trim();

                        if (!NameDish.isEmpty() && !urlimage.isEmpty() && !DescriptionDish.isEmpty() && !PriceDish.isEmpty()) {

                            // Convertir el primer carácter a mayúscula
                            char primerCaracter = Character.toUpperCase(NameDish.charAt(0));

                            // Concatenar el primer carácter en mayúscula con el resto del texto
                            String namedishFormateado = primerCaracter + NameDish.substring(1);

                            // Agregar lugar a la base de datos utilizando el nombre formateado
                            ContentValues values = new ContentValues();
                            values.put(DishEntry.COLUMN_NAME_DISH, namedishFormateado);
                            values.put(DishEntry.COLUMN_DESCRIPTION_DISH, DescriptionDish);
                            values.put(DishEntry.COLUMN_PRICE_PRICE, PriceDish);
                            values.put(DishEntry.COLUMN_IMAGE_DISH, urlimage);

                            long newRowId = db.insert(DishEntry.TABLE_NAME, null, values);

                            dialog.dismiss();
                            LoadDish();

                            Toast.makeText(MainActivity.this, "El Platillo " + namedishFormateado + " Se agregó correctamente", Toast.LENGTH_SHORT).show();

                        } else {
                            // Mostrar un mensaje de error si el nombre de la ciudad está vacío
                            Toast.makeText(MainActivity.this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss(); // Cierra el diálogo
                    }
                });

                dialog.show();
                return true;


            /*--------------------------Boton Predeterminados--------------------------*/
            case R.id.default_cities:

                permisodatospredeterminados();

                /*--------------------------Boton Predeterminados--------------------------*/
                return true;

            case R.id.item_signout:
                // Crear un cuadro de diálogo de confirmación
                new AlertDialog.Builder(this)
                        .setTitle("Cerrar sesión")
                        .setMessage("¿Está seguro de que desea cerrar sesión?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Si el usuario confirma, cerrar sesión
                                signOut();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null) // No hace nada si el usuario cancela
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
    /*--------------------Logica de botones main_menu--------------------*/

    /*--------------------------------------------------------------Menu en barra--------------------------------------------------------------*/



    /*--------------------------------------------------------------Menu flotante--------------------------------------------------------------*/

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.float_menu, menu); // Inflar el menú contextual
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // Obtener la información sobre el elemento seleccionado en el ListView

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.itemEditar:
                // Obtener el ID del Platillo seleccionada

                long dishid = info.id;

                // Obtener el nombre del platillo de la base de datos utilizando el ID
                String[] projection = {
                        DishEntry.COLUMN_NAME_DISH,
                        DishEntry.COLUMN_DESCRIPTION_DISH,
                        DishEntry.COLUMN_PRICE_PRICE,
                        DishEntry.COLUMN_IMAGE_DISH
                };
                String selection = DishEntry._ID + "=?";
                String[] selectionArgs = {String.valueOf(dishid)};
                Cursor cursor = db.query(
                        DishEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );

                // Verificar si se encontró la ciudad
                if (cursor != null && cursor.moveToFirst()) {
                    // Obtener el nombre del Platillo

                    String namedish = cursor.getString(cursor.getColumnIndexOrThrow(DishEntry.COLUMN_NAME_DISH));
                    String descriptiondish = cursor.getString(cursor.getColumnIndexOrThrow(DishEntry.COLUMN_DESCRIPTION_DISH));
                    String pricedish = cursor.getString(cursor.getColumnIndexOrThrow(DishEntry.COLUMN_PRICE_PRICE));
                    String urldish = cursor.getString(cursor.getColumnIndexOrThrow(DishEntry.COLUMN_IMAGE_DISH));

                    // Mostrar un cuadro de diálogo para editar el luga
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setView(R.layout.edit_dish);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    // Obtener referencia al EditText en el cuadro de diálogo
                    EditText editTextDishName = dialog.findViewById(R.id.editTextDishName);
                    EditText editTextDishDescription = dialog.findViewById(R.id.editTextDishDescription);
                    EditText editTextPriceDish = dialog.findViewById(R.id.editTextPriceDish);
                    EditText editTextDishurl = dialog.findViewById(R.id.editTextDishURL);


                    editTextDishName.setText(namedish);
                    editTextDishDescription.setText(descriptiondish);
                    editTextPriceDish.setText(pricedish);
                    editTextDishurl.setText(urldish);

                    // Configurar botones del cuadro de diálogo
                    Button buttonEdit = dialog.findViewById(R.id.buttonEdit);
                    buttonEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // Obtener el nuevo nombre del platillo del EditText
                            String newnamedish = editTextDishName.getText().toString().trim();
                            String newdescriptiondish = editTextDishDescription.getText().toString().trim();
                            String newpricedish = editTextPriceDish.getText().toString().trim();
                            String newurldish = editTextDishurl.getText().toString().trim();

                            if (newnamedish.isEmpty() || newdescriptiondish.isEmpty() || newpricedish.isEmpty() || newurldish.isEmpty()) {
                                Toast.makeText(MainActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                                return; // Salir del método onClick sin ejecutar la actualización
                            }

                            // Actualizar el nombre del platillo en la base de datos
                            ContentValues values = new ContentValues();

                            values.put(DishEntry.COLUMN_NAME_DISH, newnamedish);
                            values.put(DishEntry.COLUMN_DESCRIPTION_DISH, newdescriptiondish);
                            values.put(DishEntry.COLUMN_PRICE_PRICE, newpricedish);
                            values.put(DishEntry.COLUMN_IMAGE_DISH, newurldish);

                            String whereClause = DishEntry._ID + "=?";
                            String[] whereArgs = {String.valueOf(dishid)};
                            int rowsUpdated = db.update(DishEntry.TABLE_NAME, values, whereClause, whereArgs);

                            // Verificar si la actualización fue exitosa
                            if (rowsUpdated > 0) {
                                Toast.makeText(MainActivity.this, "Platillo actualizado correctamente", Toast.LENGTH_SHORT).show();
                                LoadDish(); // Recargar la lista de ciudades
                                dialog.dismiss(); // Cerrar el cuadro de diálogo
                            } else {
                                Toast.makeText(MainActivity.this, "Error al actualizar el Platillo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
                    buttonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss(); // Cerrar el cuadro de diálogo
                        }
                    });
                } else {
                    // Mostrar un mensaje si no se encontró la ciudad
                    Toast.makeText(this, "No se encontró el Platillo", Toast.LENGTH_SHORT).show();
                }

                // Cerrar el cursor
                if (cursor != null) {
                    cursor.close();
                }

                return true;

            case R.id.itemEliminar:
                // Obtiene el ID del elemento a eliminar
                long id = info.id;

                // Crea un diálogo de confirmación
                new AlertDialog.Builder(this)
                        .setTitle("Confirmar eliminación")
                        .setMessage("¿Está seguro de que desea eliminar este elemento?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Si el usuario confirma, eliminar el elemento de la base de datos
                                String selectionEliminar = DishEntry._ID + "=?";
                                String[] selectionArgsEliminar = {String.valueOf(id)};
                                db.delete(DishEntry.TABLE_NAME, selectionEliminar, selectionArgsEliminar);
                                // Recargar la lista de platillos
                                LoadDish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null) // No hace nada si el usuario cancela
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    /*--------------------------------------------------------------Menu flotante--------------------------------------------------------------*/

    /*--------------------------------------------------------------Verificacion--------------------------------------------------------------*/

    /*---------------------------Eliminar datos existentes---------------------------*/
    private void eliminarDatosExistente() {
        RestaurantDbHelper dbHelper = new RestaurantDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Especificar la tabla de la que deseas borrar los datos
        String tabla = DishEntry.TABLE_NAME;

        // Borrar todos los registros de la tabla
        db.delete(tabla, null, null);

        // Cerrar la conexión con la base de datos
        db.close();


    }
    /*---------------------------Eliminar datos existentes---------------------------*/

    /*---------------------------Agregar datos predeterminados---------------------------*/
    private void agregarDatosPredeterminados() {
        // Agregar los datos predeterminados a la base de datos
        RestaurantDbHelper dbHelper = new RestaurantDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Default_Dish.DefaultDishies(db);

    }
    /*---------------------------Agregar datos predeterminados---------------------------*/

    /*---------------------------Verificar primera vez---------------------------*/
    private void verificarPrimerInicio() {

        // Verificar si es la primera vez que se inicia la aplicación
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isFirstTimeLaunch = prefs.getBoolean("isFirstTimeLaunch", true);

        // Si es la primera vez que se inicia la aplicación, borra los datos existentes y agrega los predeterminados
        if (isFirstTimeLaunch) {
            agregarDatosPredeterminados();

            // Guarda el estado de isFirstTimeLaunch como falso para futuros lanzamientos

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFirstTimeLaunch", false);
            editor.apply();
            Toast.makeText(this, "BIENVENIDO A NUESTRO RESTAURANTE GOURMET", Toast.LENGTH_SHORT).show();
        } else {
            // Si no es la primera vez que se inicia la aplicación, muestra los datos existentes
            Toast.makeText(this, "BIENVENIDO DE NUEVO A NUESTRO RESTAURANTE GOURMET", Toast.LENGTH_SHORT).show();
            LoadDish();
        }
    }
    /*---------------------------Verificar primera vez---------------------------*/
    /*---------------------------Cerrar Sesion---------------------------*/
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        // Aquí puedes agregar cualquier otra acción que desees realizar después de cerrar sesión, como redireccionar al usuario a la pantalla de inicio de sesión, por ejemplo.
        // Por ejemplo:
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Esto limpia el stack de actividades y coloca LoginActivity como la actividad superior.
        startActivity(intent);
        finish(); // Cierra la actividad actual para evitar que el usuario regrese a ella usando el botón de retroceso.
    }
    /*---------------------------Cerrar Sesion---------------------------*/


    /*--------------------------------------------------------------Verificacion--------------------------------------------------------------*/

    /*--------------------------------------------------------------Permisos--------------------------------------------------------------*/
    private void permisodatospredeterminados() {

        // Inflar el diseño del cuadro de diálogo
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View view = inflater.inflate(R.layout.permissions, null);

        final EditText editTextPassword = view.findViewById(R.id.editTextPassword);
        final Button btnAccept = view.findViewById(R.id.btnpermissionsaceept);
        final Button btnCancel = view.findViewById(R.id.btnpermissionscancel);

        // Construir el cuadro de diálogo
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view)
                .setTitle("Ingrese contraseña para restablecer los datos");

        final AlertDialog dialog = builder.create();
        dialog.show();

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editTextPassword.getText().toString();

                if (password.equals("123qwe")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Confirmación")
                            .setMessage("¿Está seguro de volver a los datos predeterminados? Ya no podrá rehacer los cambios.")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Acción cuando se presiona "Aceptar"
                                    eliminarDatosExistente();
                                    agregarDatosPredeterminados();
                                    LoadDish();

                                }
                            })

                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Acción cuando se presiona "Cancelar"
                                    dialog.dismiss(); // Cierra el diálogo
                                }
                            });

                    // Mostrar el cuadro de diálogo
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }else if (password.equals("")) {
                    Toast.makeText(MainActivity.this, "Ingrese una Contraseña Válida", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else {
                    // Contraseña incorrecta, muestra un mensaje de error
                    Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Cierra el diálogo si se presiona "Cancelar"
            }
        });
    }
    /*--------------------------------------------------------------Permisos--------------------------------------------------------------*/

}
