import numpy as np
import sys
from Projects.BitCreationCorruption import bf_corrupter_return_list, e_corrupter_return_list
from Projects.BitFlipDecoding import bit_flip_decode
from Projects.ErasureDecoding import erasure_decode
from Projects.KeywordCreation import codeword_create

"""
Description: An interactive program that asks you if you wanna make a random keyword, corrupt it, and decode it
Depends on: numpy, sys, BitCreationCorruption (bf_corrupter_return_list, e_corrupter_return_list), BitFlipDecoding
            (bit_flip_decode), ErasureDecoding (erasure_decode), KeywordCreation (codeword_create)
Parameters: None, not a function
Returns: Nothing, not a function
"""

print()
print("Welcome to the Bit Creator, Corruptor, and Decoder.")
print("This program will be able to create a keyword, corrupt it, and decode it.")
print("Would you like to start?")

"While loop to start or end program."
while True:

    answer_yes_no = input("Yes or No: ")

    if answer_yes_no == "No" or answer_yes_no == "no":
        print("Thank you, have a good day!")
        sys.exit(1)
    elif answer_yes_no == "Yes" or answer_yes_no == "yes":
        print("Let's begin!\n")
        break
    else:
        print("\nRespond with Yes or No")

created_codeword = codeword_create()

print("Here is your random codeword: " + str(created_codeword) + "\n")

print("Would you like to corrupt it using the Bitflip corruptor or the Erasure corruptor: ")

"While loop are used to repeat actions if the user gave an invalid input."

while True:

    answer_bitflip_erasure = input("Bitflip or Erasure: ")

    "If else statement branches if you choose bitflip or erasure."

    if answer_bitflip_erasure == "Bitflip" or answer_bitflip_erasure == "bitflip":
        print()
        print("The code will be corrupted using the Bitflip corruptor\n")

        corrupt_percent = 0

        while True:

            print("From 0 (0% corruption chance) to 1 (100% corruption chance)")
            answer_corrupt_percent = input("What do you choose: ")

            "Checks if input is a valid percentage."

            if float(answer_corrupt_percent) >= 0 and float(answer_corrupt_percent) <= 1:
                corrupt_percent = float(answer_corrupt_percent)
                break

            else:
                print("Respond with a valid decimal")

        print()
        print("You chose " + str(corrupt_percent) + " as your corrupt percentage\n")

        "Returns the corrupted keyword as a list, not an array."

        corrupted_keyword = bf_corrupter_return_list(created_codeword, corrupt_percent)

        print("Here is your corrupt keyword: " + str(np.array(corrupted_keyword)) + "\n")

        print("Would you like to decode the corrupted keyword now?")

        while True:

            answer_yes_no = input("Yes or No: ")

            if answer_yes_no == "No" or answer_yes_no == "no":
                print("Alright\n")

            elif answer_yes_no == "Yes" or answer_yes_no == "yes":
                print()
                print("        Codeword: " + str(created_codeword))
                print("Corrupt Codeword: " + str(np.array(corrupted_keyword)))
                print("Decoded Codeword: " + str(bit_flip_decode(corrupted_keyword)))
                sys.exit(1)

            else:
                print("Respond with Yes or No")


    elif answer_bitflip_erasure == "Erasure" or answer_bitflip_erasure == "erasure":
        print()
        print("The code will be corrupted using the Erasure corruptor")

        corrupt_percent = 0

        while True:

            print("From 0 (0% corruption chance) to 1 (100% corruption chance)")
            answer_corrupt_percent = input("What do you choose: ")

            "Checks if input is a valid percentage."

            if float(answer_corrupt_percent) >= 0 and float(answer_corrupt_percent) <= 1:
                corrupt_percent = float(answer_corrupt_percent)
                break

            else:
                print("Respond with a valid decimal")

        print()
        print("You chose " + str(corrupt_percent) + " as your corrupt percentage\n")

        "Returns the corrupted keyword as a list, not an array."

        corrupted_keyword = e_corrupter_return_list(created_codeword, corrupt_percent)

        print("Here is your corrupt keyword: " + str(np.array(corrupted_keyword, dtype = object)) + "\n")

        print("Would you like to decode the corrupted keyword now?")

        while True:

            answer_yes_no = input("Yes or No: ")

            if answer_yes_no == "No" or answer_yes_no == "no":
                print("Alright\n")

            elif answer_yes_no == "Yes" or answer_yes_no == "yes":
                print()
                print("        Codeword: " + str(created_codeword))
                print("Corrupt Codeword: " + str(np.array(corrupted_keyword, dtype = object)))
                print("Decoded Codeword: " + str(erasure_decode(corrupted_keyword)))
                sys.exit(1)

            else:
                print("Respond with Yes or No")


