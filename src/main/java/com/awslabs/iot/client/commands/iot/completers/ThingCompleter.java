package com.awslabs.iot.client.commands.iot.completers;

import com.awslabs.iot.client.completers.DynamicStringsCompleter;
import com.awslabs.iot.client.helpers.CandidateHelper;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import org.jline.reader.Candidate;
import software.amazon.awssdk.services.iot.model.ThingAttribute;

import javax.inject.Inject;
import java.util.List;

public class ThingCompleter extends DynamicStringsCompleter {
    @Inject
    V2IotHelper v2IotHelper;
    @Inject
    CandidateHelper candidateHelper;

    @Inject
    public ThingCompleter() {
    }

    @Override
    public List<Candidate> getStrings() {
        return candidateHelper.getCandidates(v2IotHelper.getThings().map(ThingAttribute::thingName));
    }
}
