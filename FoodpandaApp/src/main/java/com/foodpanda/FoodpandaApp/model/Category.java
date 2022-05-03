package com.foodpanda.FoodpandaApp.model;

public enum Category {
    BREAKFAST(){
        @Override
        public String toString() {
            return "Breakfast";
        }
    },
    LUNCH {
        @Override
        public String toString() {
            return "Lunch";
        }
    },
    DESSERT {
        @Override
        public String toString() {
            return "Dessert";
        }
    },
    BEVERAGES {
        @Override
        public String toString() {
            return "Beverages";
        }
    };

    public abstract String toString();
}
