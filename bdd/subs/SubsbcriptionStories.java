package bdd.subs;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

import java.util.LinkedList;
import java.util.List;

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.CONSOLE;

/**
 * Created by i316946 on 3/10/19.
 */
public class SubsbcriptionStories extends JUnitStories {

    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(this.getClass()))
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withCodeLocation(codeLocationFromClass(this.getClass()))
                        .withFormats(CONSOLE));
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(),
                new CheckAllSubscriptionStep(),
                new ActivateSubscriptionSteps(),
                new CancelSubscriptionSteps(),
                new LoginSteps()
        );
    }

    @Override
    protected List<String> storyPaths() {
        List<String> stories = new LinkedList<>();
        stories.add("list_subscription.story");
        stories.add("activate_subscription.story");
        stories.add("cancel_subscription.story");
        stories.add("login.story");
        return stories;
    }
}
