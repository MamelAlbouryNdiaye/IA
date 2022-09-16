/**
 * Author : Mamel Alboury Ndiaye
 */
public class HitoriPuzzle implements Annealing<Integer[][]> {
    final static float T0 = 1f;
    final static float Nt0 = 100f;
    final static float R = 0.99f;
    final static float F = 200;

    /** Exécute la fonction de annealing (recuit simulée) (annealing) */
    public boolean solver(Integer[][] puzz_tab)
    {
        System.out.println("La résolution des puzzles est en cours, veuillez attendre s'il vous plaît ...");
        puzz_tab = annealing(puzz_tab, T0, Nt0, R, F);
        printSolution(puzz_tab);
        return true;
    }

    /** Retourne un neighbor du tableou entrée avec une case choisie aleatoirement => passage de marquée à non marquée ou le contraire*/
    @Override
    public Integer[][] neighbor(Integer[][] integer) {
        Integer[][] tab = new Integer[integer.length][integer.length];
        for (int i = 0; i < integer.length; i++) {
            for (int j = 0; j < integer.length; j++) {
                tab[i][j] = integer[i][j];
            }
        }
        int x, y;
        do {
            x = (int) (Math.random() * integer.length);
            y = (int) (Math.random() * integer.length);
        } while (isMarked(tab, x, y) == 0);
        tab[x][y] = - tab[x][y];
        return tab;
    }

    /** Donne la condition d'arrêt pour la fonction de annealing (recuit simulé) */
    @Override
    public boolean StopCondition(Integer[][] x) {
        return this.quality(x) == 0;
    }

    /** Calcule la qualité de tableau pris en entrée */
    @Override
    public double quality(Integer[][] x) {
        int resultNonMarked = 0, resultMarked = 0, resultRelated = 0;
        Integer[][] puzzRelated = new Integer[x.length][x.length];

        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x.length; j++) {
                resultNonMarked += checkNonMarked(x, i, j);
                resultMarked += checkMarked(x, i, j);

                puzzRelated[i][j] = 0;
            }
        }

        resultRelated += checkRelated(x, puzzRelated);
        return resultNonMarked + resultMarked + resultRelated;
    }

    /** Vérifie que la case non marquée n'a pas d'autres occurrences horizontalement ou verticalement. Si le contraire
     * est vrai alors retourne le nombre occurrences */
    public int checkNonMarked(Integer[][] puzz, int i, int j) {
        int result = 0;
        if (puzz[i][j] > 0) {
            for (int k = 0; k < puzz.length; k++) {
                if (i != k && puzz[i][j].equals(puzz[k][j]))
                    result++;
                if (j != k && puzz[i][j].equals(puzz[i][k]))
                    result++;
            }
        }
        return result;
    }

    /** Vérifie que la case marquée n'est pas de case marquée adjacente horizontalement ou verticalement. Si le contraire
     * est vrai alors retourne le nombre occurrences */
    public int checkMarked(Integer[][] tab, int i, int j) {
        int result = 0;
        if (tab[i][j] < 0) {
            if (i + 1 < tab.length && tab[i + 1][j] < 0) {
                result++;
            }
            if (j + 1 < tab.length && tab[i][j + 1] < 0) {
                result++;
            }
        }
        return result;
    }

    /** Vérifie si le tableau est connexe ou ps. Si non, retourne le nombre de cases non connecté au reste */
    public int checkRelated(Integer[][] puzz, Integer[][] puzzRelated) {
        int countElementPuzzRelated = 0;
        int countElementPuzz = 0;
        int x = 0, y = 0;
        int tmp = 0;

        while (puzz[x][y] < 0) {
            tmp++;
            y = tmp % puzz.length;
            x = Math.floorDiv(tmp,  puzz.length);
            if(x == puzz.length) {
                return countElementPuzz;
            }
        }

        checkRelated(puzz, puzzRelated, x, y);

        for (int i = 0; i < puzz.length; i++) {
            for (int j = 0; j < puzz.length; j++) {
                if (puzz[i][j] > 0) {
                    countElementPuzz++;
                }
                if (puzzRelated[i][j] == 1) {
                    countElementPuzzRelated++;
                }
            }
        }

        return (countElementPuzz - countElementPuzzRelated);
    }

    /** Appel récursif sur les cases non marquée voisine et ajouté la case au tebleau des valeurs connexes */
    public void checkRelated(Integer[][] puzz, Integer[][] puzzRelated, int x, int y) {
        puzzRelated[x][y] = 1;
        if (x + 1 < puzz.length && puzz[x + 1][y] > 0 && puzzRelated[x + 1][y] != 1) {
            checkRelated(puzz, puzzRelated, x + 1, y);
        }
        if (x - 1 >= 0 && puzz[x - 1][y] > 0 && puzzRelated[x - 1][y] != 1) {
            checkRelated(puzz, puzzRelated,x - 1, y);
        }
        if (y + 1 < puzz.length && puzz[x][y + 1] > 0 && puzzRelated[x][y + 1] != 1) {
            checkRelated(puzz, puzzRelated,x, y + 1);
        }
        if (y - 1 >= 0 && puzz[x][y - 1] > 0 && puzzRelated[x][y - 1] != 1) {
            checkRelated(puzz, puzzRelated,x, y - 1);
        }
    }

    /** Vérifie que la case peut être marquée en retournant  */
    public int isMarked(Integer[][] puzz, int i, int j) {
        int result = 0;
        for (int k = 0; k < puzz.length; k++) {
            if (i != k && Math.abs(puzz[i][j]) == (Math.abs(puzz[k][j]))) {
                result++;
            }
            if (j != k && Math.abs(puzz[i][j]) == (Math.abs(puzz[i][k]))) {
                result++;
            }
        }
        return result;
    }

    /** Fonction pour imprimer la solution*/
    void printSolution(Integer[][] board)
    {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++)
                System.out.printf("| %2s " , (board[i][j] > 0 ? board[i][j] : "."));
            System.out.println("|");
        }
    }

    /** Dans cette méthode principale on va résoudre les deux puzzles 15*15 et 20*20 */
    public static void main(String[] args) {

        /** Le puzzle de 15*15 */
        Integer[][] puzz15 = {
                {11, 12,  4, 11, 11,  9,  6, 10, 14,  6,  5,  6,  8,  6,  3},
                {15,  7,  2, 12, 13,  9,  1,  5,  8, 10,  9,  4,  5, 11,  9},
                { 4, 15,  8,  9,  8,  1,  4, 11,  7,  6, 13,  7,  2,  4,  5},
                {10,  9,  5,  9, 15,  7,  4,  3,  7, 14,  9,  2,  9, 13,  9},
                {11, 14,  2,  6,  2, 12, 15,  2,  5,  2, 10,  2,  4,  2,  1},
                {14, 13,  8,  7,  9,  2,  7, 15,  4,  1,  7,  5, 14, 12,  4},
                { 9,  2, 10, 14,  1,  6,  5,  2, 15, 12,  6,  3, 11,  6,  7},
                { 7,  3,  7,  2, 10,  5,  6,  7,  4,  6,  8, 12, 10,  1, 10},
                {14,  2,  6,  4,  5, 13,  9,  1,  3, 13, 12,  2, 10,  7,  8},
                { 5,  6,  1,  5,  8, 14,  6, 12,  7, 11,  7,  8, 14, 10,  4},
                { 5, 10,  5, 11,  4,  7,  2,  5,  1,  7,  6, 15,  9,  7, 13},
                {13,  2, 15, 10, 12,  6, 10,  9,  2,  5, 11,  2,  3,  8, 14},
                { 2,  5,  2,  1,  2,  4,  8, 13,  6, 15,  2, 10,  2,  3, 12},
                {12,  2,  7, 15,  3, 15, 14,  6,  9,  4,  2,  9,  1,  5,  9},
                { 8,  1,  2, 13,  8, 10, 12,  4, 11,  2,  3,  4,  5,  2, 15}

        };


        /** Le puzzle de 20*20 */
        Integer[][] puzz20 = {
                {17, 20, 17,  8, 16,  4,  7,  2,  7,  3, 11, 19, 17, 10, 16, 17, 14, 16, 11,  9},
                {17, 18,  7, 13, 20,  2, 14,  2, 15,  2,  5, 11, 16, 12,  9,  3, 11,  1,  8, 18},
                {11,  9, 15, 10, 12, 14, 12, 13,  5,  1, 12,  4, 15,  3, 15, 20, 17, 12,  7,  6},
                { 8, 13, 11,  8,  5,  8,  9, 17,  8, 12,  3,  8, 19,  8,  2,  8,  1, 15, 10,  8},
                { 4,  7, 12, 11,  4,  2, 16,  5,  3,  5,  6, 14, 12, 15, 13, 18,  4,  8,  5, 19},
                { 5, 13, 20, 18,  9, 12, 13, 10, 18, 19,  7, 18,  2, 13,  4, 18,  6, 18, 17, 13},
                { 5, 11, 14,  3,  8,  5, 20,  5,  9, 18, 14,  6, 12,  4,  5, 19,  7, 13,  5, 15},
                { 6, 10,  8,  2,  4, 19, 11, 18, 13, 16, 15, 16,  1,  9, 20, 12, 20,  2, 14, 18},
                { 9, 15,  4,  2,  4, 18, 15, 11, 15, 17, 15, 10,  5, 15,  7,  4,  8,  6,  4, 14},
                {17,  1, 19,  7, 11,  7,  6,  7,  4, 17, 10, 15,  7,  2, 17, 16,  5, 17,  9, 17},
                { 3,  4,  4,  7, 19, 20,  5, 14,  8, 15, 12,  4, 17, 19,  1,  5, 16, 10,  4, 11},
                {18,  2,  3,  9,  3, 15, 17,  1, 18, 11,  8, 13, 18,  5, 18,  4,  3, 19, 20,  3},
                {19,  5, 13,  5,  1, 11,  5, 15, 16,  6,  5,  9,  8,  5,  10, 5, 18,  4,  5, 20},
                {20, 17, 16, 19, 18, 20,  1, 16, 18, 13, 14,  5,  3,  8, 14,  9, 10, 20,  4, 20},
                { 8,  6,  1, 19, 14, 17, 14, 19, 12, 13, 11, 13, 20, 14,  5,  2,  8,  9, 13,  4},
                { 1, 17,  5, 16, 15,  9,  2,  6, 17,  4, 17,  8,  9, 20, 14, 17, 13, 17, 12,  9},
                {14,  5, 10,  1, 10,  9, 19, 14,  7, 14, 17,  3,  4,  3, 12,  8,  3, 20,  3, 13},
                {14, 16, 17,  4, 13,  1, 10,  9, 19,  8,  4,  7,  4, 18, 16, 10, 11, 16,  2,  1},
                {16, 14, 18, 15, 18,  8,  5, 18, 17, 12, 20, 12, 13, 12, 11,  1, 12,  7,  6,  2},
                {13, 19, 16, 19,  2,  5,  4, 12, 14, 10,  4, 18,  5, 17, 19,  7, 20, 14, 15,  4}
        };

        HitoriPuzzle hitoriPuzzle = new HitoriPuzzle();
        long start = System.currentTimeMillis();
        hitoriPuzzle.solver(puzz15);
        long elapsedTime = System.currentTimeMillis() - start;
        System.out.println("Le temps écoulé pour résoudre le puzzle 15*15 est de " + elapsedTime + " milliseconds");

        long start1 = System.currentTimeMillis();
        hitoriPuzzle.solver(puzz20);
        long elapsedTime1 = System.currentTimeMillis() - start1;
        System.out.println("Le temps écoulé pour résoudre le puzzle 20*20 est de " + elapsedTime1 + " milliseconds");






    }
}
