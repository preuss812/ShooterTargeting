# ShooterTargeting
Helper app to find coefficients for a shooter

Currently the program calculates trajectories for a basketball like shooting exercise
where the "basket" is set at 72 inches height and the shooter height is "15" inches height.
The projectile is a 6 inch ball weighing about 0.25 kg.
The program steps through various RPMs for the flywheel in the shooter and does an iterative
approximation to track the trajectory of the ball considering gravity and wind resistance (aka drag).
The rpm to distance data points are then fit to a polynomial to come up with coefficients
that could be used for a shooter to calculate the required rpm given the distance of the shooter
from the target.

Here is a sample of the output:
    Fitted polynomial coefficients: ax^3 + bx^2 + cx + d where x = distance from shooter to target in meters
    a =   2.60938
    b = -26.01130
    c = 388.74040
    d = 498.29348

The program also outputs a set of trajectories several rpm values that can be cut and pasted into
excel or google sheets to draw a graph of the trajectories for visual inspection.

The real world correlation to these simulated results are not tested yet so beware that there
may be problems yet to be discovered.
