package com.example.employeenew;

public class AttendanceData {
    String email;
    String dateTime;
    String Attendance;
    String empid;
    String name;

    public AttendanceData(String email, String dateTime, String attendance, String empid, String name) {
        this.email = email;
        this.dateTime = dateTime;
        Attendance = attendance;
        this.empid = empid;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAttendance() {
        return Attendance;
    }

    public void setAttendance(String attendance) {
        Attendance = attendance;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

