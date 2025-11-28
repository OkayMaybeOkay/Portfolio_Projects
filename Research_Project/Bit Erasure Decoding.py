import numpy as np

"""
Here are the constants that we need to start creating the Erasure Decoding Process.
They will consist of: The corrupted code word (which is inside the function), the H matrix, max iterations, and
an E matrix. The corrupted code is what we're trying to figure out, the H matrix is what does the solving, 
the max is for max attempts, and the E matrix is the matrix that will, for each keyword bit, have the opinion
of each check node. This means E must equal H in terms of size.
"""

"""
Description: Takes in a corrupted keyword (1 by 7) and decodes it using erasure decoding
Depends on: numpy
Parameters: input_list (list data type, 1 by 7)
Returns: m_sub_i (array data type, 1 by 7),
         "Decoding Failed: ... Iterations" (string data type, ... is your Max Iterations number)
"""

"Now it's time to make the function that can decode keywords"

def erasure_decode(input_list):

    "H Matrix"
    h_matrix = np.array([
        [1, 1, 0, 1, 0, 0, 0],
        [1, 0, 1, 0, 1, 0, 0],
        [0, 1, 1, 0, 0, 1, 0],
        [1, 1, 1, 0, 0, 0, 1],
        [1, 1, 1, 1, 1, 1, 1]
    ], dtype=object)

    "Max iterations"
    max_iterations = 1000

    "The E Matrix"
    e_matrix = np.full(
        h_matrix.shape, "N", dtype=object
    )

    """The corrupted code word"""
    m_sub_i = np.array(input_list, dtype = object)

    "This for will be for repeating the process of decoding a keyword"
    for iterations in range(max_iterations):

        "This for loop is for checking the rows of the H matrix and giving the positions of the 1's"
        for j_row in range(h_matrix.shape[0]):
            particular_row = h_matrix[j_row, :]
            particular_row_tuple = np.where(particular_row == 1)
            ones_places_for_row = particular_row_tuple[0]

            "This for loop takes a specific position in the row list and gives another list without that position"
            for i_column in ones_places_for_row:
                current_specific_position_tuple = np.where(ones_places_for_row == i_column)
                current_specific_position = current_specific_position_tuple[0]
                position_of_other_ones = np.delete(ones_places_for_row, current_specific_position)

                "This is to make an empty matrix to store the keyword bits using positions of other 1's"
                keyword_summation_array = np.empty(0)

                "This for loop is to give a list of the bits in the codeword"
                for keyword_bit_positions in position_of_other_ones:
                    keyword_bit = m_sub_i[keyword_bit_positions]
                    keyword_summation_array = np.append(keyword_summation_array, keyword_bit)

                "This if then statement is to check if all the bits in the list are known. If the bits are known we do"
                "mod 2 and if not, we put it as 'x'. These answers are then put into the E matrix."
                if "x" in keyword_summation_array:
                    e_matrix[j_row, i_column] = "x"
                else:
                    new_keyword_bit = np.sum(keyword_summation_array)
                    e_matrix[j_row, i_column] = int(new_keyword_bit % 2)

        "Now this will store each keyword bit"
        for i_column_again in range(h_matrix.shape[1]):
            checking_x = m_sub_i[i_column_again]

            "Now we check if they are 'x's"
            if checking_x == "x":

                "If they are x, then we go through each element in the E Matrix column vector if it's non x"
                for j_row_again in range(h_matrix.shape[0]):
                    e_matrix_bit = e_matrix[j_row_again, i_column_again]

                    "If the E Matrix bit is a number, it will set that number as its new keyword"
                    #Note that there might be a problem with using this because it chooses 0's over 1's
                    if e_matrix_bit in (1, 1.0, 0.0, 0):
                        m_sub_i[i_column_again] = e_matrix_bit

        "Now we check if there are any x's in the keyword, and if not, we have the keyword"
        if 'x' not in m_sub_i:
            #print("It took " + str(iterations + 1) + " iteration(s) to find the code word: " + str(m_sub_i))
            return m_sub_i

        "If there is still unknowns in the keyword and the max iterations have been reached, we say it reached the max"
        if (iterations + 1) == max_iterations:
            #print("Max iterations have been reached: " + str(max_iterations))
            #print("Decoding failed")
            return m_sub_i

