package com.company.AdminAPI.util.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Scanner;

public class PasswordUtility {

    public static void main(String[] args) {
        PasswordEncoder enc = new BCryptPasswordEncoder();

        Scanner myScanner = new Scanner(System.in);

        System.out.print("Type in password to encrypt: ");

        String password = myScanner.nextLine();

        String encodedPassword = enc.encode(password);

        System.out.println(encodedPassword);
    }
}
