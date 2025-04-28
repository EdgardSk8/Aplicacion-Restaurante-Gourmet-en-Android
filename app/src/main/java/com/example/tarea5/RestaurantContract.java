package com.example.tarea5;

import android.provider.BaseColumns;

public class RestaurantContract {

    public static class DishEntry implements BaseColumns{
        public static final String COLUMN_NAME_DISH = "nombre";
        public static final String COLUMN_DESCRIPTION_DISH = "descripcion";
        public static final String COLUMN_PRICE_PRICE = "precio";
        public static final String COLUMN_IMAGE_DISH = "imagen";
        public static final String TABLE_NAME = "Platillos";

    }

}
