package com.c196project.models;

public enum AssessmentType {
    OA {
        @Override
        public String toString() {
            return "Objective Assessment";
        }
    },

    PA {
        @Override
        public String toString() {
            return "Performance Assessment";
        }
    }
}
