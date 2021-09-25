package com.c196project.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

    @Entity(tableName = "Instructors")
    public class Instructor {
        @PrimaryKey(autoGenerate = true)
        private int id;
        private String name;
        private String email;
        private String phone;
        private int courseId;

        @Ignore
        public Instructor() {

        }

        @Ignore
        public Instructor(String name, String email, String phone, int courseId) {
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.courseId = courseId;
        }

        public Instructor(String name, String email, String phone) {
            this.name = name;
            this.email = email;
            this.phone = phone;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }
    }
