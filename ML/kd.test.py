import pickle
import numpy as np # linear algebra
import pandas as pd
from sklearn.ensemble import RandomForestClassifier # data processing, CSV file I/O (e.g. pd.read_csv)
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score, classification_report, confusion_matrix
from sklearn.model_selection import StratifiedKFold, cross_val_score

data = pd.read_csv("new_model.csv")
data = data.drop(21)
# seperating columns, and target
numerical_cols = ['Bp', 'Sg', 'Al', 'Su', 'Bu', 'Sc', 'Sod', 'Pot', 'Hemo', 'Wbcc', 'Rbcc']
x_num = data[numerical_cols]
x_cat = data[['Rbc', 'Htn']]
y = data['Class']
# scaling columns
scaler = StandardScaler()
x_num_scaled = scaler.fit_transform(x_num)
x_scaled = np.concatenate((x_num_scaled, x_cat), axis=1)


X_train, X_test, y_train, y_test = train_test_split(x_scaled, y, test_size=0.5, random_state=42)

# went with random forest with 99%, possible overfitting
model = RandomForestClassifier(n_estimators=100, random_state=42)
model.fit(X_train, y_train)

y_pred = model.predict(X_test)

acc = accuracy_score(y_test, y_pred)
print(f"Accuracy: {acc:.2f}")
conf = confusion_matrix(y_test, y_pred)
print("Confusion Matrix:\n", conf)
report = classification_report(y_test, y_pred)
print("Classification Report:\n", report)

# Cross validation with 5 and 10 splits was 97-99%
n_splits = 5
cv = StratifiedKFold(n_splits=n_splits, shuffle=True, random_state=42)
cv_scores = cross_val_score(model, x_scaled, y, cv=cv, scoring='accuracy')

print(f"Cross-validation scores: {cv_scores}")
print(f"Average cross-validation accuracy: {cv_scores.mean():.2f}")

with open("best_kd_model.pkl", "wb") as kd_model:
    pickle.dump(model, kd_model)

with open("kd_scaler.pkl", "wb") as kd_scaler:
    pickle.dump(scaler, kd_scaler)