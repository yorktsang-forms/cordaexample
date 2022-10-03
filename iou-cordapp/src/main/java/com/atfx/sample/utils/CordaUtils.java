package com.atfx.sample.utils;

import net.corda.core.identity.Party;
import net.corda.core.node.NodeInfo;
import net.corda.core.node.ServiceHub;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CordaUtils {
    public static Party getServiceHubPreferredNotary(ServiceHub serviceHub) {
        return serviceHub.getNetworkMapCache().getNotaryIdentities().get(0);
    }

    public static Party getServiceHubMyIdentity(ServiceHub serviceHub) {
        return serviceHub.getMyInfo().getLegalIdentities().get(0);
    }

    public static List<Party> getServiceHubAllParties(ServiceHub serviceHub) {
        List<Party> notaries = serviceHub.getNetworkMapCache().getNotaryIdentities();
        ArrayList<Party> result = new ArrayList<>();
        for (NodeInfo nodeInfo: serviceHub.getNetworkMapCache()
                .getAllNodes()) {
            Party identity = nodeInfo.getLegalIdentities().get(0);
            if(!notaries.contains(identity)) {
                result.add(identity);
            }
        }
        return result;
    }
}
