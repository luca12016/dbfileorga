MitgliederDB ist die abstrakte Oberklasse (enthält die gemeinsame Logik) und definiert die Operationen als abstrakte Methoden.
MitgliederDBUnordered und MitgliederDBOrdered sind Unterklassen, die von MitgliederDB erben und die abstrakten Methoden für ihre jeweilige Dateiorganisation implementieren.

Zum Starten der DB muss jeweils ein Objekt von MitgliederDBUnordered bzw. MitgliederDBOrdered erzeugt werden. Auf diesen Objekten können zum Testen der Operationen die Methoden aufgerufen werdne.