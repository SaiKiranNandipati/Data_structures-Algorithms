
/**
 * created on October 25th, 2023
 * implemented by Sai Kiran Nandipati

 */

/**
 * Copyright SAI KIRAN NANDIPATI October 25th, 2023. 
 *
 * This code is the property of Sai Kiran Nandipati. No part of this code
 * may be copied, modified, or distributed without the express written permission
 * of the copyright owner.
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class Point {
    double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Point other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
}

class ClosestPairResult {
    Point point1;
    Point point2;
    double distance;

    public ClosestPairResult(Point point1, Point point2, double distance) {
        this.point1 = point1;
        this.point2 = point2;
        this.distance = distance;
    }
}

public class closestPair {

    private static long runTime = 0;

	public static Point[] sortByX(Point[] points) {
        Arrays.sort(points, Comparator.comparingDouble(p -> p.x));
        return points;
    }

    public static Point[] sortByY(Point[] points) {
        Arrays.sort(points, Comparator.comparingDouble(p -> p.y));
        return points;
    }
    
    public static ClosestPairResult findClosestPair(Point[] P) {
    	long startTime = System.currentTimeMillis();

    	Point[] Px = sortByX(P.clone());
    	
        ClosestPairResult result= closestPairRecursive(P, Px);
        
        long endTime = System.currentTimeMillis();
        runTime = endTime - startTime;

        return result;
    }
    
    private static ClosestPairResult bruteForceClosestPair(Point[] P) {
        int n = P.length;
        if (n < 2) {
            return null; // Not enough points to form a pair
        }

        double minDistance = Double.POSITIVE_INFINITY;
        Point closestPoint1 = null;
        Point closestPoint2 = null;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = P[i].distance(P[j]);
                if (dist < minDistance) {
                    minDistance = dist;
                    closestPoint1 = P[i];
                    closestPoint2 = P[j];
                }
            }
        }

        return new ClosestPairResult(closestPoint1, closestPoint2, minDistance);
    }

    private static ClosestPairResult closestPairRecursive(Point[] P, Point[] Px) {
    	if (P.length <= 3) {
    		return bruteForceClosestPair(P);
    	}
    	
    	int mid = Px.length / 2;
        Point[] Q = Arrays.copyOfRange(Px, 0, mid);        
        Point[] Qx = sortByX(Q.clone());
        
        Point[] R = Arrays.copyOfRange(Px, mid, Px.length);
        Point[] Rx = sortByX(R.clone());
        
        
        ClosestPairResult QRes = closestPairRecursive(Q, Qx);
        ClosestPairResult RRes = closestPairRecursive(R, Rx);
        
        Double delta = Math.min(QRes.distance, RRes.distance);
        
        Double xStar = Q[Q.length - 1].x;
        
        List<Point> SList = new ArrayList<>();

        for (Point point : P) {
            if (point.x >= (xStar - delta) && point.x <= (xStar + delta)) {
                SList.add(point);
            }
        }

        // Convert the List<Point> to an array of Point[]
        Point[] S = SList.toArray(new Point[0]);

        Point[] Sy = sortByY(S.clone());
        
        ClosestPairResult SRes = null;
        
        int n = Sy.length;
        int k = 15;

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < Math.min(n, i + k); j++) {
                double dist = Sy[i].distance(Sy[j]);
                if (SRes == null || dist < SRes.distance) {
                	SRes = new ClosestPairResult(Sy[i], Sy[j], dist);
                }
            }
        }
        
        if (SRes != null && SRes.distance < delta) {
            return SRes;
        } else if (QRes.distance < RRes.distance) {
            return QRes;
        } else {
            return RRes;
        }
    }

    public static void main(String[] args) throws IOException {
   	    String fileName = args[0];
		BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        List<Point> pointList = null;
        while ((line = br.readLine()) != null) {
        	if(line.startsWith("**")) {
        		String firstLine = line.substring(3).split(":")[0];
        		System.out.print(firstLine + ":\t");
        		pointList = new ArrayList<>();
        	}
        	else if(line.startsWith("(")) {
        		// keep on adding points
        		// exclude the (
        		String x = line.split(", ")[0].substring(1);
        		String y = line.split(", ")[1];
        		// exclude the )
        		y = y.substring(0, y.length() - 1);
        		pointList.add(new Point(Double.parseDouble(x), Double.parseDouble(y)));
        	}
        	else if(line.startsWith("--")) {
        		Point[] points = pointList.toArray(new Point[0]);
        		System.out.println(points.length + " points");
        		
        		ClosestPairResult result = findClosestPair(points);
        		
        		System.out.println(String.format("\t(%10.5f, %10.5f)-(%10.5f, %10.5f)", result.point1.x, result.point1.y, result.point2.x, result.point2.y));
				System.out.println(String.format("\tdistance = %11.6f (%d ms)\n", result.distance, runTime));
        	}
        }
        br.close();
        System.out.println("by Sai Kiran Nandipati");
    }

}
