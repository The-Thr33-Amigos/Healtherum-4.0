import pickle
import pandas as pd
from sklearn.discriminant_analysis import StandardScaler


from sklearn.model_selection import GridSearchCV, StratifiedKFold, cross_val_score, train_test_split

import xgboost as xgb



data = pd.read_csv("diabetes.csv")
# KNN cross-val = 0.76
# Log 0.75
# RF 0.775, bootstrap=false, max_depth=none, min_sample_leaf=4, min_split=2 n_est=200
# Svm 0.77


"""
Pregnancies = num of times pregnant (0-17)
Glucose = Plasma glucose concentration (0-199)
BloodPressure = Diastolic BP (0-122)
SkinThickness = Triceps skin fold thickness (mm) - (0 - 99)
Insulin = 2-Hour Serum insulin (ml) - (0-846)
BMI = Body Mass Index (0-67.1) ---- BMI = weight(kg) / (height(m)) ^ 2
DiabetesPedigreeFunction = (0.08-2.42)
Age = (21-81)
Outcome(Target) - (0-1)
 """

x = data.drop(columns=['Outcome'])
y = data['Outcome']

X_train, X_test, y_train, y_test = train_test_split(x, y, test_size=0.4, random_state=42)


scaler = StandardScaler()
X_train = scaler.fit_transform(X_train)
X_test = scaler.transform(X_test)

X_scaled = scaler.fit_transform(x)

cv = StratifiedKFold(n_splits=5, shuffle=True, random_state=42)

xgb_model = xgb.XGBClassifier()
xgb_model.fit(X_train, y_train)

param_grid_xgb = {
    'learning_rate': [0.01, 0.1, 0,2],
    'n_estimators': [50, 100, 200],
    'max_depth': range(1, 11),
    'subsample': [0.5, 0.8, 1],
    'colsample_bytree': [0.5, 0.8, 1]
}


grid_search_xgb = GridSearchCV(xgb_model, param_grid_xgb, cv=cv, scoring='accuracy', n_jobs=-1)
grid_search_xgb.fit(X_scaled, y)

best_params_xgb = grid_search_xgb.best_params_
print(f"XGBoost Best Parameters: {best_params_xgb}")

best_xgb_model = xgb.XGBClassifier(**best_params_xgb)
best_xgb_model.fit(X_train, y_train)

cv_scores_xgb = cross_val_score(best_xgb_model, X_scaled, y, cv=cv, scoring='accuracy')
print(f"XGBoost Cross-validation scores: {cv_scores_xgb}")
print(f"XGBoost Average cross-validation accuracy: {cv_scores_xgb.mean():.2f}")


with open("best_db_model.pkl", "wb") as model_file:
    pickle.dump((best_xgb_model, scaler), model_file)










