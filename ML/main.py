import sys
import numpy as np
import traceback
import warnings
from sklearn.exceptions import DataConversionWarning

from heart_disease_ml import heart_disease_ml
from kidney_disease_ml import kidney_disease_ml

warnings.filterwarnings("ignore")

if __name__ == "__main__":

    try:
        args_list = [int(i) for i in sys.argv[1:]]
        if args_list[0] == 5:
            sex = args_list[1]
            sysbp = args_list[2]
            hr = args_list[3]
            train_data = [sex, sysbp, hr]

            heart_disease = heart_disease_ml(train_data)

            result = heart_disease.train_model()
            acc = str(result[0])
            res = str(result[1])
            print(acc)
            print(res)
        elif args_list[0] == 1:
            kidney_disease = kidney_disease_ml()
            result = kidney_disease.train_model()
            acc = str(result[0])
            res = str(result[1])
            print(acc)
            print(res)
        elif args_list[0] == 2:
            print("N/A")
        else:
            print("N/A")
    except Exception as e:
        traceback.print_exc(file=sys.stderr)









