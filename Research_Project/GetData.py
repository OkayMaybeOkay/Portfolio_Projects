from Projects.TrialsAndErrorPercentBigH import sample_error_data

count_FER = 0
total_FER = 0
count_BER = 0
total_BER = 0


for i in range(10):
    print("EXPERIMENT: " + str(i+1))
    print("\n")


    placeholder_list = sample_error_data(0.1, "b")
    count_FER = placeholder_list[0]
    count_BER = placeholder_list[1]

    total_FER += count_FER
    total_BER += count_BER

    print()
print("\nRESULTS")
print("The Average FER: " + str(total_FER/10))
print("The Average BER: " + str(total_BER/10))


