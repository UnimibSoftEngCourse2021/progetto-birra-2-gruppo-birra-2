package it.progettois.brewday.common.util;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class LPOptimizerUtil {

    MPSolver solver = MPSolver.createSolver("GLOP");


}
