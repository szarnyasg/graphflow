package ca.waterloo.dsg.graphflow.query.executors;

import org.junit.Assert;
import org.junit.Test;

public class GenericJoinIntersectionRuleTest {

    GenericJoinIntersectionRule obj_equal_1 = new GenericJoinIntersectionRule(0, false);
    GenericJoinIntersectionRule obj_equal_2 = new GenericJoinIntersectionRule(0, false);
    GenericJoinIntersectionRule obj_different = new GenericJoinIntersectionRule(1, true);

    @Test
    public void equalObjects() throws Exception {
        Assert.assertTrue(obj_equal_1.isSameAs(obj_equal_2));
    }

    @Test
    public void notEqualObjects() throws Exception {
        Assert.assertFalse(obj_equal_1.isSameAs(obj_different));
        Assert.assertFalse(obj_equal_2.isSameAs(obj_different));
    }
}
