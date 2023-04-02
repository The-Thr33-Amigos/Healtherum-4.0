import pickle
import numpy as np

class kidney_disease_ml:

    def train_model(self):
        with open("ML/best_kd_model.pkl", "rb") as model_file:
            loaded_kd_model = pickle.load(model_file)

        with open("ML/kd_scaler.pkl", "rb") as scaler_file:
            loaded_scaler = pickle.load(scaler_file)

        Bp = float(np.random.randint(50,180,1))
        Sg = np.random.uniform(low=1.005,high=1.025,size=1)[0]
        Al = float(np.random.randint(0, 5, 1))
        Su = float(np.random.randint(0, 5, 1))
        Rbc = float(np.random.randint(0, 1, 1))
        Bu = np.random.uniform(low=1.5, high=391, size=1)[0]
        Sc = np.random.uniform(low=0.4, high=48.1, size=1)[0]
        Sod = float(np.random.randint(104, 163, 1))
        Pot = np.random.uniform(low=2.5, high=47.0, size=1)[0]
        Hemo = np.random.uniform(low=3.1, high=17.8)
        Wbcc = float(np.random.randint(2200, 26400, 1))
        Rbcc = np.random.uniform(low=2.1, high=8.0, size=1)[0]
        Htn = float(np.random.randint(0, 1, 1))


        
        num_scale = [Bp, Sg, Al, Su, Bu, Sc, Sod, Pot, Hemo, Wbcc, Rbcc]
        num_scale = np.array(num_scale).reshape(1, -1)
        synth_num_scaled = loaded_scaler.transform(num_scale)
    
        
        x_cat = np.array([Rbc, Htn]).reshape(1,-1)
        synth_scaled = np.concatenate((synth_num_scaled, x_cat), axis=1)

        synth_pred = loaded_kd_model.predict(synth_scaled)

        return ["0.99", synth_pred[0]]





