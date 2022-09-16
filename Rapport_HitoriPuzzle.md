#Hitori Puzzle
Mamel Alboury NDIAYE

## Le Travail demandé:

Le travail demandé est de développer un programme qui est capable de résoudre des Hitori Puzzles, le principe est de marquer certaines cases et pour cela on doit respecter quelques:
* Chaque ligne et chaque colonne de Hitori Puzzle ne doit contenir qu’une seule occurrence d’un chiffre donné.
* Les cases marquées ne doivent pas être adjacentes soit horizontalement ou verticalement, elles peuvent être uniquement adjacentes en diagonale.
* Les cases non-marquées doivent toutes être connectées entre elles par adjacence soit horizontalement ou verticalement.

## Le problème à résoudre pour développer notre programme:

->On utilise l'algorithme de (Annealing).

  -->On prend les valeur suivantes :

* la fitness égale à 200.
* la temperature de départ égale à 1,
* le Nombre d'Iteration de température égale à 100,
* le Taux de décroissance de la température égale à 0.99


-> Hitori Puzzle est représenté par un tableau de deux dimensions d'entiers.
-> Les cases marquées sont représentées par la valeur négative de la valeur de la case. 
-> Le changement de l'état des cases est possible sans provoquer une perte de données.

## La Fitness:

-> On a utilisé une liste sous forme de (`ArrayList`) pour la mémorisation des (`n`) dernières qualités gradées.
-> Si elle sont toutes identiques, le programme donc accepte un poucentage de 10 % de (`n`) fois une différence de qualité supérieure par rapport à l'ancienne valeur pour 
se permettre de sortir de l'état de blocage.

## Le fonctionnement de notre programme:

-> Lors de l'exécution de la méthode (main) de la classe  (HitoriPuzzle), le programme générera des voisins aléatoirement et en utilisant l'algotithme de (Annealing), il sera capable de grader les voisins avec une
meilleure qualité jusqu'à obtenir une qualité nulle qui seranotre solution.

-> Plus la valeur de la qualité est basse, mieux elle est car elle représente le nombre de fois où les règles n'ont pas été
respectées.
 
-> La qualité est calculée à partir des règles de (Hitori Puzzle), dans notre cas les règles sont celles de la section (Le travail demandé).
 
->Chaque règle doit retourner le nombre de fois où elle n'a pas été respectée.

## Les tests de notre programme:

 -> Le programme résout dans la méthode (main) deux Hitori Puzzles (15x15) et (20x20).
