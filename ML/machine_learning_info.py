# Model examples
cv = StratifiedKFold(n_splits=5, shuffle=True, random_state=42)


# basic svm params, and traing
svm_model = SVC()
svm_model.fit(X_train, y_train)

param_grid_svm = {
    'C': [0.1, 1, 10, 100],
    'gamma': [1, 0.1, 0.01, 0.0001],
    'kernel': ['linear', 'rbf']
}

grid_search_svm = GridSearchCV(svm_model, param_grid_svm, cv=cv, scoring='accuracy', n_jobs=-1)
grid_search_svm.fit(X_scaled, y)

best_params_svm = grid_search_svm.best_params_
print(f"SVM Best Parameters: {best_params_svm}")

best_svm_model = SVC(**best_params_svm)
best_svm_model.fit(X_train, y_train)

cv_scores_svm = cross_val_score(best_svm_model, X_scaled, y, cv=cv, scoring='accuracy')

print(f"SVM Cross-validation scores: {cv_scores_svm}")
print(f"SVM Average cross-validation accuracy: {cv_scores_svm.mean():.2f}")

# decision tree
dt_model = DecisionTreeClassifier()
dt_model.fit(X_train, y_train)

param_grid_dt = {
    'criterion': ['gini', 'entropy'],
    'max_depth': range(1, 31),
    'min_samples_split': range(2, 11),
    'min_samples_leaf': range(1, 11)
}

grid_search_dt = GridSearchCV(dt_model, param_grid_dt, cv=cv, scoring='accuracy', n_jobs=-1)
grid_search_dt.fit(X_scaled, y)

best_params_dt = grid_search_dt.best_params_
print(f"Decision Tree Best Parameters: {best_params_dt}")

best_dt_model = DecisionTreeClassifier(**best_params_dt)
best_dt_model.fit(X_train, y_train)

cv_scores_dt = cross_val_score(best_dt_model, X_scaled, y, cv=cv, scoring='accuracy')
print(f"Decision Tree Cross-validation scores: {cv_scores_dt}")
print(f"Decision Tree Average cross-validation accuracy: {cv_scores_dt.mean():.2f}")


# Naive Bayes
nb_model = GaussianNB()
nb_model.fit(X_train, y_train)

cv_scores_nb = cross_val_score(nb_model, X_scaled, y, cv=cv, scoring='accuracy')
print(f"Naive Bayes Cross-validation scores: {cv_scores_nb}")
print(f"Naive Bayes Average cross-validation accuracy: {cv_scores_nb.mean():.2f}")

# XGB target needs to be 0,1
# XGBoost
xgb_model = XGBClassifier()
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

best_xgb_model = XGBClassifier(**best_params_xgb)
best_xgb_model.fit(X_train, y_train)

cv_scores_xgb = cross_val_score(best_xgb_model, X_scaled, y, cv=cv, scoring='accuracy')
print(f"XGBoost Cross-validation scores: {cv_scores_xgb}")
print(f"XGBoost Average cross-validation accuracy: {cv_scores_xgb.mean():.2f}")

# LightGBM
lgbm_model = LGBMClassifier()
lgbm_model.fit(X_train, y_train)

# Hyperparameter tuning for LightGBM
param_grid_lgbm = {
    'learning_rate': [0.01, 0.1, 0.2],
    'n_estimators': [50, 100, 200],
    'max_depth': range(1, 11),
    'subsample': [0.5, 0.8, 1],
    'colsample_bytree': [0.5, 0.8, 1]
}

grid_search_lgbm = GridSearchCV(lgbm_model, param_grid_lgbm, cv=cv, scoring='accuracy', n_jobs=-1)
grid_search_lgbm.fit(X_scaled, y)

best_params_lgbm = grid_search_lgbm.best_params_
print(f"LightGBM Best Parameters: {best_params_lgbm}")

best_lgbm_model = LGBMClassifier(**best_params_lgbm)
best_lgbm_model.fit(X_train, y_train)

cv_scores_lgbm = cross_val_score(best_lgbm_model, X_scaled, y, cv=cv, scoring='accuracy')
print(f"LightGBM Cross-validation scores: {cv_scores_lgbm}")
print(f"LightGBM Average cross-validation accuracy: {cv_scores_lgbm.mean():.2f}")