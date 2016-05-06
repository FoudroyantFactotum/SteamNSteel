package mod.steamnsteel.utility;

import net.minecraft.util.Vec3;

import java.awt.geom.Point2D;

public class BezierCurve
{
    public static Point2D.Double GetPointOnCurve(Point2D.Double p0, Point2D.Double p1, Point2D.Double p2, Point2D.Double p3, double t)
    {
        double u = 1 - t;
        double tt = t * t;
        double uu = u * u;
        double uuu = uu * u;
        double ttt = tt * t;

        Point2D.Double p = new Point2D.Double(p0.x, p0.y);
        p.x *= uuu;
        p.y *= uuu;

        p.x += 3 * uu * t * p1.x;
        p.y += 3 * uu * t * p1.y;

        p.x += 3 * u * tt * p2.x;
        p.y += 3 * u * tt * p2.y;

        p.x += ttt * p3.x;
        p.y += ttt * p3.y;

        return p;
    }

    /**
     * Computes pos on a cubic bezier curve.
     * @param bezier 4 in length array containing control points
     * @param t 0-1 representing a point along the curve
     * @param pos 3 in length array with resultant value
     */
    public static void get3DPointOnCurve(final Vec3[] bezier, final double t, final double[] pos)
    {
        final double u = 1 - t;
        final double tt = t * t;
        final double uu = u * u;
        final double uuu = uu * u;
        final double ttt = tt * t;

        pos[0] =  bezier[0].xCoord;              pos[1] =  bezier[0].yCoord;              pos[2] =  bezier[0].zCoord;
        pos[0] *= uuu;                           pos[1] *= uuu;                           pos[2] *= uuu;
        pos[0] += 3 * uu * t * bezier[1].xCoord; pos[1] += 3 * uu * t * bezier[1].yCoord; pos[2] += 3 * uu * t * bezier[1].zCoord;
        pos[0] += 3 * u * tt * bezier[2].xCoord; pos[1] += 3 * u * tt * bezier[2].yCoord; pos[2] += 3 * u * tt * bezier[2].zCoord;
        pos[0] += ttt * bezier[3].xCoord;        pos[1] += ttt * bezier[3].yCoord;        pos[2] += ttt * bezier[3].zCoord;
    }
}
