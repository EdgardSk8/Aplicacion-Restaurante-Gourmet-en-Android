package com.example.tarea5;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class Default_Dish {

    public static void insertDish(SQLiteDatabase db, String dishName, String dishimage, String dishdescription, String pricedish) {
        ContentValues values = new ContentValues();
        values.put(RestaurantContract.DishEntry.COLUMN_NAME_DISH, dishName);
        values.put(RestaurantContract.DishEntry.COLUMN_IMAGE_DISH, dishimage);
        values.put(RestaurantContract.DishEntry.COLUMN_DESCRIPTION_DISH, dishdescription);
        values.put(RestaurantContract.DishEntry.COLUMN_PRICE_PRICE, pricedish);


        db.insert(RestaurantContract.DishEntry.TABLE_NAME, null, values);
    }

    public static void DefaultDishies(SQLiteDatabase db) {

        insertDish(db, "Espagueti a la Carbonara",
                "https://i.pinimg.com/736x/00/e0/03/00e00320f916476e4b34664b380da518.jpg",
                "Espagueti a la Carbonara es un plato clásico de la cocina italiana hecho con espaguetis, huevos, queso parmesano, panceta y pimienta negra. Este plato es conocido por su rica y cremosa salsa que se mezcla perfectamente con la pasta al dente. \n \n" +
                        "Rango de precios: 20 $ - 35 $\n \n" +
                        "Tipos de cocina: Italiana\n \n" +
                        "Dietas especiales: Opciones sin gluten disponibles\n \n" +
                        "Ambiente: Tradicional y acogedor, ideal para cenas familiares\n \n" +
                        "Ingredientes: Espaguetis de trigo duro, huevos frescos, queso parmesano auténtico, panceta italiana, pimienta negra recién molida\n \n" +
                        "Servicios: Asientos en el interior y al aire libre, se aceptan tarjetas de crédito, comida para llevar, servicios sanitarios disponibles",
                "30 $");




        insertDish(db, "Salmón al Horno con Costra de Hierbas",
                "https://i.pinimg.com/736x/39/bc/cd/39bccd6dacdf8f49499ecf78995d5fc2.jpg",
                "Salmón al Horno con Costra de Hierbas es un plato exquisito y saludable que combina la frescura del salmón con una costra crujiente de hierbas frescas. Este platillo gourmet es ideal para una cena elegante o una ocasión especial. \n \n" +
                        "Rango de precios: 35 $ - 50 $\n \n" +
                        "Tipos de cocina: Internacional\n \n" +
                        "Dietas especiales: Sin gluten\n \n" +
                        "Ingredientes: Filete de salmón, hierbas frescas (perejil, eneldo, cilantro), limón, ajo, pan rallado sin gluten\n \n" +
                        "Servicios: Asientos en el interior y al aire libre, se aceptan tarjetas de crédito, comida para llevar",
                "40 $");

        insertDish(db, "Pollo Relleno de Espinacas y Queso de Cabra",
                "https://i.pinimg.com/736x/66/88/e4/6688e4785b1d784cb221dd1ba7684f84.jpg",
                "Pollo Relleno de Espinacas y Queso de Cabra es un plato sofisticado que ofrece una mezcla deliciosa de sabores y texturas. El pollo tierno se rellena con espinacas frescas y queso de cabra cremoso, creando una experiencia culinaria memorable. \n \n" +
                        "Rango de precios: 25 $ - 40 $\n \n" +
                        "Tipos de cocina: Internacional\n \n" +
                        "Dietas especiales: Baja en carbohidratos\n \n" +
                        "Ingredientes: Pechuga de pollo, espinacas frescas, queso de cabra, ajo, cebolla, hierbas italianas\n \n" +
                        "Servicios: Asientos en el interior y al aire libre, se aceptan tarjetas de crédito, comida para llevar",
                "30 $");

        insertDish(db, "Filete de Res con Reducción de Vino Tinto",
                "https://i.pinimg.com/736x/54/72/f8/5472f8f06831d76ad4d3f1f70c6bed76.jpg",
                "Filete de Res con Reducción de Vino Tinto es un platillo lujoso y lleno de sabor. Este plato presenta un filete de res jugoso acompañado de una rica reducción de vino tinto que realza sus sabores naturales. Perfecto para una cena gourmet. \n \n" +
                        "Rango de precios: 45 $ - 60 $\n \n" +
                        "Tipos de cocina: Internacional\n \n" +
                        "Dietas especiales: Alta en proteínas\n \n" +
                        "Ingredientes: Filete de res, vino tinto, caldo de carne, cebolla, ajo, romero fresco\n \n" +
                        "Servicios: Asientos en el interior y al aire libre, se aceptan tarjetas de crédito, comida para llevar",
                "50 $");

        insertDish(db, "Hamburguesa de Cerdo con Salsa Barbacoa y Coleslaw",
                "https://i.pinimg.com/736x/02/cc/e7/02cce76aa4b85e79c981c242e9db57b5.jpg",
                "Hamburguesa de Cerdo con Salsa Barbacoa y Coleslaw es una opción deliciosa y satisfactoria para los amantes de las hamburguesas. Esta hamburguesa combina la carne de cerdo tierna y jugosa con una salsa barbacoa casera y un crujiente coleslaw. \n \n" +
                        "Rango de precios: 20 $ - 35 $\n \n" +
                        "Tipos de cocina: Americana\n \n" +
                        "Dietas especiales: Sin gluten disponible\n \n" +
                        "Ingredientes: Carne de cerdo, salsa barbacoa casera, coleslaw (repollo, zanahoria, aderezo), pan sin gluten opcional\n \n" +
                        "Servicios: Asientos en el interior y al aire libre, se aceptan tarjetas de crédito, comida para llevar",
                "25 $");

        insertDish(db, "Hamburguesa de Pavo con Arándanos y Queso de Cabra",
                "https://i.pinimg.com/736x/a0/b7/b0/a0b7b00dddb9d94e70fff8cef9e493b2.jpg",
                "Hamburguesa de Pavo con Arándanos y Queso de Cabra es una opción gourmet y saludable. La combinación del pavo magro, los arándanos dulces y el queso de cabra cremoso ofrece un sabor único y delicioso. \n \n" +
                        "Rango de precios: 25 $ - 40 $\n \n" +
                        "Tipos de cocina: Fusión\n \n" +
                        "Dietas especiales: Baja en grasas\n \n" +
                        "Ingredientes: Carne de pavo, arándanos deshidratados, queso de cabra, espinacas frescas, pan integral\n \n" +
                        "Servicios: Asientos en el interior y al aire libre, se aceptan tarjetas de crédito, comida para llevar",
                "30 $");
    }

}
