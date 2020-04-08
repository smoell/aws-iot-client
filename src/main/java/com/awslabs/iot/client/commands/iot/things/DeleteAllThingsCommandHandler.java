package com.awslabs.iot.client.commands.iot.things;

import com.amazonaws.services.iot.model.ThingAttribute;
import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.exceptions.ThingAttachedToPrincipalsException;
import com.awslabs.iot.helpers.interfaces.V1ThingHelper;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;

public class DeleteAllThingsCommandHandler implements IotCommandHandler {
    private static final String DELETEALLTHINGS = "delete-all-things";
    private static final Logger log = LoggerFactory.getLogger(DeleteAllThingsCommandHandler.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    Provider<V1ThingHelper> thingHelperProvider;

    @Inject
    public DeleteAllThingsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        V1ThingHelper thingHelper = thingHelperProvider.get();

        thingHelper.listThingAttributes()
                .forEach(thingAttributes -> attemptDeleteThing(thingHelper, thingAttributes));
    }

    private void attemptDeleteThing(V1ThingHelper thingHelper, ThingAttribute thingAttribute) {
        String thingName = thingAttribute.getThingName();

        if (thingHelper.isThingImmutable(thingName)) {
            log.info("Skipping thing [" + thingName + "] because it is an immutable thing");
            return;
        }

        log.info("Deleting thing [" + thingName + "]");

        Try.run(() -> thingHelper.delete(thingName))
                .recover(ThingAttachedToPrincipalsException.class, this::logAndSkipDelete)
                // Rethrow all other exceptions
                .get();
    }

    private Void logAndSkipDelete(ThingAttachedToPrincipalsException thingAttachedToPrincipalsException) {
        log.info("Thing is still attached to principals, skipping");

        return null;
    }

    @Override
    public String getCommandString() {
        return DELETEALLTHINGS;
    }

    @Override
    public String getHelp() {
        return "Deletes all things.";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }
}
