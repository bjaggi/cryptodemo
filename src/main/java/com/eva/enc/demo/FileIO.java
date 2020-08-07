package com.eva.enc.demo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;

import static com.kpmg.wfiq.encrypt.CryptoUtils.decryptWithPassword;
import static com.kpmg.wfiq.encrypt.CryptoUtils.encryptWithPassword;
import static com.kpmg.wfiq.utils.WfiqHelper.convertToStandardDateFormat;

public class FileIO {
    private static final String currentDirectory = System.getProperty("user.dir");


    /**
     *
     */
    public static void readAndDecryptEmpMasterReport() {

        String csvFile = currentDirectory+ File.separator+"contact_tracing_detail_graph_enc.csv";
        String line = "";
        String cvsSplitBy = ",";
        boolean isHeader = true;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null ) {
                // use comma as separator
                String[] eMaster = line.split(cvsSplitBy);

                if(isHeader) {
                    System.out.println("EmpMaster [EmpID= " + eMaster[0] + " , InfectedEmployee=" + eMaster[1] + " , EmployeeName=" + eMaster[2] + " , EmployeeEmail=" + eMaster[3] + " , Date=" + eMaster[4] + "]");
                } else {
                    System.out.println("EmpMaster [EmpID= " + eMaster[0] + " , EmployeeName=" + decryptWithPassword(eMaster[1]) + " , EMAIL=" + decryptWithPassword(eMaster[2]) + " , EmployeeEmail=" + decryptWithPassword(eMaster[3]) + " , Date=" + eMaster[4] + "]");
                }
                // Files.write(Paths.get(EXPORT_FILE_NAME), Arrays.asList(attendeesOpString), utf8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                isHeader = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     *
     */
    public static void readAndDecryptDetailReport() {

        String csvFile = currentDirectory+ File.separator+"contact_tracing_detail_graph_enc.csv";
        String line = "";
        String cvsSplitBy = ",";
        boolean isHeader = true;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null ) {
                // use comma as separator
                String[] schedule = line.split(cvsSplitBy);

                if(isHeader)
                    System.out.println("FinalReport [MeetingNumber= " + schedule[0] + " , InfectedEmployee=" + schedule[1] + " , EmployeeName=" + schedule[2] +" , EmployeeEmail=" + schedule[3]+ " , Date=" + schedule[4] + "]");
                else {
                    System.out.println("FinalReport [MeetingNumber= " + schedule[0] + " , InfectedEmployee=" + decryptWithPassword(schedule[1]) + " , EmployeeName=" + decryptWithPassword(schedule[2]) + " , EmployeeEmail=" + decryptWithPassword(schedule[3]) + " , Date=" + schedule[4] + "]");
                   // Files.write(Paths.get(EXPORT_FILE_NAME), Arrays.asList(attendeesOpString), utf8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                }

                isHeader = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     */
    public static void readAndDecryptSummaryReport() {

        String csvFile = currentDirectory+ File.separator+"contact_tracing_summary_enc.csv";
        String line = "";
        String cvsSplitBy = ",";
        boolean isHeader = true;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null ) {
                // use comma as separator
                String[] schedule = line.split(cvsSplitBy);

                if(isHeader)
                    System.out.println("FinalReport [MeetingNumber= " + schedule[0] + " , InfectedEmployee=" + schedule[1] + " , EmployeeName=" + schedule[2] +" , EmployeeEmail=" + schedule[3]+ " , Date=" + schedule[4] + "]");
                else {
                    System.out.println("FinalReport [MeetingNumber= " + schedule[0] + " , InfectedEmployee=" + decryptWithPassword(schedule[1]) + " , EmployeeName=" + decryptWithPassword(schedule[2]) + " , EmployeeEmail=" + decryptWithPassword(schedule[3]) + " , Date=" + schedule[4] + "]");
                    // Files.write(Paths.get(EXPORT_FILE_NAME), Arrays.asList(attendeesOpString), utf8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                }

                isHeader = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param aesKeyStr
     * @param encryptedText
     * @return
     * @throws Exception
     */
    public static String decryptContactTracingReport(String aesKeyStr, String encryptedText) throws Exception {

        byte[] decodedKey = Base64.getDecoder().decode(aesKeyStr);
        SecretKey secretKey = new SecretKeySpec(decodedKey, 0,decodedKey.length, "AES");
        byte[] value = Base64.getDecoder().decode(encryptedText);

        String decryptedText = decryptWithPassword(aesKeyStr );
        System.out.println(decryptedText);
        return  decryptedText;
    }


    public static String generateContactTracingDetailGraphReport() throws IOException {
        Charset utf8 = StandardCharsets.UTF_8;
        final String EXPORT_FILE_NAME = "contact_tracing_detail_graph_enc.csv";
        try (Reader reader = new FileReader(currentDirectory+ File.separator+"Outlook-calendar-export.json")) {
            JSONParser parser = new JSONParser();

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            //System.out.println(jsonObject);

            JSONArray valueArr = (JSONArray) jsonObject.get("value");
            System.out.println(" Total Number of meetings found "+ valueArr.size());

            //String headerString = "MeetingNumber, MeetingID, MeetingRoom, OrganizerEmail, AttendeeEmail, StartTime, EndTime ";
            String headerString = "MeetingNumber, InfectedEmployee, EmployeeName, Email, Date ";
            Files.write(Paths.get(EXPORT_FILE_NAME), Arrays.asList(headerString), utf8);

            // loop array
            int meetingNumber = 0;
            Iterator<JSONObject> valueIterator = valueArr.iterator();
            while (valueIterator.hasNext()) {
                JSONObject meetingDetails = valueIterator.next();
                //System.out.println(" Meeting Number : "+meetingNumber + " , Data : "+ meetingDetails );
                String meetingID = (String) meetingDetails.get("id");
                JSONArray attendeesArr = (JSONArray)  meetingDetails.get("attendees");
                String meetingRoom = (String) ((JSONObject)meetingDetails.get("location")).get("displayName");
                String organizerName = (String)((JSONObject) ((JSONObject)meetingDetails.get("organizer")).get("emailAddress")).get("name");
                String organizerEmail = (String)((JSONObject) ((JSONObject)meetingDetails.get("organizer")).get("emailAddress")).get("address");
                String startTime = (String) ((JSONObject)meetingDetails.get("start")).get("dateTime");
                String endTime = (String) ((JSONObject)meetingDetails.get("end")).get("dateTime");

                //String organizerOpString = meetingNumber+","+organizerName+","+organizerEmail+","+convertToStandardDateFormat(startTime);
                //Files.write(Paths.get("contact_tracing.csv"), Arrays.asList(organizerOpString), utf8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

                Iterator<JSONObject>  attendeesIterator = attendeesArr.iterator();
                while(attendeesIterator.hasNext()){
                    JSONObject attendeesDetail = attendeesIterator.next();
                    String attendeeEmail = (String)  ((JSONObject)attendeesDetail.get("emailAddress")).get("address");
                    String attendeeName = (String)  ((JSONObject)attendeesDetail.get("emailAddress")).get("name");
                    //String opString = meetingNumber+","+meetingID+","+meetingRoom+","+organizerEmail+","+attendeeEmail+","+convertToTigerGraphTimeFormat(startTime)+","+convertToTigerGraphTimeFormat(endTime);
                    String attendeesOpString = meetingNumber+","+encryptWithPassword(organizerName)+","+encryptWithPassword(attendeeName)+","+encryptWithPassword(attendeeEmail)+","+convertToStandardDateFormat(startTime);
                    //System.out.println(attendeesOpString);
                    Files.write(Paths.get(EXPORT_FILE_NAME), Arrays.asList(attendeesOpString), utf8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                }

                meetingNumber ++;
            }
            System.out.println( " Successfully Converted Json to Encrypted CSV and stored to file : "+EXPORT_FILE_NAME );

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @return
     * @throws IOException
     */
    public static String generateEncryptedEmployeeMaster() throws IOException {
        Charset utf8 = StandardCharsets.UTF_8;
        final String EXPORT_FILE_NAME = "employee_master_encrypted.csv";
        String line = "";
        String cvsSplitBy = ",";
        boolean isHeader = true;
        try (BufferedReader br = new BufferedReader(new FileReader(currentDirectory+ File.separator+"employee_master.csv"))) {
            while ((line = br.readLine()) != null ) {
                // use comma as separator
                String[] empMaster = line.split(cvsSplitBy);
                if(isHeader)
                    System.out.println("FinalReport [MeetingNumber= " + empMaster[0] + " , InfectedEmployee=" + empMaster[1] + " , EmployeeName=" + empMaster[2] +" , EmployeeEmail=" + empMaster[3]+ " , Date=" + empMaster[4] + "]");
                else
                    System.out.println("FinalReport [MeetingNumber= " + empMaster[0] + " , InfectedEmployee=" + encryptWithPassword(empMaster[1]) + " , EmployeeName=" + encryptWithPassword(empMaster[2]) +" , EmployeeEmail=" + encryptWithPassword(empMaster[3])+ " , Date=" + empMaster[4] + "]");
                isHeader = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String generateContactTracingSummaryReport() throws IOException {
        Charset utf8 = StandardCharsets.UTF_8;
        String EXPORT_FILE_NAME = "contact_tracing_summary.csv";

        try (Reader reader = new FileReader(currentDirectory+ File.separator+"Outlook-calendar-export.json")) {
            JSONParser parser = new JSONParser();

            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            JSONArray valueArr = (JSONArray) jsonObject.get("value");
            System.out.println(" Total Number of meetings found "+ valueArr.size());

            //String headerString = "MeetingNumber, MeetingID, MeetingRoom, OrganizerEmail, AttendeeEmail, StartTime, EndTime ";
            String headerString = "MeetingNumber, EmployeeName, Email, Date ";
            Files.write(Paths.get(EXPORT_FILE_NAME), Arrays.asList(headerString), utf8);

            // loop array
            int meetingNumber = 0;
            Iterator<JSONObject> valueIterator = valueArr.iterator();
            while (valueIterator.hasNext()) {
                JSONObject meetingDetails = valueIterator.next();
                //System.out.println(" Meeting Number : "+meetingNumber + " , Data : "+ meetingDetails );
                String meetingID = (String) meetingDetails.get("id");
                JSONArray attendeesArr = (JSONArray)  meetingDetails.get("attendees");
                String meetingRoom = (String) ((JSONObject)meetingDetails.get("location")).get("displayName");
                String organizerName = (String)((JSONObject) ((JSONObject)meetingDetails.get("organizer")).get("emailAddress")).get("name");
                String organizerEmail = (String)((JSONObject) ((JSONObject)meetingDetails.get("organizer")).get("emailAddress")).get("address");
                String startTime = (String) ((JSONObject)meetingDetails.get("start")).get("dateTime");
                String endTime = (String) ((JSONObject)meetingDetails.get("end")).get("dateTime");

                String organizerOpString = meetingNumber+","+encryptWithPassword(organizerName)+","+encryptWithPassword(organizerEmail)+","+convertToStandardDateFormat(startTime);
                Files.write(Paths.get(EXPORT_FILE_NAME), Arrays.asList(organizerOpString), utf8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

                Iterator<JSONObject>  attendeesIterator = attendeesArr.iterator();
                while(attendeesIterator.hasNext()){
                    JSONObject attendeesDetail = attendeesIterator.next();
                    String attendeeEmail = (String)  ((JSONObject)attendeesDetail.get("emailAddress")).get("address");
                    String attendeeName = (String)  ((JSONObject)attendeesDetail.get("emailAddress")).get("name");
                    //String opString = meetingNumber+","+meetingID+","+meetingRoom+","+organizerEmail+","+attendeeEmail+","+convertToTigerGraphTimeFormat(startTime)+","+convertToTigerGraphTimeFormat(endTime);
                    String attendeesOpString = meetingNumber+","+encryptWithPassword(attendeeName)+","+encryptWithPassword(attendeeEmail)+","+convertToStandardDateFormat(startTime);

                    //System.out.println(attendeesOpString);
                    Files.write(Paths.get(EXPORT_FILE_NAME), Arrays.asList(attendeesOpString), utf8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                }

                meetingNumber ++;
            }
            System.out.println( " Successfully Converted Json to Encrypted CSV and stored to file : "+EXPORT_FILE_NAME );


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
