import numpy as np
import sys
from Projects.BitCreationCorruption import bf_corrupter_return_list, e_corrupter_return_list
from Projects.BitFlipDecoding import bit_flip_decode
from Projects.ErasureDecoding import erasure_decode
from Projects.KeywordCreation import codeword_create

"""
Description: A short interactive program that asks you if you wanna make a random keyword, corrupt it, and decode it
Depends on: numpy, sys, BitCreationCorruption (bf_corrupter_return_list, e_corrupter_return_list), BitFlipDecoding
            (bit_flip_decode), ErasureDecoding (erasure_decode), KeywordCreation (codeword_create)
Parameters: None, not a function
Returns: Nothing, not a function
"""

print()
print("Create Codeword? ")

while True:

    answer_yes_no = input("Y/N: ")

    if answer_yes_no == "N" or answer_yes_no == "n":
        sys.exit(1)
    elif answer_yes_no == "Y" or answer_yes_no == "y":
        break
    else:
        print("\nRespond with Yes or No (Y/y/N/n)\n")

created_codeword = codeword_create()


print("\nCodeword: " + str(created_codeword) + "\n")

print("Bitflip or Erasure Corruption: ")

while True:

    answer_bitflip_erasure = input("B/E: ")

    if answer_bitflip_erasure == "B" or answer_bitflip_erasure == "b":
        print()

        corrupt_percent = 0

        while True:

            print("From 0 (0% corruption chance) to 1 (100% corruption chance)")
            answer_corrupt_percent = 0

            while True:

                try:
                    answer_corrupt_percent = input("Corrupt Chance: ")
                    answer_corrupt_percent = float(answer_corrupt_percent)
                    break
                except ValueError:
                    print("\nEnter a number\n")

            if float(answer_corrupt_percent) >= 0 and float(answer_corrupt_percent) <= 1:
                corrupt_percent = float(answer_corrupt_percent)
                break

            else:
                print("\nRespond with a valid decimal\n")

        corrupted_keyword = bf_corrupter_return_list(created_codeword, corrupt_percent)

        print("\nCorrupt Keyword: " + str(np.array(corrupted_keyword, dtype = object)) + "\n")

        print("Decode?")

        while True:

            answer_yes_no = input("Y/N: ")

            if answer_yes_no == "N" or answer_yes_no == "n":
                print()

            elif answer_yes_no == "Y" or answer_yes_no == "y":
                print()
                print("        Codeword: " + str(created_codeword))
                print("Corrupt Codeword: " + str(np.array(corrupted_keyword)))
                print("Decoded Codeword: " + str(bit_flip_decode(corrupted_keyword)))
                sys.exit(1)

            else:
                print("\nRespond with Yes or No (Y/y/N/n)\n")


    elif answer_bitflip_erasure == "E" or answer_bitflip_erasure == "e":
        print()

        corrupt_percent = 0

        while True:

            print("From 0 (0% corruption chance) to 1 (100% corruption chance)")
            answer_corrupt_percent = 0

            while True:

                try:
                    answer_corrupt_percent = input("Corrupt Chance: ")
                    answer_corrupt_percent = float(answer_corrupt_percent)
                    break
                except ValueError:
                    print("\nEnter a number\n")

            if float(answer_corrupt_percent) >= 0 and float(answer_corrupt_percent) <= 1:
                corrupt_percent = float(answer_corrupt_percent)
                break

            else:
                print("\nRespond with a valid decimal\n")

        corrupted_keyword = e_corrupter_return_list(created_codeword, corrupt_percent)

        print("\nCorrupt Keyword: " + str(np.array(corrupted_keyword, dtype = object)) + "\n")

        print("Decode?")

        while True:

            answer_yes_no = input("Y/N: ")

            if answer_yes_no == "N" or answer_yes_no == "n":
                print()

            elif answer_yes_no == "Y" or answer_yes_no == "y":
                print()
                print("        Codeword: " + str(created_codeword))
                print("Corrupt Codeword: " + str(np.array(corrupted_keyword, dtype = object)))
                print("Decoded Codeword: " + str(erasure_decode(corrupted_keyword)))
                sys.exit(1)

            else:
                print("\nRespond with Yes or No (Y/y/N/n)\n")

    else:
        print("\nRespond with Bitflip or Erasure (B/b/E/e)\n")
