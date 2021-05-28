package com.example.employeenew;
public class SignUpData {
    String name;
    String email;
    String password;
    String empid;
    String phonenumber;
    String department;
    String dateTime;





    public SignUpData(String name, String email, String empid, String phonenumber, String department, String dateTime) {
        this.name = name;
        this.email = email;
        this.empid = empid;
        this.phonenumber = phonenumber;
        this.department = department;
        this.dateTime = dateTime;


    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getEmpid() {
        return empid;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
