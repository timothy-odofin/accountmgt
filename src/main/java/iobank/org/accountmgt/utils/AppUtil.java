package iobank.org.accountmgt.utils;

import java.util.regex.Pattern;

public class AppUtil {

    public  static Integer digitCombo(String number) {
        Integer newNumber = Integer.parseInt(number);

        if (newNumber < 10) {
            return newNumber;
        } else {
            String[] arr1 = {};
            arr1 = number.split("");
            int numbertobereturned = Integer.parseInt(arr1[0]) + Integer.parseInt(arr1[1]);
            return numbertobereturned;
        }
    }

    public  static Integer lastIndexCalcTwo(Integer inputNumber) {
        if (inputNumber % 10 == 0) {

            return 0;
        }
        String newNumber = inputNumber.toString();
        String[] arr2 = {};
        arr2 = newNumber.split("");
        int firstIndex = Integer.parseInt(arr2[0]) + 1;
        String roundedValue = firstIndex + "0";
        int lastReturn = Integer.parseInt(roundedValue) - inputNumber;

        return lastReturn;
    }

    public static int getNumberSum(String[] arr) {
        int actualNumber = 0;
        for (int i = arr.length - 1; i >= 0; i--) {
            if (i % 2 == 0) {
                int useNumber = Integer.parseInt(arr[i]) * 2;
                actualNumber += digitCombo(Integer.toString(useNumber));

            } else {
                int useNumber2 = Integer.parseInt(arr[i]) * 1;
                actualNumber += digitCombo(Integer.toString(useNumber2));
            }
        }
        return actualNumber;
    }
    public static String getSerialNumber(long out, int noOfDigit) {
        String result = String.format("%0" + noOfDigit + "d", out);
        return result;
    }

    public static String generateAccountNo(long customerNumber, String bankCode) {
        String changeNumber = getSerialNumber(customerNumber, 6);
        String calNumber = bankCode + changeNumber;
        String[] arr = calNumber.split("");
        int lastDigit = lastIndexCalcTwo(getNumberSum(arr));
        return calNumber + lastDigit;
    }

}
