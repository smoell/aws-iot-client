package com.awslabs.iot.client.commands.iot.completers;

import com.awslabs.iot.client.completers.DynamicStringsCompleter;
import com.awslabs.iot.client.helpers.CandidateHelper;
import com.awslabs.iot.helpers.interfaces.V1PolicyHelper;
import org.jline.reader.Candidate;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class PolicyCompleter extends DynamicStringsCompleter {
    @Inject
    Provider<V1PolicyHelper> policyHelperProvider;
    @Inject
    CandidateHelper candidateHelper;

    @Inject
    public PolicyCompleter() {
    }

    @Override
    public List<Candidate> getStrings() {
        return candidateHelper.getCandidates(policyHelperProvider.get().listPolicyNames());
    }
}
