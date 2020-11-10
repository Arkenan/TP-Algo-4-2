# Segunda Parte

La segunda parte consiste en crear un modelo de ML a partir de nuestra data en la db, para esto sera necesario hacer un programa
que levante la data de la db (en doobie), convierta los valores a numeros, parta la data en train y test (70/30). Para finalmente
pasarsela a un pipeline de spark que nos cree un modelo Rando Forest regressor y sea persistido en formato PMML.