import pickle
import numpy as np # linear algebra
import pandas as pd # data processing, CSV file I/O (e.g. pd.read_csv)

from sklearn.model_selection import StratifiedKFold, cross_val_score, train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score, classification_report, confusion_matrix

class heart_disease_ml:
    def __init__(self, training):
        self.training = training

    def train_model(self):

        with open("ML/best_knn_model.pkl", "rb") as model_file:
            loaded_knn_model = pickle.load(model_file)

        with open("ML/hd_scaler.pkl", "rb") as scaler_file:
            loaded_scaler = pickle.load(scaler_file)

        
        age = np.random.randint(29,79,1)
        sex = self.training[0]
        cp = np.random.randint(0, 4, 1)
        sys = self.training[1]
        chol = np.random.randint(126, 567, 1)
        fbs = np.random.randint(0, 2, 1)
        restecg = np.random.randint(0, 3, 1)
        hr = self.training[2]
        exang = np.random.randint(0, 2, 1)
        oldpeak = np.random.uniform(0, 6.2, 1)
        slope = np.random.randint(0, 3, 1)
        ca = np.random.randint(0, 5, 1)
        thal = np.random.randint(0, 4, 1)

        synth = synth = np.column_stack((age, sex, cp, sys, chol, fbs, restecg, hr, exang, oldpeak, slope, ca, thal))
        synth_scale = loaded_scaler.transform(synth)
        synth_pred = loaded_knn_model.predict(synth_scale)


        return ["0.86",synth_pred[0]]



