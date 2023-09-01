# About
[Simple Banking System](https://hyperskill.org/projects/93) project is a part of Java Backend Developer track provided by Hyperskill.

- This is an application for managing banking system. Application can generate card number along with PIN, user can log in to the account and check balance, add income, make transfer to another account in the database and delete account. 
- All card numbers start with 400000 and generated randomly with the last number generated with help of the Luhn Algorithm. 
- Application is working with SQLite database.
- Connection to the database is done with the help of JDBC api.
- Database has only one table `card` with the following columns:
    - `id` INTEGER
    - `number` TEXT
    - `pin` TEXT
    - `balance` INTEGER DEFAULT 0

# Functionality
After the connection to the database is established, application outputs menu with the help of `System.out`. Application is getting user's input through `System.in`.

```
1. Create an account 
2. Log into account 
0. Exit
```
- `Exit` stops the application
- `Create account` generates card number along with PIN
- `Log into account` requires card number and PIN. If log in is successful, user gets transferred to the account menu 

```
1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
```
With this menu user can check current balance, add income (any positive number), make transfer (can't make transfer of the higher sum than balance, to the same account, or to the non-existing account), and close (=delete) account.

**Example**
```
// user's input is indicated with >>

1. Create an account 
2. Log into account 
0. Exit
>> 1

Your card has been created
Your card number:
4000003789119852
Your card PIN:
9650

1. Create an account 
2. Log into account 
0. Exit
>> 1

Your card has been created
Your card number:
4000008021181123
Your card PIN:
2162

1. Create an account 
2. Log into account 
0. Exit
>> 2

Enter your card number:
>> 4000003789119852
Enter your PIN:
>> 9650

You have successfully logged in!


1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>> 1

Balance: 0

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>> 2

Enter income:
>> 100
Income was added!

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>> 3

Transfer
Enter card number:
>> 4000008021181122
Probably you made a mistake in the card number. Please try again!

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>> 3

Transfer
Enter card number:
>> 4000008021181123
Enter how much money you want to transfer:
>> 200
Not enough money!

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>> 3

Transfer
Enter card number:
>> 4000008021181123
Enter how much money you want to transfer:
>> -100
Transfer must be higher than 0

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>> 3

Transfer
Enter card number:
>> 4000003789119852
You can't transfer money to the same account!

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>> 3

Transfer
Enter card number:
>> 4000008021181123
Enter how much money you want to transfer:
>> 100
Success!

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>> 1

Balance: 0

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>> 5

You have successfully logged out!


1. Create an account 
2. Log into account 
0. Exit
>> 2

Enter your card number:
>> 4000008021181123
Enter your PIN:
>> 0000
Wrong card number or PIN!


1. Create an account 
2. Log into account 
0. Exit
>> 2

Enter your card number:
>> 4000008021181123
Enter your PIN:
>> 2162

You have successfully logged in!


1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>> 1

Balance: 100

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>> 4

The account has been closed!

1. Create an account 
2. Log into account 
0. Exit
>> 2

Enter your card number:
>> 4000008021181123
Enter your PIN:
>> 2162
Wrong card number or PIN!


1. Create an account 
2. Log into account 
0. Exit
>> 0

Bye!
```
