/**
 * Author : Mamel Alboury Ndiaye
 */
import java.util.ArrayList;

public interface Annealing<T> {
    /** Fonction de annealing (recuit simulé) sans fitness */
    default T annealing(T element0, double time0, double IterationNumberTime, double GrowthRateTime ) {
        T x = element0;
        double t = time0;
        while( !StopCondition( x ) ) {
            for( int m=0; m < IterationNumberTime; m++ ) {
                T y = neighbor( x );
                double dF = quality( y ) - quality( x );
                if( accepted( dF, t ) ) {
                    x = y;
                }
            }
            t = GrowthRateTime * t;
        }
        return x;
    }

    /** Fonction de annealing (recuit simulé) avec fitness */
    default T annealing(T element0, double time0, double IterationNumberTime, double GrowthRateTime, float fitness) {

        T x = element0;
        double time = time0;
        ArrayList<Double> list = new ArrayList<>();
        while( !StopCondition( x ) ) {
            for( int counter=0; counter < IterationNumberTime; counter++ ) {
                T y = neighbor( x );

                if (time < 0.2 && isStagnant(list, fitness, quality( x ))) {
                    for (int i = 0; i < fitness * 0.1; i++) {
                        while (quality(y) - quality(x) < 0) {
                            y = neighbor(x);
                        }
                        x = y;
                    }
                } else if( accepted( quality( y ) - quality( x ), time ) ) {
                    x = y;
                    list.clear();
                }
                list.add(quality( x ));
                if (list.size() > fitness) {
                    list.remove(0);
                }

            }
            time = GrowthRateTime * time;
        }
        return x;
    }

    /** Fonction de annealing (recuit simulé) sans fitness */
    default boolean accepted(double dF, double t ) {
        if( dF >= 0 )
            if( Math.random() >= Math.exp( -dF/t ) )
                return false;
        return true;
    }

    /** Vérifie que la liste n'est pas complètement remplie par une unique valeur */
    default boolean isStagnant(ArrayList<Double> list, float fitness, double quality) {
        return list.stream().filter(elem -> elem == quality).count() == fitness;
    }

    T neighbor(T a );
    boolean StopCondition(T x );
    double quality(T x );

}
