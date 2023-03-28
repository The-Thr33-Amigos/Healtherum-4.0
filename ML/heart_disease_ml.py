import numpy as np # linear algebra
import pandas as pd # data processing, CSV file I/O (e.g. pd.read_csv)

from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score, classification_report, confusion_matrix

class heart_disease_ml:
    def __init__(self, training):
        self.training = training

    def train_model(self):

        # Heart Disease Training Data
        data_set = pd.read_csv("ML/Heart_Disease_Prediction.csv")

        # Age,Sex,Chest pain type,BP,Cholesterol,FBS over 120,EKG results,Max HR,Exercise angina,ST depression,Slope of ST,Number of vessels fluro,Thallium,Heart Disease
        # Seperating test data
        x = data_set.iloc[:, :-1]
        y = data_set.iloc[:, -1]

        # # Load dataset
        # url = "ML/Heart_Disease_Prediction.csv"
        # col_names = ['Age', 'Sex', 'Chest pain type', 'BP', 'Cholesterol', 'FBS over 120', 'EKG results', 'Max HR', 'Exercise angina', 'ST depression', 'Slope of ST', 'Number of vessels fluro', 'Thallium', 'Heart Disease']
        # pima = pd.read_csv(url, header=None, names=col_names)
        # # Selecting the features
        # feature_cols = ['Age', 'Sex', 'BP', 'Cholesterol', 'FBS over 120']
        # X = pima[feature_cols]
        # y = pima.label
        # Feature scaling
        # scaler = MinMaxScaler()
        # X_scaled = scaler.fit_transform(X)


        # Splitting test data
        X_train, X_test, y_train, y_test = train_test_split(x, y, test_size=0.2, random_state=42)

        # X_train = pd.DataFrame(X_train, columns=x.columns)
        # X_test = pd.DataFrame(X_test, columns=x.columns)

        # preprocessing
        scaler = StandardScaler()
        X_train = scaler.fit_transform(X_train)
        X_test = scaler.transform(X_test)

        # Using Logistic Regression for model
        model = LogisticRegression()
        model.fit(X_train, y_train)

        # Prediction
        y_pred = model.predict(X_test)

        # Model Score, etc
        acc = accuracy_score(y_test, y_pred)
        # print(f"Accuracy: {acc:.2f}")

        conf = confusion_matrix(y_test, y_pred)
        # print("Confusion Matrix:\n", conf)

        report = classification_report(y_test, y_pred)
        # print("Classification Report:\n", report)

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
        synth_scale = scaler.transform(synth)
        synth_pred = model.predict(synth_scale)
        # print(synth_pred)

        return [acc,synth_pred[0]]



