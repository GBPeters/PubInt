package nl.gijspeters.pubint.otpentry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gijspeters on 02-10-16.
 *
 * This class creates an entry point to OTP and handles all requests to OTP.
 *
 */
public class OTPHandler {

    private static Set<OTPEntry> instances = new HashSet<>();

    public static OTPEntry getInstance() {
        return getInstances(1).get(0);
    }

    public static List<OTPEntry> getInstances(int numInstances) {
        while (instances.size() < numInstances) {
            instances.add(new OTPEntry());
        }
        int i = 0;
        List<OTPEntry> instances = new ArrayList<>();
        for (OTPEntry instance : OTPHandler.instances) {
            if (!instance.hasStarted()) {
                try {
                    instance.restartOTP();
                    instances.add(instance);
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                instances.add(instance);
                i++;
            }
            if (i >= numInstances) {
                break;
            }
        }
        return instances;
    }

}
