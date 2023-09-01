package banking;

import java.util.Random;

class Card {
    static String generateCard() {
        StringBuilder temp = new StringBuilder();
        temp.append(400000);
        String accountId = randomNumber(9);
        temp.append(accountId);

        int controlNumber = luhnAlgo(temp);
        int lastDigit = Math.abs(round(controlNumber) - controlNumber);
        temp.append(lastDigit);

        return temp.toString();
    }

    static boolean checkByLuhn(String cardNumber) {
        int sum = 0;
        int checkNumber;
        int lastDigit = -1;
        int temp;
        for (int i = 0; i < cardNumber.length(); i++) {
            if (i == cardNumber.length() - 1) {
                lastDigit = Character.getNumericValue(cardNumber.charAt(i));
                break;
            }
            temp = Character.getNumericValue(cardNumber.charAt(i));
            if (i % 2 == 0) {
                temp *= 2;
                if (temp > 9) {
                    temp -= 9;
                }
            }
            sum += temp;
        }

        checkNumber = Math.abs(round(sum) - sum);

        //return true if last digit is correct, so card passed Luhn algorithm
        return checkNumber == lastDigit;
    }


    private static int round(int input) {
        if (input % 10 == 0) {
            return input;
        }
        return (input + 10)/10 * 10;
    }

    private static int luhnAlgo(StringBuilder input) {
        int sum = 0;
        int temp;
        for (int i = 0; i < input.length(); i++) {
            temp = Character.getNumericValue(input.charAt(i));
            if (i % 2 == 0) {
                temp *= 2;
                if (temp > 9) {
                    temp -= 9;
                }
            }
            sum += temp;
        }

        return sum;
    }

     static String randomNumber(int length) {
        StringBuilder temp = new StringBuilder();
        Random random = new Random();
        while (length > 0) {
            temp.append(random.nextInt(10));
            length--;
        }

        return temp.toString();
    }
}
