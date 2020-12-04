# Algoritmos 4 - TP2

La segunda parte de este trabajo práctico consiste en desarrollar un programa en Scala que:

1. Levante los datos cargados en una base de datos usando Doobie.
2. Elimine las columnas que no agregan datos.
3. Haga un split 70/30 de esos datos en training y test set.
4. Entrene un Random Forest Regressor en Spark. 
5. Guarde el modelo PPML.

## Prerrequisitos

Para ejecutar el código debe tenerse instalado [SBT](https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html).

## Instrucciones

### Ejecución del programa

Por el momento el programa se ejecuta correctamente en IntelliJ idea, pero no en SBT por un problema de dependencias de
Spark.

El programa no acepta argumentos y muestra por pantalla la separación entre sets de entrenamiento y testing.

### Output

Al ejecutar el pipeline, se escriben dos archivos de salida:

- `results.txt`: Contiene el RME resultante de aplicar el modelo entrenado al test set. 
- `model.pmml`: Archivo que contiene el modelo entrenado para futuras ejecuciones, independiente del lenguaje. 

## Descripción de la Solución

### Estructura del Proyecto

```bash
src/
- main/scala/fiuba/fp/
--- models/models.scala # Modelo en Scala de un DataFrameRow
--- Run.scala # Archivo principal de ejecución, con la descripción del pipeline FS2.
--- DB.scala # Archivo con utilidades para leer DataFrameRows de la BDD.
--- WightedCoin # Clase para simular eventos aleatorios booleanos con probabilidad.
--- Splitter # Clase para agregar elementos a una de dos listas al azar.
--- SparkRegressor # Archivo con utilidades para entrenar y testear modelos de machine learning.
--- Persistence # Módulo con métodos para persistir los resultados de train y test.
```

### FS2 Pipeline

La obtención y procesamiento de los datos sucede en un pipeline de FS2 con las siguientes etapas:

- Lectura de la base de datos utilizando el objeto DB (wrapper sobre Doobie).
- Fold que va alimentando con cada `DataFrameRow` a `Splitters` hasta quedar uno con todas filas leídas. 
  Es decir, el resultado de esta etapa será un `Stream(Splitter[DataFrameRow])` con un solo elemento. 
- Se mapea el `Splitter` a sus dos listas, la primera para un training set y la segunda para un test set.
- Se mapean los sets al `SparkRegressor` para su procesamiento.
- Los resultados del `SparRegressor` (schema, resultado de test y modelo) se pasan al módulo de persistencia para grabar
  los resultados a disco.

### Random Forest Regressor

Dentro del SparkRegressor se toman el train y test set y se siguen los siguientes pasos:

- Se crea un assembler con las features (todas ls columnas menos `close`).
- Se crea un Indexer con el target/label `close`.
- Se crea un regresor con los hiperparámetros.
- Los tres pasos anteriores se convierten en un pipeline de spark, que pasa a 
ajustar un modelo. Con este modelo se hacen dos cosas:
  - Se guarda un PMML.
  - Se devuelve el resultado de ajustar el modelo al test set.


