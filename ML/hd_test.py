import pickle
import pandas as pd
from sklearn.model_selection import StratifiedKFold, train_test_split, StratifiedKFold, cross_val_score, GridSearchCV
from sklearn.neighbors import KNeighborsClassifier
from sklearn.preprocessing import StandardScaler


data_set = pd.read_csv("Heart_Disease_Prediction.csv")
data_set.columns = ['Age', 'Sex', 'Chest pain type', 'BP', 'Cholesterol', 'FBS over 120', 'EKG results', 'Max HR', 'Exercise angina', 'ST depression', 'Slope of ST', 'Number of vessels fluro', 'Thallium', 'Heart Disease']
# Age,Sex,Chest pain type,BP,Cholesterol,FBS over 120,EKG results,Max HR,Exercise angina,ST depression,Slope of ST,Number of vessels fluro,Thallium,Heart Disease
# Seperating test data
data_set['Heart Disease'] = data_set['Heart Disease'].replace({'Absence': 0, 'Presence': 1})
x = data_set.iloc[:, :-1]
y = data_set.iloc[:, -1]


# Splitting test data
X_train, X_test, y_train, y_test = train_test_split(x, y, test_size=0.4, random_state=1384)

# preprocessing
scaler = StandardScaler()
X_train = scaler.fit_transform(X_train)
X_test = scaler.transform(X_test)

X_scaled = scaler.fit_transform(x)

cv = StratifiedKFold(n_splits=5, shuffle=True, random_state=42)

knn_model = KNeighborsClassifier()
knn_model.fit(X_train, y_train)

param_grid_knn = {
    'n_neighbors': range(1, 31),
    'weights': ['uniform', 'distance'],
    'metric': ['euclidean', 'manhattan', 'minkowski']
}

grid_search_knn = GridSearchCV(knn_model, param_grid_knn, cv=cv, scoring='accuracy', n_jobs=-1)
grid_search_knn.fit(X_scaled, y)

best_params_knn = grid_search_knn.best_params_
print(f"KNN Best Paramaters: {best_params_knn}")

best_knn_model = KNeighborsClassifier(**best_params_knn)
best_knn_model.fit(X_train, y_train)

cv_scores_knn = cross_val_score(best_knn_model, X_scaled, y, cv=cv, scoring='accuracy')
print(f"KNN Cross-validation scores: {cv_scores_knn}")
print(f"KNN Average cross-validation accuracy: {cv_scores_knn.mean():.2f}")

with open("best_knn_model.pkl", "wb") as hd_model:
    pickle.dump(best_knn_model, hd_model)

with open("hd_scaler.pkl", "wb") as hd_scaler:
    pickle.dump(scaler, hd_scaler)


