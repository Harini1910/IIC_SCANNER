package com.example.iic_scanner;

public class signup {

    public String Username, IDNo, Email, MobileNumber,Dept;

    public signup(){}

    public signup(String Username, String IDNo, String Emailid, String mobno,String dept) {
        this.Username = Username;
        this.IDNo = IDNo;
        this.Dept=dept;
        Email = Emailid;
        MobileNumber = mobno;
    }
}
