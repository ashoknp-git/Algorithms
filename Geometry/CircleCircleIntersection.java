/**
 * This file shows you how to find the intersection of two circles.
 * See live demo: http://www.williamfiset.com/circlecircleintersection
 *
 * Time Complexity: O(1)
 *
 * @author William Fiset, william.alexandre.fiset@gmail.com
 **/

import static java.lang.Math.*;
import java.awt.geom.Point2D;

public class CircleCircleIntersection {

  private static final double EPS = 1e-6;

  // Due to double rounding precision the value passed into the acos
  // function may be outside its domain of [-1, +1] which would return
  // the value Double.NaN which we do not want.
  static double arccosSafe(double x) {
    if (x >= +1.0) return 0;
    if (x <= -1.0) return PI;
    return acos(x);
  }

  public static Point2D[] circleCircleIntersection(Point2D c1, double r1, Point2D c2, double r2) {
    
    // r is the smaller radius and R is bigger radius
    double r, R;
    
    // c is the center of the small circle
    // C is the center of the big circle
    Point2D c, C;
    
    // Determine which is the bigger/smaller circle
    if (r1 < r2) { r = r1; R = r2; c = c1; C = c2; } 
    else         { r = r2; R = r1; c = c2; C = c1; }
    
    double d = c1.distance(c2);

    // There are an infinite number of solutions
    // make sure to handle this however appropriate...
    if (d < EPS && abs(r-R) < EPS)
      return new Point2D[]{};
      // throw new IllegalStateException("Infinite number of solutions (circles are equal)");
    
    // No intersection (circles centered at same place with different size)
    else if (d < EPS) return new Point2D[]{};
    
    // Let (cx, cy) be the center of the small circle
    // Let (Cx, Cy) be the center of the larger circle
    double cx = c.getX(); double Cx = C.getX();
    double cy = c.getY(); double Cy = C.getY();

    // Compute the vector from the big circle to the little circle
    double vx = cx - Cx;
    double vy = cy - Cy;

    // Scale the vector by R and offset the vector so that the head of the vector 
    // is positioned on the circumference of the big circle ready to be rotated
    double x = ( vx / d) * R + Cx;
    double y = ( vy / d) * R + Cy;
    Point2D point = new Point2D.Double(x, y);

    // Single intersection (kissing circles)
    if (abs((R+r)-d) < EPS || abs(R-(r+d)) < EPS)
      return new Point2D[]{ point };
    
    // No intersection. Either the small circle contained within 
    // big circle or circles are simply disjoint.
    if (d + r < R || R + r < d)
      return new Point2D[]{};

    // Find the angle of rotation via cos law
    double angle = arccosSafe((r*r-d*d-R*R)/(-2*d*R));

    // Find the two unique intersection points      
    Point2D pt1 = rotatePoint( C, point,  angle );
    Point2D pt2 = rotatePoint( C, point, -angle );

    return new Point2D[]{pt1, pt2};

  }

  // Rotate point 'pt' a certain number of radians clockwise 
  // relative to some fixed point 'fp'. Note that the angle
  // should be specified in radians, not degrees.
  public static Point2D rotatePoint(Point2D fp, Point2D pt, double angle) {
    
    double fpx = fp.getX(); double fpy = fp.getY();
    double ptx = pt.getX(); double pty = pt.getY();
    
    // Compute the vector <x, y> from the fixed point
    // to the point of rotation.
    double x = ptx - fpx;
    double y = pty - fpy;

    // Apply the clockwise rotation matrix to the vector <x, y>
    // |  cosθ sinθ ||x|   |  xcosθ + ysinθ |
    // | -sinθ cosθ ||y| = | -xsinθ + ycosθ |
    double xRotated = x*cos(angle) + y*sin(angle);
    double yRotated = y*cos(angle) - x*sin(angle);

    // The rotation matrix rotated the vector about the origin, so we 
    // need to offset it by the point (fpx, fpy) to get the right answer
    return new Point2D.Double(fpx + xRotated, fpy + yRotated);

  }

  public static void main(String[] args) {

    Point2D center1 = new Point2D.Double(3,-5);
    Point2D center2 = new Point2D.Double(2,-5);
    Point2D center3 = new Point2D.Double(6,-5);
    Point2D center4 = new Point2D.Double(3,-7);
    Point2D center5 = new Point2D.Double(3,-10);
    
    double radius1 = 2;
    double radius2 = 1;
    double radius3 = 1;
    double radius4 = 1;
    double radius5 = 0.5;

    Point2D[] pts = circleCircleIntersection(center1,radius1,center2,radius2);
    displayIntersectionPoints(pts);

    pts = circleCircleIntersection(center1,radius1,center3,radius3);
    displayIntersectionPoints(pts);

    pts = circleCircleIntersection(center1,radius1,center4,radius4);
    displayIntersectionPoints(pts);

    pts = circleCircleIntersection(center1,radius1,center5,radius5);
    displayIntersectionPoints(pts);

    Point2D center6 = new Point2D.Double(3,1);
    Point2D center7 = new Point2D.Double(4,1);

    double radius6 = 2.0;
    double radius7 = sqrt(2);

    pts = circleCircleIntersection(center6,radius6,center7,radius7);
    displayIntersectionPoints(pts);

    Point2D center8 = new Point2D.Double(9,1);
    Point2D center9 = new Point2D.Double(12,1);
    Point2D center10 = new Point2D.Double(9,1);

    double radius8 = 2;
    double radius9 = sqrt(1.1);
    double radius10 = 0.25;
    
    pts = circleCircleIntersection(center8,radius8,center9,radius9);
    displayIntersectionPoints(pts);

    pts = circleCircleIntersection(center8,radius8,center10,radius10);
    displayIntersectionPoints(pts);
    
    int n = 2000;
    int multiplier = 10000;
    double  R[] = new double[n];
    Point2D C[] = new Point2D[n];
    for (int i = 0; i < n; i++) {
      double r = (Math.random()) * multiplier;
      double x = (Math.random()) * multiplier;
      double y = (Math.random()) * multiplier;
      R[i] = r; C[i] = new Point2D.Double(x, y);
    }
    
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        pts = circleCircleIntersection(C[i], R[i], C[j], R[j]);
        for (Point2D p : pts) {
          if (Double.isNaN(p.getX())) System.out.println("ERR");
          if (Double.isNaN(p.getY())) System.out.println("ERR");
        }
      }
    }

    Point2D c1 = new Point2D.Double(3, 0);
    double r1 = 2.5;
    Point2D c2 = new Point2D.Double(5, 0);
    double r2 = 1.5;
    Point2D[] intersectionPoints = circleCircleIntersection(c1, r1, c2, r2);
    System.out.println(intersectionPoints[0]);    
    System.out.println(intersectionPoints[1]);    

  }

  private static void displayIntersectionPoints(Point2D[] pts) {
    System.out.println("Circle intersections: ");
    for (Point2D p: pts) System.out.println(p);
  }

}




