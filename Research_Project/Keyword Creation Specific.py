import numpy as np

"""
Description: This program uses a G matrix (3 by 7) and an inputed U Bit String (1 by 3) to return a keyword (1 by 7).
Depends on: numpy
Parameters: list (List data type, 1 by 3)
Return: code_word (Array data type, 1 by 7)
"""

def codeword_create_specific(list):

    g_matrix = np.array([
        [1, 0, 0, 1, 1, 0, 1],
        [0, 1, 0, 1, 0, 1, 1],
        [0, 0, 1, 0, 1, 1, 1]
    ])

    u_bit_string = np.array(list)

    code_word = np.dot(u_bit_string, g_matrix)

    for i in range(code_word.shape[0]):
        if code_word[i] > 1:
            code_word[i] = code_word[i] % 2


    return code_word



