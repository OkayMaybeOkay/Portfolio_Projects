import numpy as np
#Decoders
from Projects.BitFlipDecodingAnyHMatrix import bit_flip_decode
from Projects.ErasureDecodingAnyHMatrix import erasure_decode

#Create 0's
from Projects.TextfileHMatrixCreator import return_0_list

#Corrupts Code
from Projects.BitCreationCorruption import e_corrupter_return_list, bf_corrupter_return_list

"""
Description: An interactive program that asks you if you wanna make a big random keyword, corrupt it, and decode it
Depends on: 
Parameters: None, not a function
Returns: FER_BER (a list, gives the Frame error rate and bit error rate) 
"""

def sample_error_data(probability, choose_method):

    i_max = 10
    total_trials = 0
    total_frame_errors = 0
    total_bit_errors = 0

    for i in range(i_max):

        # 476 zeroes
        keyword_list = return_0_list(476)
        keyword = np.array(keyword_list)

        if ((choose_method == "b") | (choose_method == "B")):

            bf_corrupted_keyword_list = bf_corrupter_return_list(keyword, probability)
            bf_decoded_keyword = bit_flip_decode(bf_corrupted_keyword_list)
            bf_decoded_keyword_list = bf_decoded_keyword.tolist()

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

            print("Trial " + str(total_trials))
            print("Keyword: " + str(keyword_list))
            print("Corrupted Keyword: " + str(bf_corrupted_keyword_list))
            print("Decoded Keyword: " + str(bf_decoded_keyword_list))
            print("Frame Errors: " + str(bf_frame_errors))
            print("Bit Errors: " + str(bf_bit_errors) + "\n")


            if (total_frame_errors == 100):
                break




        elif ((choose_method == "e") | (choose_method == "E")):

            e_corrupted_keyword_list = e_corrupter_return_list(keyword, probability)
            e_decoded_keyword = erasure_decode(e_corrupted_keyword_list)
            e_decoded_keyword_list = e_decoded_keyword.tolist()

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

            print("Trial " + str(total_trials))
            print("Keyword: " + str(keyword_list))
            print("Corrupted Keyword: " + str(e_corrupted_keyword_list))
            print("Decoded Keyword: " + str(e_decoded_keyword_list))
            print("Frame Errors: " + str(e_frame_errors))
            print("Bit Errors: " + str(e_bit_errors) + "\n")

            if (total_frame_errors == 100):
                break



    print("Total Trials: " + str(total_trials))
    print("Total Frame Errors: " + str(total_frame_errors))
    print("Total Bit Errors: " + str(total_bit_errors))
    print("Frame Error Rate: " + str(total_frame_errors/total_trials))
    print("Bit Error Rate: " + str(total_bit_errors / (total_trials * keyword.size)))

    FER_BER = [total_frame_errors/total_trials, total_bit_errors / (total_trials * keyword.size)]
    return (FER_BER)

#sample_error_data(0.5, 'e')
