import numpy as np

from Projects.BitCreationCorruption import bf_corrupter_return_list
from Projects.BitFlipDecoding import bit_flip_decode
from Projects.KeywordCreationSpecific import codeword_create_specific
from Projects.BitCreationCorruption import e_corrupter_return_list
from Projects.ErasureDecoding import erasure_decode

"""
Description: An interactive program that asks you if you wanna make a random keyword, corrupt it, and decode it
Depends on: numpy, sys, BitCreationCorruption (bf_corrupter_return_list, e_corrupter_return_list), BitFlipDecoding
            (bit_flip_decode), ErasureDecoding (erasure_decode), KeywordCreation (codeword_create)
Parameters: None, not a function
Returns: Nothing, not a function
"""

def sample_error_data(probability, choose_method):

    i_max = 1000
    total_trials = 0
    total_frame_errors = 0
    total_bit_errors = 0

    for i in range(i_max):

        keyword = codeword_create_specific([0, 0, 0])

        if ((choose_method == "b") | (choose_method == "B")):

            bf_corrupted_keyword = bf_corrupter_return_list(keyword, probability)
            bf_corrupted_keyword_array = np.array(bf_corrupted_keyword)
            bf_decoded_keyword = bit_flip_decode(bf_corrupted_keyword)

            bf_frame_errors = 0
            bf_bit_errors = 0

            for j in range(keyword.size):

                if (keyword[j] != bf_decoded_keyword[j]):
                    bf_bit_errors += 1

            if (bf_bit_errors != 0):
                bf_frame_errors += 1
                """print("v ERROR !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                """

            total_trials += 1
            total_frame_errors += bf_frame_errors
            total_bit_errors += bf_bit_errors

            """print("Trial " + str(total_trials))
            print("Keyword: " + str(keyword))
            print("Corrupted Keyword: " + str(bf_corrupted_keyword_array))
            print("Decoded Keyword: " + str(bf_decoded_keyword))
            print("Frame Errors: " + str(bf_frame_errors))
            print("Bit Errors: " + str(bf_bit_errors) + "\n")"""


            if (total_frame_errors == 100):
                break




        elif ((choose_method == "e") | (choose_method == "E")):

            e_corrupted_keyword = e_corrupter_return_list(keyword, probability)
            e_corrupted_keyword_array = np.array(e_corrupted_keyword, dtype = object)
            e_decoded_keyword = erasure_decode(e_corrupted_keyword)

            e_frame_errors = 0
            e_bit_errors = 0

            for j in range(keyword.size):
                if (keyword[j] != e_decoded_keyword[j]):
                    e_bit_errors += 1

            if (e_bit_errors != 0):
                e_frame_errors += 1
                """print("v ERROR !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                """

            total_trials += 1
            total_frame_errors += e_frame_errors
            total_bit_errors += e_bit_errors

            """print("Trial " + str(total_trials))
            print("Keyword: " + str(keyword))
            print("Corrupted Keyword: " + str(e_corrupted_keyword_array))
            print("Decoded Keyword: " + str(e_decoded_keyword))
            print("Frame Errors: " + str(e_frame_errors))
            print("Bit Errors: " + str(e_bit_errors) + "\n")"""

            if (total_frame_errors == 100):
                break



    """print("Total Trials: " + str(total_trials))
    print("Total Frame Errors: " + str(total_frame_errors))
    print("Total Bit Errors: " + str(total_bit_errors))"""
    print("Frame Error Rate: " + str(total_frame_errors/total_trials))
    print("Bit Error Rate: " + str(total_bit_errors / (total_trials * keyword.size)))

    FER_BER = [total_frame_errors/total_trials, total_bit_errors / (total_trials * keyword.size)]
    return (FER_BER)
