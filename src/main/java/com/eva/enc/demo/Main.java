package com.eva.enc.demo;


import java.util.Scanner;
import static com.eva.enc.FileIO.*;

public class Main {


    public static String password;
    public static void main(String[] args) throws Exception {
       // checkEncDecWithPassword();
       // System.exit(1);

        System.out.println(" Starting WFIQ Utility Application!");
        Scanner userInput = new Scanner(System.in);
        while(true) {
            System.out.println(" Enter 1 to convert Json To Csv & Encrypt PII ");
            System.out.println(" Enter 2 to Decrypt CSV");

            String input = userInput.nextLine();
            input = input.trim();
            System.out.println(" You chose Option :  '" + input + "'");
            if("1".equals(input)) {
                System.out.println(" Program will Convert Json To Csv and Encrypt all PII Information ");
                System.out.println(" Enter Your Secret Password ( required for AES encryption ) : " );
                password = userInput.nextLine();
                System.out.println(" Important Message : Do Not Loose this password as its required to decrypt Data ");
                System.out.println(" Press Enter key to acknowledge and continue ");
                userInput.nextLine();
                generateContactTracingSummaryReport();
                generateContactTracingDetailGraphReport();
                generateEncryptedEmployeeMaster();


                break;
            }else if("2".equals(input)) {
                System.out.println(" Program will Decrypt CSV");
                System.out.println(" Enter Your Secret Password ( used for AES encryption ) : " );
                password = userInput.nextLine();
                //read file and decrypt every element
                readAndDecryptDetailReport();
                readAndDecryptSummaryReport();
                readAndDecryptEmpMasterReport();
                break;
            }else {
                System.out.println(" Invalid Response, please try again!");

            }
        }
    }
}
