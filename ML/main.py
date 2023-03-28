import sys
import numpy as np
import traceback
import warnings
from sklearn.exceptions import DataConversionWarning

from heart_disease_ml import heart_disease_ml

warnings.filterwarnings("ignore")

if __name__ == "__main__":

    try:
        sex, sysbp, hr = [int(i) for i in sys.argv[1:]]
        train_data = [sex, sysbp, hr]

        heart_disease = heart_disease_ml(train_data)

        result = heart_disease.train_model()
        acc = str(result[0])
        res = str(result[1])
        print(acc)
        print(res)
    except Exception as e:
        traceback.print_exc(file=sys.stderr)









