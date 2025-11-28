import numpy as np


"""
Description: Creates a bit string of chosen length, 50% chance for 0 or 1
Depends on: numpy
Parameters: length (int data type)
Returns: bit_string (array data type, 1 by length)
"""

def create_bit_array(length):
    bit_string = np.random.choice([0, 1], length, p = [0.5, 0.5])
    return bit_string

"""
Description: Corrupts a given string with a probability for a bit flip
Depends on: numpy
Parameters: string (string data type), probability (float data type, 0 - 1)
Returns: return_list (array data type, 1 by string's length)
"""

def bf_corrupter_return_list(string, probability):

    new_string = np.array([
    ], dtype=int)

    for i in range(string.shape[0]):
        current_bit = string[i]
        non_current_bit = (current_bit + 1) % 2
        new_bit = np.random.choice([current_bit, non_current_bit], 1, p=[1 - probability, probability])
        new_string = np.append(new_string, new_bit)

    return_list = new_string.tolist()

    return(return_list)

"""
Description: Corrupts a given string with a probability for an erasure
Parameters: string (string data type), probability (float data type, 0 - 1)
Returns: return_list (array data type, 1 by string's length)
"""
def e_corrupter_return_list(string, probability):

    new_string = np.full(string.shape[0], 2, dtype = int)

    for i in range(string.shape[0]):
        current_bit = string[i]
        new_bit = np.random.choice([current_bit, 2], 1, p=[1 - probability, probability])
        new_string[i] = new_bit[0]

    return_list = new_string.tolist()

    for i in range(string.shape[0]):
        if new_string[i] == 2:
            return_list[i] = "x"

    return (return_list)

