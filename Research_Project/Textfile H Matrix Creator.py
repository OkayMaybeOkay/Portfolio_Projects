import numpy as np

"""
Description: Takes in a file name, reads it, stores each line into matrix (each line is also a matrix),
             and formats the file into a string which using eval, turns it into real code

             !!! WARNING !!! Eval uses string code to turn into real code, so if the code in the text file is malicious
             it can be used in your real code (Don't trust user input)

Depends on: Nothing (Lone wolf frfr)
Parameters: file_name (String data type, your text file's name)
Returns: A Matrix (Literal Code data type)
"""

def return_h_matrix (file_name):
    empty_string = "np.array(["

    with open(file_name, "r") as file:
        list_of_lines = file.readlines()

        for i in range(len(list_of_lines)):

            item = list_of_lines[i]
            new_item = item.strip()

            empty_string += "["

            for j in range(len(new_item)):

                empty_string += new_item[j]

                if (j != len(new_item) - 1):
                    empty_string += ", "

            if (i != len(list_of_lines) - 1):
                empty_string += "],"
                empty_string += "\n"

            else:
                empty_string += "]"
                empty_string += "], dtype = object)"

    return eval(empty_string)


"""
Description: Creates a list of 0's of ay length you desire

             !!! WARNING !!! Eval uses string code to turn into real code, this has no text file involved so it's safe
             but still be warned

Depends on: Nothing (Lone wolf frfr)
Parameters: num_zeros (Integer data type)
Returns: A list (Literal Code data type)
"""


def return_0_list (num_zeros):
    empty_string = "["

    for i in range(num_zeros):
        empty_string += "0"

        if i != num_zeros - 1:
            empty_string += ", "

        else:
            empty_string += " ]"

    return eval(empty_string)



