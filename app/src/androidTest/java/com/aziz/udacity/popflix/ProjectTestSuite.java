package com.aziz.udacity.popflix;

import android.test.suitebuilder.TestSuiteBuilder;
import junit.framework.Test;

public class ProjectTestSuite {
    public static Test suite () {
        return new TestSuiteBuilder(ProjectTestSuite.class)
            .includeAllPackagesUnderHere()
            .build();
    }
}