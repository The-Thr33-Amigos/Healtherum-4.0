import pickle
import numpy as np

class diabetes_ml:

    def train_model(self):
        with open("ML/best_db_model.pkl", "rb") as model_file:
            loaded_kd_model, loaded_scaler = pickle.load(model_file)
        
        # Synthetic data, can add a few "other fake" data but no reason since this is a simulation


        Pregnancies = np.random.randint(0, 17, 1)
        Glucose = np.random.randint(0, 199, 1)
        BloodPresssure = np.random.randint(36, 122, 1)
        SkinThickness = np.random.randint(0, 99, 1)
        Insulin = np.random.randint(0, 510, 1)
        BMI = np.random.uniform(low=2.0, high=65.0, size=1)[0]
        DiabetesPedrigreeFunction = np.random.uniform(low=0.08, high=2.42, size=1)[0]
        Age = np.random.randint(21, 81, 1)

        synth = synth = np.column_stack((Pregnancies, Glucose, BloodPresssure, SkinThickness, Insulin, BMI, DiabetesPedrigreeFunction, Age))
        synth_scale = loaded_scaler.transform(synth)
        synth_pred = loaded_kd_model.predict(synth_scale)


        return ["0.78", synth_pred[0]]

