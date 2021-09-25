package com.c196project.guis;

public enum RecyclerContext {
    MAIN {
        @Override
        public String toString() {
            return "Parent";
        }
    },

    CHILD {
        @Override
        public String toString() {
            return "Child";
        }
    }
}
