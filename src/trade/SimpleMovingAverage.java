package trade;

import java.util.LinkedList;
import java.util.Queue;

/*
 * Starting point for the code was taken from:
 * from: http://rosettacode.org/wiki/Averages/Simple_moving_average#Java
 */
public class SimpleMovingAverage {
    private Queue<Double> window = new LinkedList<Double>();
    private int period;
    private double sum = 0;
 
    public SimpleMovingAverage(int period) {
        assert period > 0 : "Period must be a positive integer";
        this.period = period;
    }
    
    /*
     * reset average to  0 (technically undefined)
     */
    public void reset() {
    	window.clear();
    	sum = 0;
    }
 
    public void newNum(double num) {
        sum += num;
        window.add(num);
        if (window.size() > period) {
            sum -= window.remove();
        }
    }
 
    public double getAvg() {
        if (window.isEmpty()) return 0; // technically the average is undefined
        return sum / window.size();
    }
 
}